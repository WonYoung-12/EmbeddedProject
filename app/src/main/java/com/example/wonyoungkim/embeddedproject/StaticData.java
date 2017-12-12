package com.example.wonyoungkim.embeddedproject;

/**
 * Created by user on 2017-12-05.
 */

public class StaticData {
    private static boolean isSOS = false;

    private static final String ip = "203.252.166.225";
    private static final int port = 8080;

    private static final String[] phoneNumbers = {"01063953275", "01067476302"};

    public static boolean isSOS() {
        return isSOS;
    }

    public static void setIsSOS(boolean isSOS) {
        StaticData.isSOS = isSOS;
    }

    public static String getIp() {
        return ip;
    }

    public static int getPort() {
        return port;
    }

    public static String[] getPhoneNumbers() {
        return phoneNumbers;
    }
}
