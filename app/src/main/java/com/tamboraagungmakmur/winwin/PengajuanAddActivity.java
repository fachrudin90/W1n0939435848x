package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/30/2017.
 */

public class PengajuanAddActivity extends FragmentActivity {

    private final static String TAG = "PENGAJUAN_ADD";
    private RequestQueue requestQueue;

    private TextInputEditText klien, amount, duration, tujuan;
    private Button save;
    private ProgressBar progressBar;

    private Context context;
    private String idPengajuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpeng);
        context = PengajuanAddActivity.this;
        requestQueue = Volley.newRequestQueue(context);

        initView();

    }

    private void initView() {
        klien = (TextInputEditText) findViewById(R.id.klien);
        amount = (TextInputEditText) findViewById(R.id.amount);
        duration = (TextInputEditText) findViewById(R.id.duration);
        tujuan = (TextInputEditText) findViewById(R.id.tujuan);
        save = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        klien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListKlienActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                addPeng();
            }
        });


        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("klien_terkait"));

    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            idPengajuan = intent.getStringExtra("id");
            klien.setText(intent.getStringExtra("nomor_pelanggan") + " " + intent.getStringExtra("nama_lengkap") + " [" + intent.getStringExtra("email") + "]");
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void addPeng() {
        Log.d("klien_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_PENGAJUAN_STORE + "/" + idPengajuan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_find", response);
                progressBar.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse response1 = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (!response1.isError()) {
                        Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    } else {
                        LogoutResponse response2 = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);
                        Toast.makeText(context, response2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Intent intent1 = new Intent("tahap");
                    LocalBroadcastManager.getInstance(PengajuanAddActivity.this).sendBroadcast(intent1);
                    onBackPressed();


                } catch (JSONException e) {

                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        LogoutResponse response1 = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                        Toast.makeText(context, response1.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e1) {
                        e.printStackTrace();
                        GlobalToast.ShowToast(context, getString(R.string.session_expired));

                        SessionManager sessionManager = new SessionManager(context);
                        sessionManager.logoutUser(); sessionManager.setPage(0);

                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        finish();
                    }




                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent);
//                    finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("amount", amount.getText().toString());
                params.put("duration", duration.getText().toString());
                params.put("tujuan", tujuan.getText().toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
