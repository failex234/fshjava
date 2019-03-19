package me.felixnaumann.reflection.Debug;

public class DebuggingTools {
    private static final boolean DEBUG = true;

    public static void log(String line) {
        if (DEBUG) {
            System.out.println("[DEBUG] " + line);
        }
    }

    public static void logf(String line, Object... format) {
        if (DEBUG) {
            System.out.printf("[DEBUG] " + line, format);
        }
    }

    public static void logef(String line, Object... format) {
        if (DEBUG) {
            System.err.printf("[DEBUG] " + line, format);
        }
    }

    public static void loge(String line) {
        if (DEBUG) {
            System.err.println("[DEBUG] " + line);
        }
    }

}
