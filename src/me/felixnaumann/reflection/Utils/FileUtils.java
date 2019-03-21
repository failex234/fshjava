package me.felixnaumann.reflection.Utils;

import me.felixnaumann.reflection.Debug.DebuggingTools;

public class FileUtils {

    public static void setWorkingDirectory(String wd) {
        int result = Native.changeWorkingDirectory(wd);
        String newdir = Native.getWorkingDirectory();

        if (result == -1 && wd.equals(newdir)) {
            System.err.println("fsh: access denied");
        } else if (result == -2 && wd.equals(newdir)) {
            System.err.println("fsh: no such file or directory");
        } else if (result == -3 && wd.equals(newdir)) {
            System.err.println("fsh: this is not a directory");
        } else {
            DebuggingTools.logf("Changed to %s\n", wd);
            System.setProperty("user.dir", Native.getWorkingDirectory());
        }
    }
}
