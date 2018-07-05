package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 11/28/2017.
 */

public class RefPerusahaanAddActivity extends FragmentActivity {

    private static final Object TAG = "REF_PERUSAHAAN_ADD";
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.txNama)
    TextInputEditText txNama;
    @Bind(R.id.input1)
    TextInputLayout input1;
    @Bind(R.id.txAlamat)
    TextInputEditText txAlamat;
    @Bind(R.id.input2)
    TextInputLayout input2;
    @Bind(R.id.txKota)
    TextInputEditText txKota;
    @Bind(R.id.input3)
    TextInputLayout input3;
    @Bind(R.id.txProvinsi)
    TextInputEditText txProvinsi;
    @Bind(R.id.input4)
    TextInputLayout input4;
    @Bind(R.id.txNoTelp)
    TextInputEditText txNoTelp;
    @Bind(R.id.input5)
    TextInputLayout input5;
    @Bind(R.id.txKomoditi)
    TextInputEditText txKomoditi;
    @Bind(R.id.input6)
    TextInputLayout input6;
    @Bind(R.id.txJenisIndustri)
    TextInputEditText txJenisIndustri;
    @Bind(R.id.input7)
    TextInputLayout input7;
    @Bind(R.id.spinner1)
    Spinner spinner1;
    @Bind(R.id.input8)
    TextInputLayout input8;
    @Bind(R.id.save)
    Button save;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    private Context context;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_perusahaan_edit);
        ButterKnife.bind(this);
        context = RefPerusahaanAddActivity.this;
        requestQueue = Volley.newRequestQueue(context);
//        id = getIntent().getStringExtra("id");

        initView();
    }

    private void initView() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_perusahaan, android.R.layout.simple_spinner_item);
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
    }

    private void edit() {

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_REF_PERUSAHAAN_STORE + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
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

                    Intent intent = new Intent("ref_perusahaan");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

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
                params.put("nama_perusahaan", txNama.getText().toString());
                params.put("alamat", txAlamat.getText().toString());
                params.put("kota", txKota.getText().toString());
                params.put("provinsi", txProvinsi.getText().toString());
                params.put("telepon", txNoTelp.getText().toString());
                params.put("jenis_komoditi", txKomoditi.getText().toString());
                params.put("jenis_industri", txJenisIndustri.getText().toString());
                params.put("rlc", spinner1.getSelectedItemPosition() == 0 ? "5" : "6");

                AndLog.ShowLog("perusahaan_postt", params.toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
