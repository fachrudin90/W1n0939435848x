package com.tamboraagungmakmur.winwin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by innan on 8/30/2017.
 */

public class TransferReferralActivity extends Activity {

    private final static String TAG = "TRANSFER_REFERRAL";
    private RequestQueue requestQueue;

    private Button ok, cancel;
    private ProgressBar progressBar;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferreferral);
        initView();
    }

    @Override
    public void onStop() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_REFERRAL_TRANSFER + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("referal_all", response);
                progressBar.setVisibility(View.INVISIBLE);
                ok.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    if (!klienResponse.isError()) {

                        Toast.makeText(TransferReferralActivity.this, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        Intent intent = new Intent("referral");
                        LocalBroadcastManager.getInstance(TransferReferralActivity.this).sendBroadcast(intent);

                    } else {
                        Toast.makeText(TransferReferralActivity.this, "gagal set sudah transfer", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(TransferReferralActivity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(TransferReferralActivity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(TransferReferralActivity.this, LoginActivity.class);
                    TransferReferralActivity.this.startActivity(intent);
                    TransferReferralActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(TransferReferralActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(TransferReferralActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(TransferReferralActivity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(TransferReferralActivity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(TransferReferralActivity.this, LoginActivity.class);
//                    TransferReferralActivity.this.startActivity(intent);
//                    TransferReferralActivity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(TransferReferralActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(TransferReferralActivity.this).addToRequestQueue(stringRequest);

    }

}

