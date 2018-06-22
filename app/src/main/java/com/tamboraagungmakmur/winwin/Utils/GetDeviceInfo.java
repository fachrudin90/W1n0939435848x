package com.tamboraagungmakmur.winwin.Utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by Tambora on 10/02/2017.
 */
public class GetDeviceInfo {

    public static String getInfo(Context context) {

        String brandPhone = Build.BRAND;
        String modelPhone = Build.MODEL;
        String osPhone = Build.VERSION.RELEASE;
        //TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //String device_id = tm.getDeviceId();
        SessionManager sess = new SessionManager(context);

        return brandPhone + " " + modelPhone + " " + osPhone;
    }
}
