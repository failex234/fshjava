package me.felixnaumann.reflection;

import me.felixnaumann.reflection.Utils.FileUtils;
import me.felixnaumann.reflection.Utils.Native;

public class Commands {

    public int _echo(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("");
            return 0;
        }

        String finalstring = "";
        int pos = 0;

        for (String part : args) {
            if (pos != args.length - 1) {
                finalstring += part + " ";
            } else {
                finalstring += part;
            }
            pos++;
        }
        System.out.println(finalstring);
        return 0;
    }

    public int _false(String[] args) {
        return 1;
    }

    public int _pwd(String[] args) {
        System.out.println(Native.getWorkingDirectory());
        return 0;
    }

    public int _cd(String[] args) {
        if (args == null || args.length == 0) {
            FileUtils.setWorkingDirectory(System.getProperty("user.home"));
        }
        FileUtils.setWorkingDirectory(args[0]);

        return 0;
    }
}
