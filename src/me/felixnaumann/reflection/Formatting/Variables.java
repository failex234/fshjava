package me.felixnaumann.reflection.Formatting;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static me.felixnaumann.reflection.General.MainShell.lastRet;
import static me.felixnaumann.reflection.FshMain.vars;
import static me.felixnaumann.reflection.Debug.DebuggingTools.*;

public class Variables {
    private static final String VAR_REPLACE_EXPR = "^\\$%s$|^\\$%s\\s|\\s\\$%s$|\\s\\$%s\\s|\\$\\(%s\\)";

    public static String getVar(String var) {
        String varname = var.replace("$", "");
        if (varname.equals("@")) {
            return lastRet + "";
        } else if (var.equals("PS1")) {
            String rawps1 = vars.get(varname);
            rawps1 = rawps1.replace("\\u", System.getProperty("user.name"));
            if (System.getProperty("user.dir").equals(System.getProperty("user.home"))) {
                rawps1 = rawps1.replace("\\w", "~");
            } else {
                rawps1 = rawps1.replace("\\w", System.getProperty("user.dir").replace("\\", "\\\\"));
            }
            //rawps1.replace("\\$", UserCheck.isAdminstrator() ? "$" : "#");
            rawps1 = rawps1.replace("\\$", "#");
            try {
                rawps1 = rawps1.replace("\\h", InetAddress.getLocalHost().getHostName());
            } catch (UnknownHostException e) {
                rawps1 = rawps1.replace("\\h", "localhost");
            }
            return rawps1;
        } else {
            if (vars.get(varname) != null) return vars.get(varname);
            return "";
        }
    }

    private static void setVar(String varname, String val) {
        if (vars.containsKey(varname)) {
            vars.replace(varname, val);
            System.out.printf("fsh: changed value of %s to \"%s\"\n", varname, val);
        } else {
            vars.put(varname, val);
            System.out.printf("fsh: set variable %s to \"%s\"\n", varname, val);
        }
    }

    public static void parseVarLine(String line) {
        String[] allelems = line.split("=");

        //Something is missing
        if (allelems.length != 2) {
            System.err.println("fsh: invalid variable setting syntax!");
            return;
        } else if (allelems[0].isEmpty() || allelems[0].replace(" ", "").length() == 0) {
            //First parameter is missing or only containing spaces
            System.err.println("fsh: the variable name can't be empty!");
            return;
        } else if (allelems[1].isEmpty() || allelems[1].replace(" ", "").length() == 0) {
            //Second parameter is missing or only containing spaces
            System.err.println("fsh: the variable value can't be empty!");
            return;
        }

        if (allelems[0].contains(" ") && allelems[0].charAt(allelems[0].length() - 1) != ' ') {
            System.err.println("fsh: variable name can't contain spaces!");
            return;
        }

        String val, varname = allelems[0].replace(" ", "");

        if (allelems[1].charAt(0) == ' ') {
            val = allelems[1].replaceAll("^\\s", "");
            val = val.replaceAll("\\s{2,}", " ");
        } else {
            val = allelems[1];
        }

        setVar(varname, val);
    }

    public static String replaceVars(String line) {
        String temp = line;

        for (String var : vars.keySet()) {
            String format = String.format(VAR_REPLACE_EXPR, var, var, var, var, var);
            temp = temp.replaceAll(format, " " + getVar(var) + " ");
        }
        logf("Replaced with vars: %s\n", temp);
        return temp;
    }
}
