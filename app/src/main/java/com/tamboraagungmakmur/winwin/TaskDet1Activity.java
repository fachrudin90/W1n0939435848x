package com.tamboraagungmakmur.winwin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
 * Created by innan on 8/28/2017.
 */

public class TaskDet1Activity extends Activity {

    private final static String TAG = "TASK_DET1";
    private RequestQueue requestQueue;

    private String id;
    private TextInputEditText note;
    private Button save, cancel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdet1);
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

        note = (TextInputEditText) findViewById(R.id.note);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                finish1();
            }
        });

    }

    private void finish1() {
        Log.d("pengajuan_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_TASK_FINISH + "/" + id , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan_all", response);
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(response);
                    LogoutResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    Toast.makeText(TaskDet1Activity.this, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();

                    Intent intent = new Intent("task_update");
                    LocalBroadcastManager.getInstance(TaskDet1Activity.this).sendBroadcast(intent);

                } catch (JSONException e) {
                    GlobalToast.ShowToast(TaskDet1Activity.this, getString(R.string.session_expired));
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(TaskDet1Activity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(TaskDet1Activity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(TaskDet1Activity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(TaskDet1Activity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(TaskDet1Activity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(TaskDet1Activity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(TaskDet1Activity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(TaskDet1Activity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("note", note.getText().toString());
                return params;
            }

        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(TaskDet1Activity.this).addToRequestQueue(stringRequest);

    }

}
