package com.example.databaseproject;

import android.util.Log;

public class Logger {
    private static final boolean verbose = true;

    public static void d(String tag, String msg) {
        if (verbose) {
            Log.d(tag, msg);
//            System.out.println(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (verbose) {
            Log.e(tag, msg);
        }
    }
}
