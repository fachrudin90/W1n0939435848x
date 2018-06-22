package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/4/2017.
 */

public class KlienAktiva1Activity extends FragmentActivity {

    private final static String TAG = "KLIEN_AKTIVA1";
    private RequestQueue requestQueue;

    private Button ok, cancel;
    private ProgressBar progressBar;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klienaktiva1);
        requestQueue = Volley.newRequestQueue(this);
        initView();
    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {
        id = getIntent().getStringExtra("id");

        ok = (Button) findViewById(R.id.ok);
        cancel = (Button) findViewById(R.id.cancel);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ok.setVisibility(View.INVISIBLE);
                activaKlien();
            }
        });
    }

    private void activaKlien() {
        Log.d("klien_all", "ok");

//        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_KLIEN_ACTIVATE1 + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                progressBar.setVisibility(View.INVISIBLE);
                ok.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    if (!klienResponse.isError()) {

                        Toast.makeText(KlienAktiva1Activity.this, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        Intent intent = new Intent("klien");
                        LocalBroadcastManager.getInstance(KlienAktiva1Activity.this).sendBroadcast(intent);

                    } else {
                        Toast.makeText(KlienAktiva1Activity.this, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(KlienAktiva1Activity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(KlienAktiva1Activity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(KlienAktiva1Activity.this, LoginActivity.class);
                    KlienAktiva1Activity.this.startActivity(intent);
                    KlienAktiva1Activity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(KlienAktiva1Activity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(KlienAktiva1Activity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(KlienAktiva1Activity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(KlienAktiva1Activity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(KlienAktiva1Activity.this, LoginActivity.class);
//                    KlienAktiva1Activity.this.startActivity(intent);
//                    KlienAktiva1Activity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(KlienAktiva1Activity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(KlienAktiva1Activity.this).addToRequestQueue(stringRequest);

    }

}