package me.wes.command.test.util;

/**
 * ******************************************************************************************************
 * Copyright (C) 2015 Wesley Smith under the Copyright Law of the United States. All rights reserved.   *
 * All code contained in this document is sole property of Wesley Smith. Do NOT distribute, reproduce,  *
 * take snippets, or take this code under your name without exclusive permission from Wesley Smith.     *
 * Not following this statement will result in a void of all agreements made, and possibly legal action.*
 * If you have any questions or concerns, please contact me via email with the address of               *
 * wesjavadev@gmail.com                                                                                 *
 * Thanks for your cooperation.                                                                         *
 * ******************************************************************************************************
 */
public class Logging {

    private static final String PREFIX = "Sponge Testing";

    public static void logStatistic(String message) {
        logWithExtra("Statistic", message);
    }

    public static void logDebug(String message) {
        logWithExtra("Debug", message);
    }

    public static void logError(String message) {
        logWithExtra("Error", message);
    }

    public static void logWarning(String message) {
        logWithExtra("Warning", message);
    }

    public static void logInfo(String message) {
        logWithExtra("Info", message);
    }

    public static void logWithExtra(String extra, String message) {
        System.out.println(PREFIX + ": " + extra + " | " + message);
    }

    public static void log(String message) {
        System.out.println(PREFIX + " | " + message);
    }

}
