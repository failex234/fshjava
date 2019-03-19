package me.felixnaumann.reflection.General;

import me.felixnaumann.reflection.Commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static me.felixnaumann.reflection.Formatting.Variables.*;
import static me.felixnaumann.reflection.FshMain.klasse;

public class MainShell {
    public static int lastRet = 0;

     public static void interpretLine(String line) {
        Method[] methods = Commands.class.getDeclaredMethods();
        boolean methodfound = false;

        if (line.equals("help")) {
            System.out.println("available commands: ");
            for (Method m : methods) {
                System.out.println(m.getName().replace("_", ""));
            }
        } else if (line.startsWith("$")) {
            System.out.println(getVar(line));
        } else {
            if (line.contains("=")) {
                parseVarLine(line);
            } else {
                String replaced = replaceVars(line);
                try {
                    String[] args = replaced.split(" ");

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

                            lastRet = (int) m.invoke(klasse, (Object) newargs);
                            break;
                        }
                    }

                    if (!methodfound) {
                        System.err.println("fsh: command not found");
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
