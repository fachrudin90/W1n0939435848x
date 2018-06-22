package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/12/2017.
 */

public class PindahTahap5Activity extends FragmentActivity {

    private final static String TAG = "PINDAH_TAHAP5";
    private RequestQueue requestQueue;

    private Button ok, cancel;
    private TextView txWarning;
    private String id, idKlien, idPenc, note, date, nopeng, nama, jumlah, nama_bank, no_rek, nama_rekening;
    private ProgressBar progressBar;

    private Context context;
    private String jumlahTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tahap5);
        context = PindahTahap5Activity.this;
        requestQueue = Volley.newRequestQueue(context);

        jumlahTransfer = "0";

        id = getIntent().getStringExtra("id");
        idKlien = getIntent().getStringExtra("id_klien");
        idPenc = getIntent().getStringExtra("id_penc");
        note = getIntent().getStringExtra("note");
        date = getIntent().getStringExtra("date");
        nopeng = getIntent().getStringExtra("nopeng");
        nama = getIntent().getStringExtra("nama");

        jumlahTransfer = getIntent().getStringExtra("jumlah");
        nama_bank = getIntent().getStringExtra("nama_bank");
        no_rek = getIntent().getStringExtra("no_rek");
        nama_rekening = getIntent().getStringExtra("nama_rekening");

        initView();

        if (nama_bank.toLowerCase().contains("bca") || nama_bank.toLowerCase().contains("central")) {
            txWarning.setVisibility(View.GONE);
        } else {
            txWarning.setVisibility(View.VISIBLE);
        }

        AndLog.ShowLog("HASIL", jumlahTransfer + ";" + nama_bank + ";" + no_rek + ";" + nama_rekening);


    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {
        txWarning = (TextView) findViewById(R.id.txWarning);
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


                if (nama_bank.toLowerCase().contains("bca") || nama_bank.toLowerCase().contains("central")) {
                    sudahTransfer();
                } else {
                    getPengajuanDetail1();
                    getPengajuanDetail();
                }
            }
        });
    }

    private void getPengajuanDetail() {
        Log.d("tahap_5", id);

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_PENCAIRAN_CAIR + "/" + idPenc + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                ok.setVisibility(View.VISIBLE);
                Log.d("tahap_2", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse logoutResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

//                    Toast.makeText(context, logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Pengajuan nomor " + nopeng + " (" + nama + ") Mendapatkan Pencairan!", Toast.LENGTH_LONG).show();

                    if (!logoutResponse.isError()) {
                        Intent intent = new Intent(PindahTahap5Activity.this, PindahTahap5aActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Intent intent1 = new Intent("tahap5");
                        intent1.putExtra("id", getIntent().getStringExtra("id"));
                        intent1.putExtra("id_klien", getIntent().getStringExtra("id_klien"));
                        LocalBroadcastManager.getInstance(PindahTahap5Activity.this).sendBroadcast(intent1);

                        Intent intent2 = new Intent("tahap");
                        LocalBroadcastManager.getInstance(PindahTahap5Activity.this).sendBroadcast(intent2);

                        onBackPressed();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    finish();
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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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
                params.put("note", note);
                params.put("date", date);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void getPengajuanDetail1() {
        Log.d("tahap_2", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_PENGAJUAN_TAHAP + "/" + id + "/14" + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                progressBar.setVisibility(View.INVISIBLE);
//                ok.setVisibility(View.VISIBLE);
                Log.d("tahap_2", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse logoutResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

//                    Toast.makeText(context, logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if (!logoutResponse.isError()) {
//                        Intent intent = new Intent("tahap3");
//                        intent.putExtra("id", getIntent().getStringExtra("id"));
//                        intent.putExtra("id_klien", getIntent().getStringExtra("id_klien"));
//                        LocalBroadcastManager.getInstance(PindahTahap3Activity.this).sendBroadcast(intent);
//                        onBackPressed();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    finish();
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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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
                params.put("note", note);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }


    private void sudahTransfer() {
        Log.d("transfer_data", "ok");

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_GET_API_BCA_TRF, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("transfer_data", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);

                    if (jsonObject1.has("Status")) {


                        getPengajuanDetail1();
                        getPengajuanDetail();


                    } else if (jsonObject1.has("ErrorMessage")) {

                        JSONObject a = new JSONObject(jsonObject1.getString("ErrorMessage"));
                        Toast.makeText(context, a.getString("Indonesian"), Toast.LENGTH_SHORT).show();
                        ok.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
                    ok.setVisibility(View.VISIBLE);
                }

                progressBar.setVisibility(View.INVISIBLE);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ok.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "no connection", Toast.LENGTH_SHORT).show();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("user", "apibca");
                params.put("pass", "tr4nsf3rbc4.");
                params.put("jumlah", jumlahTransfer);
                params.put("nama_bank", nama_bank);
                params.put("no_rek", no_rek);
                params.put("nama_rekening", nama_rekening);
                params.put("pengajuan_id", id);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
