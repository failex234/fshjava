package me.felixnaumann.fsh.Debug;

import me.felixnaumann.fsh.FshMain;

public class DebuggingTools {
    private static boolean DEBUG = FshMain.debug;

    public static void log(String line) {
        if (DEBUG) {
            System.out.println("[debug] " + line);
        }
    }

    public static void logf(String line, Object... format) {
        if (DEBUG) {
            System.out.printf("[debug] " + line, format);
        }
    }

    public static void logef(String line, Object... format) {
        if (DEBUG) {
            System.err.printf("[debug] " + line, format);
        }
    }

    public static void loge(String line) {
        if (DEBUG) {
            System.err.println("[debug] " + line);
        }
    }

    public static boolean toggleDebug() {
        DEBUG = !DEBUG;
        return DEBUG;
    }

    public static boolean setDebug(boolean newstatus) {
        DEBUG = newstatus;
        return DEBUG;
    }

}
