package com.tamboraagungmakmur.winwin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import com.tamboraagungmakmur.winwin.Model.PassResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/7/2017.
 */

public class GantiPassActivity extends Activity {

    private final static String TAG = "GANTI_PASS";
    private RequestQueue requestQueue;

    private TextInputEditText pass, pass1, pass2;
    private Button save;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantipass);
        requestQueue = Volley.newRequestQueue(this);
        initView();

    }

    @Override
    public void onStop() {
//        VolleyHttp.getInstance(GantiPassActivity.this).getRequestQueue().cancelAll(AppConf.httpTag);
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {
        pass = (TextInputEditText) findViewById(R.id.pass);
        pass1 = (TextInputEditText) findViewById(R.id.pass1);
        pass2 = (TextInputEditText) findViewById(R.id.pass2);
        save = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                savePass(pass.getText().toString(), pass1.getText().toString(), pass2.getText().toString());
            }
        });

    }

    private void savePass(final String pass, final String pass1, final String pass2) {
        Log.d("klien_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_USER_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PassResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PassResponse.class);

                    Toast.makeText(GantiPassActivity.this, klienResponse.getData(), Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(GantiPassActivity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(GantiPassActivity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(GantiPassActivity.this, LoginActivity.class);
                    GantiPassActivity.this.startActivity(intent);
                    GantiPassActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(GantiPassActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(GantiPassActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(GantiPassActivity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(GantiPassActivity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(GantiPassActivity.this, LoginActivity.class);
//                    GantiPassActivity.this.startActivity(intent);
//                    GantiPassActivity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(GantiPassActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("old_password", pass);
                params.put("new_password", pass1);
                params.put("confirm_new_password", pass2);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(GantiPassActivity.this).addToRequestQueue(stringRequest);

    }

}
