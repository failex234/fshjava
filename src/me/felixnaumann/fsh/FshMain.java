package me.felixnaumann.fsh;

import me.felixnaumann.fsh.Formatting.Variables;
import me.felixnaumann.fsh.General.Commands;
import me.felixnaumann.fsh.Utils.GeneralUtils;

import java.util.*;

import static me.felixnaumann.fsh.General.MainShell.interpretLine;

public class FshMain {

    private static final String VERSION = "0.3.4";

    public static Commands klasse;
    public static HashMap<String, String> vars;
    public static boolean debug = false;

    public static void main(String[] args) {
        if (args.length != 0) {
            switch (args[0]) {
                case "--debug":
                    debug = true;
                    break;
                case "--help":
                    System.out.println("usage: fsh [arguments]...");
                    System.out.println("fsh is a shell written in java. aiming to be very similar to bash");
                    System.out.print("\n");
                    System.out.println("--debug\t\t- enable debug mode");
                    System.out.println("--help\t\t- show this menu");
                    System.out.println("--version\t- show version information");
                    System.out.println("\nThis program is licensed under the MIT license");
                    System.out.println("Git-Repo: https://github.com/failex234/fshjava");
                    System.exit(0);
                    break;
                case "--version":
                    System.out.printf("fsh version %s by Felix Naumann (failex234)\n", VERSION);
                    System.exit(0);
                    break;
            }
        }
        klasse = new Commands();
        vars = new HashMap<>();
        if (GeneralUtils.getOS().equals("nix")) {
            vars.put("PATH", "/bin:/usr/bin:/sbin:/usr/sbin");
        } else {
            vars.put("PATH", "C:\\Windows;C:\\Windows\\System32");
        }
        vars.put("PS1", "[\\u@\\h \\w]\\$ ");

        String input = "";
        Scanner scanner = new Scanner(System.in);
        while (!input.equals("exit")) {
            System.out.print(Variables.getVar("PS1"));
            input = scanner.nextLine();

            if (!input.equals("exit")) {
                interpretLine(input);
            }
        }
    }



}
