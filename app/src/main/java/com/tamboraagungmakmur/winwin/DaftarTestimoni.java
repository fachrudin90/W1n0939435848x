package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.tamboraagungmakmur.winwin.Adapter.TestimoniAdapter;
import com.tamboraagungmakmur.winwin.Model.ArtikelModel;
import com.tamboraagungmakmur.winwin.Model.Testimoni;
import com.tamboraagungmakmur.winwin.Model.TestimoniResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by innan on 9/13/2017.
 */

public class DaftarTestimoni extends AppCompatActivity {

    @Bind(R.id.btBack)
    ImageButton btBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bar)
    AppBarLayout bar;
    @Bind(R.id.rvList)
    RecyclerView rvList;

    private final static String TAG = "DAFTAR_TSTIMONI";
    private RequestQueue requestQueue;

    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ArtikelModel> dataSet = new ArrayList<>();

    private ArrayList<Testimoni> klienArrayList = new ArrayList<>();
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_testimoni);
        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(this);

        layoutManager = new LinearLayoutManager(DaftarTestimoni.this);
        rvList.setLayoutManager(layoutManager);
        adapter = new TestimoniAdapter(klienArrayList);
        rvList.setAdapter(adapter);

        getKlien();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("testimoni"));
    }

    @Override
    public void onStop() {
        VolleyHttp.getInstance(DaftarTestimoni.this).getRequestQueue().cancelAll(AppConf.httpTag);
        super.onStop();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (klienArrayList != null) {
                klienArrayList.clear();
            } else {
                klienArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            getKlien();
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        requestQueue.cancelAll(TAG);
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    private void GetData() {

        dataSet.clear();
        for (int i = 0; i < 8; i++) {

            ArtikelModel artikelModel = new ArtikelModel("24-02-2017", "Jenis-Jenis Pendidikan di Indonesia", "Zulkarnain", "Indonesia", "Aktif");
            dataSet.add(artikelModel);
            ArtikelModel artikelModel2 = new ArtikelModel("24-02-2017", "Permasalahan Pendidikan di Indonesia", "Adihamsa", "Pendidikan", "Aktif");
            dataSet.add(artikelModel2);
        }

        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
        }
    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(DaftarTestimoni.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_TESTIMONI_ALL + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TestimoniResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TestimoniResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            klienArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(klienArrayList.size() - 1);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(DaftarTestimoni.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(DaftarTestimoni.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(DaftarTestimoni.this, LoginActivity.class);
                    DaftarTestimoni.this.startActivity(intent);
                    DaftarTestimoni.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(DaftarTestimoni.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(DaftarTestimoni.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(DaftarTestimoni.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(DaftarTestimoni.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(DaftarTestimoni.this, LoginActivity.class);
//                    DaftarTestimoni.this.startActivity(intent);
//                    DaftarTestimoni.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(DaftarTestimoni.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(DaftarTestimoni.this).addToRequestQueue(stringRequest);

    }

}
