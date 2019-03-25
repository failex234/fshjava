package me.felixnaumann.fsh.Utils;

import me.felixnaumann.fsh.General.Commands;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GeneralUtils {
    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            return "localhost";
        }
    }

    public static boolean isBuiltInProgram(String cmdname) {
        Method[] allmethods = Commands.class.getMethods();
        for (Method method : allmethods) {
            if (method.getName().equals("_" + cmdname)) return true;
        }
        return false;
    }

    public static void launchBuiltinProgram(String line) {

    }
}
