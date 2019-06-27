package me.felixnaumann.fsh.General;

import me.felixnaumann.fsh.Enums.CommandDescriptions;
import me.felixnaumann.fsh.Utils.FileUtils;
import me.felixnaumann.fsh.Utils.GeneralUtils;
import me.felixnaumann.fsh.Utils.Native;

import java.io.File;

public class Commands {

    public int _echo(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("");
            return 0;
        }

        String finalstring = "";
        int pos = 0;

        for (String part : args) {
            if (pos != args.length - 1) {
                finalstring += part + " ";
            } else {
                finalstring += part;
            }
            pos++;
        }
        System.out.println(finalstring);
        return 0;
    }

    public int _false(String[] args) {
        return 1;
    }

    public int _true(String[] args) { return 0;}

    public int _pwd(String[] args) {
        System.out.println(Native.getWorkingDirectory());
        return 0;
    }

    public int _cd(String[] args) {
        if (args == null || args.length == 0) {
            FileUtils.setWorkingDirectory(System.getProperty("user.home"));
            return 0;
        }
        FileUtils.setWorkingDirectory(args[0]);

        return 0;
    }

    public int _cls(String[] args) {
        //Let the native function handle the console clearing
        Native.clearConsole();
        return 0;
    }

    public int _clear(String[] args) {
        return _cls(args);
    }

    public int _uname(String[] args) {
        String unamestring = "";
        boolean kname = false, nname = false, mname = false, vname = false;
        if (args == null || args.length == 0) {
            System.out.println(System.getProperty("os.name"));
            return 0;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[0].equals("--help")) {
                F_usage("uname");
                return 0;
            } else if (args[0].equals("-a")) {
                kname = true;
                nname = true;
                vname = true;
                mname = true;
                break;
            } else if (args[i].equals("-s") && !kname) {
                kname = true;
            } else if (args[i].equals("-n") && !nname) {
                nname = true;
            } else if (args[i].equals("-v") && !vname) {
                vname = true;
            } else if (args[i].equals("-m") && !mname) {
                mname = true;
            } else {
                System.err.printf("uname: invalid argument %s\n", args[i]);
                System.err.println("get some help with uname --help");
                return 1;
            }
        }

        if (kname) unamestring += System.getProperty("os.name");
        if (nname) unamestring += " " + GeneralUtils.getHostname();
        if (vname) unamestring += " " + System.getProperty("os.version");
        if (mname) unamestring += " " + System.getProperty("os.arch");

        unamestring = unamestring.replaceAll("^\\s", "");

        System.out.println(unamestring);
        return 0;
    }

    public int _ls(String[] args) {
        File currdir = null;
        if (args.length == 0) {
            currdir = new File(".");
        } else if (args.length == 1){
            currdir = new File(args[0]);
        } else {
            for (String file : args) {
                _ls(new String[]{file});
            }
            return 0;
        }
        if (currdir.isDirectory()) {
            File[] allfiles = currdir.listFiles();
            for (File file : allfiles) {
                System.out.print(file.getName() + " ");
            }
        } else if (currdir.exists()) {
            System.out.printf(currdir.getPath());
        }

        System.out.println();
        return 0;
    }

    public void F_usage(String prgname) {
        String[] descs;
        switch (prgname) {
            case "uname":
                descs = CommandDescriptions.UNAME_DESC.getDescription();
                break;
                default:
                    descs = new String[]{};
        }

        System.out.printf("usage: %s\n", descs[0]);
        System.out.printf("%s\n", descs[1]);
        System.out.print("\n");

        for (int i = 2; i < descs.length; i += 2) {
            System.out.printf("%s\t\t- %s\n", descs[i], descs[i + 1]);
        }
    }
}
