package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.tamboraagungmakmur.winwin.Adapter.RefBankAdapter;
import com.tamboraagungmakmur.winwin.Adapter.RefPerusahaanAdapter;
import com.tamboraagungmakmur.winwin.Model.RefBank;
import com.tamboraagungmakmur.winwin.Model.RefBankResponse;
import com.tamboraagungmakmur.winwin.Model.RefPerusahaan;
import com.tamboraagungmakmur.winwin.Model.RefPerusahaanResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 11/27/2017.
 */

public class RefPerusahaanActivity extends AppCompatActivity {

    private static final String TAG = "REF_PERUSAHAAN";

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RefPerusahaanAdapter adapter;
    private ArrayList<RefPerusahaan> dataArrayList = new ArrayList<>();

    private ImageButton btAdd, btBack;

    private RequestQueue requestQueue;
    private Context context;

    private int limit = 50;
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_perusahaan);
        context = RefPerusahaanActivity.this;
        requestQueue = Volley.newRequestQueue(context);
        offset = 0;
        initView();
        getTemplate();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("ref_perusahaan"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            requestQueue.cancelAll(TAG);

            offset = 0;
            if (dataArrayList != null) {
                dataArrayList.clear();
            } else {
                dataArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getTemplate();
                }
            },1000);

        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        requestQueue.cancelAll(TAG);
        super.onDestroy();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RefPerusahaanAdapter(dataArrayList);
        recyclerView.setAdapter(adapter);

        btBack = (ImageButton) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btAdd = (ImageButton) findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefPerusahaanAddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void getTemplate() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_REF_PERUSAHAAN +"/"+limit+"/"+offset+ "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    RefPerusahaanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), RefPerusahaanResponse.class);

                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//
                            dataArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(dataArrayList.size() - 1);
                        }

                        if(klienResponse.getData().length > 0){
                            offset++;
                            getTemplate();
                        }

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

}
