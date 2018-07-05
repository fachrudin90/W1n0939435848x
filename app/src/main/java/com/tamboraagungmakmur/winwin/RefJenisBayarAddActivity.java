package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.tamboraagungmakmur.winwin.Model.RefJenisBayar;
import com.tamboraagungmakmur.winwin.Model.RefTipeTransaksi;
import com.tamboraagungmakmur.winwin.Model.RefTipeTransaksiResponse;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 11/28/2017.
 */

public class RefJenisBayarAddActivity extends FragmentActivity {

    private static final Object TAG = "REF_JENIS_BAYAR_ADD";
    @Bind(R.id.save)
    Button save;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;
    @Bind(R.id.txNamaLabel)
    TextInputEditText txNamaLabel;
    @Bind(R.id.txNamaKode)
    TextInputEditText txNamaKode;
    @Bind(R.id.txKeterangan)
    TextInputEditText txKeterangan;
    @Bind(R.id.spinnerMan)
    Spinner spinnerMan;
    @Bind(R.id.spinner1)
    Spinner spinner1;

    private ArrayList<RefTipeTransaksi> dataArrayList;

    private Context context;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_jenis_bayar_edit);
        ButterKnife.bind(this);
        context = RefJenisBayarAddActivity.this;
        requestQueue = Volley.newRequestQueue(context);
        dataArrayList = new ArrayList<>();
//        id = getIntent().getStringExtra("id");

        initView();
    }

    private void initView() {


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_bank, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                edit();
            }
        });

        getTrxType();
    }

    private void getTrxType() {
         Log.d("klien_all", "ok");

            SessionManager sessionManager = new SessionManager(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_REF_TRXTIPE + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("klien_all", response);
                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        RefTipeTransaksiResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), RefTipeTransaksiResponse.class);

                        if (klienResponse.getData().length != 0) {

                            ArrayList<String> tipe = new ArrayList<>();
                            for (int i = 0; i < klienResponse.getData().length; i++) {
//
                                dataArrayList.add(klienResponse.getData()[i]);
                                tipe.add(klienResponse.getData()[i].getTrxtype_label());
                            }

                            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(RefJenisBayarAddActivity.this, android.R.layout.simple_spinner_item, tipe);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerMan.setAdapter(adapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        GlobalToast.ShowToast(context, getString(R.string.session_expired));

                        SessionManager sessionManager = new SessionManager(context);
                        sessionManager.logoutUser(); sessionManager.setPage(0);

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
                    return params;
                }
            };

            stringRequest.setTag(TAG);
            requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);


    }

    private void edit() {

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_REF_JENIS_BAYAR_STORE + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                save.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                    Intent intent = new Intent("ref_jenis_bayar");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    finish();

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

                save.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

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

                int pos = spinnerMan.getSelectedItemPosition();
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("jenisbayar_label", txNamaLabel.getText().toString());
                params.put("jenisbayar_code", txNamaKode.getText().toString());
                params.put("jenisbayar_penjelasan", txKeterangan.getText().toString());
                params.put("jenisbayar_id_trx", dataArrayList.get(pos).getId());
                params.put("jenisbayar_rlc", "" + (spinner1.getSelectedItemPosition() + 1));


                AndLog.ShowLog("jab_postt", params.toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
