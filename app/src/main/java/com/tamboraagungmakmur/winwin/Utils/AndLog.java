package com.tamboraagungmakmur.winwin.Utils;

import android.util.Log;

/**
 * Created by Tambora on 02/09/2016.
 */
public class AndLog {


    private static boolean hide = false;

    public static void ShowLog(String tag, String message) {
        if (!hide) {
            Log.i(tag, message);
        }
    }
}
