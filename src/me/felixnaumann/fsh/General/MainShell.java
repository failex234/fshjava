package me.felixnaumann.fsh.General;

import me.felixnaumann.fsh.Debug.DebuggingTools;
import me.felixnaumann.fsh.Formatting.Variables;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static me.felixnaumann.fsh.Formatting.Variables.*;
import static me.felixnaumann.fsh.FshMain.klasse;

public class MainShell {
    public static int lastRet = 0;

    public static void interpretLine(String line) {
        Method[] methods = Commands.class.getDeclaredMethods();

        if (line.equals("help")) {
            System.out.println("available commands: ");
            for (Method m : methods) {
                if (m.getName().startsWith("F")) continue;
                System.out.println(m.getName().replace("_", ""));
            }
        } else if (line.startsWith("$")) {
            DebuggingTools.logf("printing variable %s\n", line);
            System.out.println(getVar(line));
        } else {
            if (line.contains("=")) {
                DebuggingTools.logf("parsing variable line %s\n", line);
                parseVarLine(line);
            } else {
                //TODO: put all code into launchBuiltInProgram in GeneralUtils
                CommandExecutor.launchBuiltinProgram(line, methods);
            }
        }
    }
}
