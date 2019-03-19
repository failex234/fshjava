package me.felixnaumann.reflection.Utils;

import java.io.File;

public class FileUtils {

    public static void setWorkingDirectory(String wd) {
       File directory = new File(wd).getAbsoluteFile();
       if (directory.exists()) {
           System.setProperty("user.dir", directory.getAbsolutePath());
           System.out.println("cd!");
       } else {
           System.err.println("Dir not exists");
       }
    }
}
