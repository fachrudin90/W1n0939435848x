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
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 11/27/2017.
 */

public class RefBankAddActivity extends FragmentActivity {

    private static final Object TAG = "REF_BANK_ADD";
    private TextInputEditText prov, kab;
    private Spinner spinner1;
    private Button save;
    private ProgressBar progressBar;

    private String id;
    private Context context;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_bank_edit);
        context = RefBankAddActivity.this;
        requestQueue = Volley.newRequestQueue(context);
//        id = getIntent().getStringExtra("id");

        initView();
    }

    private void initView() {
        prov = (TextInputEditText) findViewById(R.id.prov);
        kab = (TextInputEditText) findViewById(R.id.kab);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        save = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
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
    }

    private void edit() {
        Log.d("klien_all", id);

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_REF_BANK_KLIEN_STORE + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
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

                    Intent intent = new Intent("ref_bank");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    finish();

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
                params.put("bank_label", prov.getText().toString());
                params.put("bank_code", kab.getText().toString());
                params.put("bank_rlc", "" + (spinner1.getSelectedItemPosition()+1));
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
