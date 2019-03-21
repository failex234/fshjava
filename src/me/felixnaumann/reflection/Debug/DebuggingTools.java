package me.felixnaumann.reflection.Debug;

import me.felixnaumann.reflection.FshMain;

public class DebuggingTools {
    private static final boolean DEBUG = FshMain.debug;

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

}
