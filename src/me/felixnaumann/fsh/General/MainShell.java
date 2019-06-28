package me.felixnaumann.fsh.General;

import me.felixnaumann.fsh.Debug.DebuggingTools;
import me.felixnaumann.fsh.Formatting.Variables;
import me.felixnaumann.fsh.Utils.GeneralUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static me.felixnaumann.fsh.Formatting.Variables.*;
import static me.felixnaumann.fsh.FshMain.klasse;

public class MainShell {
    public static int lastRet = 0;

    public static void interpretLine(String line) {
        Method[] methods = Commands.class.getDeclaredMethods();

        if (line.equals("help") || (line.length() > 4 && line.startsWith("help") && line.charAt(4) == ' ')) {
            System.out.println("available commands: ");
            for (Method m : methods) {
                if (m.getName().startsWith("F")) continue;
                System.out.println(m.getName().replace("_", ""));
            }
        } else {
            if (!GeneralUtils.checkForClosedQuotes(line)) {
                System.err.println("fsh: missing closing quote");
                lastRet = 1;
                return;
            }
            if (line.contains("=")) {
                DebuggingTools.logf("parsing variable line %s\n", line);
                parseVarLine(line);
            } else {
                //TODO: put all code into launchBuiltInProgram in GeneralUtils
                //TODO: remove redundant spaces outside of quotes
                CommandExecutor.launchBuiltinProgram(line, methods);
            }
        }
    }
}
