package me.felixnaumann.reflection;

import me.felixnaumann.reflection.Utils.FileUtils;
import me.felixnaumann.reflection.Utils.Native;

import java.util.*;

import static me.felixnaumann.reflection.General.MainShell.interpretLine;

public class FshMain {

    public static Commands klasse;
    public static HashMap<String, String> vars;

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        System.out.println(System.getProperty("user.dir"));
        //System.out.println(Native.changeWorkingDirectory("C:\\Users\\fnauman1"));
        FileUtils.setWorkingDirectory("..");
        System.out.println(System.getProperty("user.dir"));
        klasse = new Commands();
        vars = new HashMap<>();
        vars.put("PATH", "/bin:/usr/bin:/sbin:/usr/sbin");
        vars.put("PS1", "[\\u@\\h \\w]\\$ ");

        String input = "";
        Scanner scanner = new Scanner(System.in);
        while (!input.equals("exit")) {
            System.out.print("# ");
            input = scanner.nextLine();

            if (!input.equals("exit")) {
                interpretLine(input);
            }
        }
    }



}
