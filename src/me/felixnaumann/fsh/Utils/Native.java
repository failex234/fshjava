package me.felixnaumann.fsh.Utils;

public class Native {

    static {
        System.loadLibrary("fshutils");
    }

    public static native boolean isAdministrator();
    public static native int changeWorkingDirectory(String dirpath);
    public static native String getWorkingDirectory();
}
