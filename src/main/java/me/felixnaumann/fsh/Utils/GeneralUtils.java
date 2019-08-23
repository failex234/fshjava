package me.felixnaumann.fsh.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class GeneralUtils {
    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            return "localhost";
        }
    }

    //Check if there are quotes and if there are any check if they're closed
    public static boolean checkForClosedQuotes(String line) {
        if (line.contains("\"")) {
            int quotecount = 0;
            for (int i = 0; i < line.length(); i++) {
                //Go through the string and only count non-escaped quotes
                if(i == 0 && line.charAt(i) == '"' || line.charAt(i) == '"' && line.charAt(i - 1) != '\\') {
                    quotecount++;
                }
            }

            //If the count of quotes is even all quotes are closed
            if (quotecount % 2 == 0) return true;
            return false;
        }
        return true;
    }

    //Check if there are any quotes at all
    public static boolean chechForQuotes(String line) {
        if (!line.contains("\\")) return line.contains("\"");
        for (int i = 0; i < line.length(); i++) {
            if (i == 0 && line.charAt(i) == '"' || line.charAt(i) == '"' && line.charAt(i - 1) != '\\') {
                return true;
            }
        }
        return false;
    }

    public static String[] getArgs(String line) {
            if (!chechForQuotes(line)) {
            return line.split(" ");
        } else {
            ArrayList<String> tempArr = new ArrayList<>();
            boolean openQuote = false;
            int startidx = 0;

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '"') {
                    openQuote = !openQuote;
                }

                if (line.charAt(i) == ' ' && !openQuote && (i - 1 - startidx) > 1) {
                    if (!chechForQuotes(line.substring(startidx, i))) {
                        tempArr.add(line.substring(startidx, i));
                        startidx = i + 1;
                    } else {
                        tempArr.add(line.substring(startidx + 1, i - 1));
                        startidx = i + 1;
                    }
                }
            }
            String[] finalargs = new String[tempArr.size()];
            int finalargsIdx = 0;

            for (String arg : tempArr) {
                finalargs[finalargsIdx] = arg;
                finalargsIdx++;
            }

            return finalargs;

        }
    }

    public static String[] removeEscapeSlashes(String[] args) {
        String[] escaped = args.clone();
        for (int i = 0; i < escaped.length; i++) {
            String arg = escaped[i];
            String newarg = "";

            //Get the position of all slashes
            ArrayList<Integer> slashIndices = new ArrayList<>();
            for (int j = 0; j < arg.length(); j++) {
                if (arg.charAt(j) == '\\' && j != arg.length() - 1) {
                    //TODO: Check for allowed escapes
                    slashIndices.add(j);
                    j++;
                }
            }

            //Make a new string without the backslashes
            for (int j = 0; j < arg.length(); j++) {
                if (slashIndices.contains(j)) continue;
                newarg += arg.charAt(j);
            }
            escaped[i] = newarg;
        }

        return escaped;

    }

}
