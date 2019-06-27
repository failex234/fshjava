package me.felixnaumann.fsh.Utils;

import me.felixnaumann.fsh.FshMain;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Native {

    private static String libname;

    static {
        try {
            System.loadLibrary("fshutils");
        }
        catch (UnsatisfiedLinkError e) {
            try {
                loadLibraryFromJar();
            }
            catch (UnsatisfiedLinkError ex) {
                System.err.println("!!!!!!!!!!");
                System.err.println("Unable to load necessary library. Can't continue without");
                System.err.println("!!!!!!!!!!");
                System.err.println();
                System.err.println("Please make sure to have the library " + libname + " in one of the following folders:");

                String[] allpaths = System.getProperty("java.library.path").split(";");
                for(String path : allpaths) {
                    System.err.println(path);
                }

                System.exit(1);
            }
        }
    }

    private static void loadLibraryFromJar() throws UnsatisfiedLinkError {
        if (FshMain.os.equals("win")) {
            libname = "fshutils.dll";
        } else {
            libname = "libfshutils.so";
        }

        File tempfile = new File(System.getProperty("java.io.tmpdir") + "/" + libname);
        InputStream inputStream = Native.class.getResourceAsStream("/" + libname);

        if (inputStream == null) {
            throw new UnsatisfiedLinkError("No packaged library with the name \"" + libname + "\" found");
        }

        try {
            Files.copy(inputStream, tempfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.loadLibrary(tempfile.getAbsolutePath());
        tempfile.deleteOnExit();

    }

    public static native boolean isAdministrator();
    public static native int changeWorkingDirectory(String dirpath);
    public static native String getWorkingDirectory();
    public static native String getOS();
    public static native void clearConsole();
}
