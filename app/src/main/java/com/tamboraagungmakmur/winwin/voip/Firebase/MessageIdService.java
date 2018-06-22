package com.tamboraagungmakmur.winwin.voip.Firebase;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.voip.Utils.AndLog;
import com.tamboraagungmakmur.winwin.voip.Utils.AppConf;
import com.tamboraagungmakmur.winwin.voip.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.voip.Utils.VolleyHttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tambora on 26/08/2016.
 */
public class MessageIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN), recent_token);
        editor.commit();

        SessionManager sessionManager = new SessionManager(this);
        if(sessionManager.checkLogin()){
            Token();
        }
    }


    private void Token() {

        final SessionManager sessionManager = new SessionManager(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", sessionManager.getIduser());
                params.put("token", token);

                AndLog.ShowLog("Datas", params.toString() + ";;" + AppConf.URL_LOGIN);
                return params;
            }
        };

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(this).addToRequestQueue(stringRequest);

    }
}
