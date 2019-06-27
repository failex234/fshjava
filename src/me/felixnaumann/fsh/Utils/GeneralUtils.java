package me.felixnaumann.fsh.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GeneralUtils {
    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            return "localhost";
        }
    }

}
