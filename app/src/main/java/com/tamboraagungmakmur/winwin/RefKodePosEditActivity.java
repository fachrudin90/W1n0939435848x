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
import com.tamboraagungmakmur.winwin.Model.RefArea;
import com.tamboraagungmakmur.winwin.Model.RefKodePos;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 11/28/2017.
 */

public class RefKodePosEditActivity extends FragmentActivity {

    private static final Object TAG = "REF_KODEPOS_EDIT";
    @Bind(R.id.txNamaKode)
    TextInputEditText txNamaKode;
    @Bind(R.id.txNamaKel)
    TextInputEditText txNamaKel;
    @Bind(R.id.txNamaKec)
    TextInputEditText txNamaKec;
    @Bind(R.id.txNamaJenis)
    TextInputEditText txNamaJenis;
    @Bind(R.id.txNamaKab)
    TextInputEditText txNamaKab;
    @Bind(R.id.txNamaProv)
    TextInputEditText txNamaProv;
    @Bind(R.id.spinner1)
    Spinner spinner1;
    @Bind(R.id.save)
    Button save;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;


    private String id;

    private Context context;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_kodepos_edit);
        ButterKnife.bind(this);
        context = RefKodePosEditActivity.this;
        requestQueue = Volley.newRequestQueue(context);
        id = getIntent().getStringExtra("id");

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

        getDetail();
    }

    private void getDetail() {
        Log.d("klien_all", id);
        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_REF_KODEPOS_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    RefKodePos klienResponse = new Gson().fromJson(jsonObject1.getString("data"), RefKodePos.class);

                    if (klienResponse != null) {
                        txNamaKode.setText(klienResponse.getKodepos_kode());
                        txNamaKel.setText(klienResponse.getKodepos_kelurahan());
                        txNamaKec.setText(klienResponse.getKodepos_kecamatan());
                        txNamaJenis.setText(klienResponse.getKodepos_jenis());
                        txNamaKab.setText(klienResponse.getKodepos_kabupaten());
                        txNamaProv.setText(klienResponse.getKodepos_provinsi());

                        String rlc[] = getResources().getStringArray(R.array.status_bank);
                        int pos = Arrays.asList(rlc).indexOf(klienResponse.getStat_rlc());
                        spinner1.setSelection(pos);

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
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_REF_KODEPOS_UPDATE + "/" + id +"?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
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

                    Intent intent = new Intent("ref_kodepos");
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

                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("kodepos_kode", txNamaKode.getText().toString());
                params.put("kodepos_kelurahan", txNamaKel.getText().toString());
                params.put("kodepos_kecamatan", txNamaKec.getText().toString());
                params.put("kodepos_jenis", txNamaJenis.getText().toString());
                params.put("kodepos_kabupaten", txNamaKab.getText().toString());
                params.put("kodepos_provinsi", txNamaProv.getText().toString());
                params.put("kodepos_rlc", "" + (spinner1.getSelectedItemPosition() + 1));

                AndLog.ShowLog("rlc_postt", params.toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
