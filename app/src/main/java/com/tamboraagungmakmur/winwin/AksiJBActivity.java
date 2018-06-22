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
import android.widget.EditText;
import android.widget.LinearLayout;
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
import butterknife.OnClick;

/**
 * Created by innan on 9/25/2017.
 */

public class AksiJBActivity extends FragmentActivity {

    private final static String TAG = "AKSI_JB";
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.txPesan)
    EditText txPesan;
    @Bind(R.id.btSimpan)
    Button btSimpan;
    @Bind(R.id.btBatal)
    Button btBatal;
    @Bind(R.id.lyWarn)
    LinearLayout lyWarn;
    @Bind(R.id.btYa)
    Button btYa;
    @Bind(R.id.btTidak)
    Button btTidak;
    @Bind(R.id.lyTerima)
    LinearLayout lyTerima;
    @Bind(R.id.btOK)
    Button btOK;
    @Bind(R.id.lySuccess)
    LinearLayout lySuccess;


    private RequestQueue requestQueue;

    private Context context;

    private String id, aksi;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aksi_jb);
        ButterKnife.bind(this);
        context = AksiJBActivity.this;
        requestQueue = Volley.newRequestQueue(context);
        id = getIntent().getStringExtra("id");
        aksi = getIntent().getStringExtra("aksi");
        progressDialog = new ProgressDialog(AksiJBActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        if(aksi.equals("terima")){
            lyTerima.setVisibility(View.VISIBLE);
            lyWarn.setVisibility(View.GONE);
        }else{
            lyTerima.setVisibility(View.GONE);
            lyWarn.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }


    private void reqJb() {

        progressDialog.show();

//        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_AKSI_JB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("store_task", response);

                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (!klienResponse.isError()) {

                        LocalBroadcastManager.getInstance(AksiJBActivity.this).sendBroadcast(new Intent("aksi_jb"));
//                        kembali();
                        lyWarn.setVisibility(View.GONE);
                        lyTerima.setVisibility(View.GONE);
                        lySuccess.setVisibility(View.VISIBLE);


                    } else {
                        Toast.makeText(context, "gagal", Toast.LENGTH_SHORT).show();
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
//
//
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(AksiJBActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("pengajuan_id", id);
                params.put("aksi", aksi);
                params.put("pesan", txPesan.getText().toString());

                AndLog.ShowLog("REQIJB", params.toString() + ";;" + AppConf.URL_REQ_JB);
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


    @OnClick({R.id.btSimpan, R.id.btBatal, R.id.btYa, R.id.btTidak, R.id.btOK})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btSimpan:
                if (txPesan.getText().toString().isEmpty()) {
                    GlobalToast.ShowToast(AksiJBActivity.this, "Masukkan pesan");
                } else {
                    reqJb();
                }
                break;
            case R.id.btBatal:
                finish();
                break;
            case R.id.btYa:
                reqJb();
                break;
            case R.id.btTidak:
                break;
            case R.id.btOK:
                kembali();
                break;
        }
    }
}
