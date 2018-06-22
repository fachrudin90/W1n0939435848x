package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/30/2017.
 */

public class DendaDeleteActivity extends FragmentActivity {

    private Context context;

    private String id;

    private Button ok, cancel;
    private ProgressBar progressBar;
    private TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengdel);
        context = DendaDeleteActivity.this;
        id = getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        ok = (Button) findViewById(R.id.ok);
        cancel = (Button) findViewById(R.id.cancel);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        text1 = (TextView) findViewById(R.id.text1);

        text1.setText("Hapus Denda?");

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
                delete();
            }
        });
    }

    @Override
    public void onStop() {
        VolleyHttp.getInstance(DendaDeleteActivity.this).getRequestQueue().cancelAll(AppConf.httpTag);
        super.onStop();
    }

    private void delete() {

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, AppConf.URL_DENDA_DELETE + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                ok.setVisibility(View.VISIBLE);
                Log.d("tahap_2", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse logoutResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    Toast.makeText(context, logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if (!logoutResponse.isError()) {
//                        Intent intent = new Intent("links");
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        onBackPressed();
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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
