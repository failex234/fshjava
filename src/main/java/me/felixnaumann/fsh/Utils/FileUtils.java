package me.felixnaumann.fsh.Utils;

import me.felixnaumann.fsh.Debug.DebuggingTools;

public class FileUtils {
	
	public static String[] windowsFileExtensions = new String[]{"com", "exe", "bat", "msi", "scr"};

    public static void setWorkingDirectory(String wd) {
        String currdir = Native.getWorkingDirectory();
        int result = Native.changeWorkingDirectory(wd);
        String newdir = Native.getWorkingDirectory();

        if (result == -1 && currdir.equals(newdir)) {
            System.err.println("fsh: access denied");
        } else if ((result == -2 || result == -3) && currdir.equals(newdir)) {
            System.err.println("fsh: no such file or directory");
        } else {
            DebuggingTools.logf("Changed to %s\n", wd);
            System.setProperty("user.dir", Native.getWorkingDirectory());
        }
    }
}
