/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Wesley Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


package me.wes.command.test.util;

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
