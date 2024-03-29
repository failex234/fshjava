package me.felixnaumann.fsh.General;

import me.felixnaumann.fsh.Debug.DebuggingTools;
import me.felixnaumann.fsh.Formatting.Variables;
import me.felixnaumann.fsh.FshMain;
import me.felixnaumann.fsh.Utils.FileUtils;
import me.felixnaumann.fsh.Utils.GeneralUtils;
import me.felixnaumann.fsh.Utils.Native;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.felixnaumann.fsh.Formatting.Variables.replaceVars;
import static me.felixnaumann.fsh.FshMain.klasse;

public class CommandExecutor {

    private static boolean isAbsolutePath(String str) {
        if (System.getProperty("file.separator").equals("/")) {
            //*NIX code
            if (str.charAt(0) == '/') return true;
            return false;
        } else {
            //Windows code
            if (str.charAt(0) == '/') return true;
            if (str.charAt(0) == '\\') return true;
            //This regex matches things like C:\ or D:\ or C:/ or D:/
            if (str.matches("^.{1}:[\\\\/]")) return true;
            return false;
        }
    }

    private static boolean hasExtension(String str) {
        return str.matches(".+\\.{1}.+$");
    }

    public static void findAndLaunch(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("fsh: error");
            return;
        }

        String programname = args[0];
        String[] programargs = new String[args.length - 1];
        boolean launched = false;

        for (int i = 1; i < args.length; i++) {
            programargs[i - 1] = args[i];
        }

        if (isAbsolutePath(programname)) {
            try {
                launchProgram(programname, programargs);
                launched = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String[] allpaths;
            if (FshMain.os.equals("nix"))
                allpaths = FshMain.vars.get("PATH").split(":");
            else
                allpaths = FshMain.vars.get("PATH").split(";");

            if (hasExtension(programname)) {
                for (String path : allpaths) {
                    File temp = new File(path + System.getProperty("file.separator") + programname);
                    if (temp.exists()) {
                        try {
                            launchProgram(temp.getAbsolutePath(), programargs);
                            launched = true;
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                for (String path : allpaths) {
                    for (String extension : FileUtils.windowsFileExtensions) {
                        File temp;
                        if (FshMain.os.equals("nix"))
                            temp = new File(path + System.getProperty("file.separator") + programname);
                        else
                            temp = new File(path + System.getProperty("file.separator") + programname + "." + extension);

                        if (temp.exists()) {
                            try {
                                launchProgram(temp.getAbsolutePath(), programargs);
                                launched = true;
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if(FshMain.os.equals("nix")) break;
                    }
                    if (launched) break;
                }
            }
        }

        if (!launched) {
            System.err.println("fsh: command not found");
        }

    }

    private static int launchProgram(String programpath, String[] args) throws IOException {
        String[] newargs = new String[args.length + 1];
        newargs[0] = GeneralUtils.getProgramNameFromPath(programpath);

        for (int i = 1; i < args.length; i++) {
            newargs[i - 1] = args[i]; 
        }

        String[] env = new String[]{"HOME=" + Variables.getVar("HOME"), "PATH=" + Variables.getVar("PATH"), "USER=" + Variables.getVar("USER"), "SHELL=" + Variables.getVar("SHELL"), "TERM=" + Variables.getVar("TERM")};

        //TODO Fix JVM Crash when running with arguments !important

        return Native.runApplication(programpath, newargs, env);
    }

    public static boolean isBuiltInProgram(String cmdname) {
        Method[] allmethods = Commands.class.getMethods();
        for (Method method : allmethods) {
            if (method.getName().equals("_" + cmdname)) return true;
        }
        return false;
    }

    public static void launchBuiltinProgram(String line, Method[] methods) {
        boolean methodfound = false;
        if (line.startsWith("echo")) Variables.dontReplace = true;
        String replaced = replaceVars(line);
        if (line.startsWith("echo")) Variables.dontReplace = false;
        replaced = Variables.applyWildcardFilter(replaced);
        DebuggingTools.logf("File matches: %s\n", replaced);

        try {
            String[] args = GeneralUtils.removeEscapeSlashes(GeneralUtils.getArgs(replaced));

            for (Method m : methods) {
                if (m.getName().equals("_" + args[0])) {
                    methodfound = true;

                    ArrayList<String> temp = new ArrayList<>();
                    for (int i = 1; i < args.length; i++) {
                        temp.add(args[i]);
                    }

                    String[] newargs = new String[temp.size()];

                    for (int i = 0; i < temp.size(); i++) {
                        newargs[i] = temp.get(i);
                    }

                    MainShell.lastRet = (int) m.invoke(klasse, (Object) newargs);
                    break;
                }
            }

            if (!methodfound) {
                //System.err.println("fsh: command not found");
                //TODO: Fix arguments in quotes
                CommandExecutor.findAndLaunch(line.split(" "));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
