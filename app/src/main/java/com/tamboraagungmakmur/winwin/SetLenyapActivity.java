package com.tamboraagungmakmur.winwin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import butterknife.OnClick;

/**
 * Created by innan on 9/25/2017.
 */

public class SetLenyapActivity extends FragmentActivity {

    private final static String TAG = "SET_LENYAP";
    @Bind(R.id.btYa)
    Button btYa;
    @Bind(R.id.btTidak)
    Button btTidak;
    @Bind(R.id.lyWarn)
    LinearLayout lyWarn;
    @Bind(R.id.btOK)
    Button btOK;
    @Bind(R.id.lySuccess)
    LinearLayout lySuccess;


    private RequestQueue requestQueue;

    private Context context;

    private String idpeng;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_lenyap);
        ButterKnife.bind(this);
        context = SetLenyapActivity.this;
        requestQueue = Volley.newRequestQueue(context);
        idpeng = getIntent().getStringExtra("nopeng");
        progressDialog = new ProgressDialog(SetLenyapActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }


    private void reqLenyap() {

        progressDialog.show();

//        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_SET_LENYAP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("store_task", response);

                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (!klienResponse.isError()) {

                        LocalBroadcastManager.getInstance(SetLenyapActivity.this).sendBroadcast(new Intent("set_lenyap"));
//                        kembali();
                        lyWarn.setVisibility(View.GONE);
                        lySuccess.setVisibility(View.VISIBLE);


                    } else {
                        Toast.makeText(context, "gagal memindahkan ke Lenyap", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

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
//                    startActivity(intent);
//                    finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(SetLenyapActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("no_peng", idpeng);

                AndLog.ShowLog("SETLENYAP", params.toString() + ";;" + AppConf.URL_SET_LENYAP);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void kembali() {
        onBackPressed();
    }


    @OnClick({R.id.btYa, R.id.btTidak, R.id.btOK})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btYa:
                reqLenyap();
                break;
            case R.id.btTidak:
                finish();
                break;
            case R.id.btOK:
                kembali();
                break;
        }
    }
}
