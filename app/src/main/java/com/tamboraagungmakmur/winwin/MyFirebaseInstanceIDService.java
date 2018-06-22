package com.tamboraagungmakmur.winwin;

import android.app.Service;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

/**
 * Created by innan on 10/10/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "firebase_token";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        SharedPreferences sharedPref = getSharedPreferences("token", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", refreshedToken);
        editor.apply();
    }

}
