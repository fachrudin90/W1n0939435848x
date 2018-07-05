package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
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
import com.tamboraagungmakmur.winwin.Adapter.PengajuanAdapter;
import com.tamboraagungmakmur.winwin.Model.Pengajuan;
import com.tamboraagungmakmur.winwin.Model.PengajuanResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 11/14/2017.
 */

public class ListPencairanActivity extends FragmentActivity {

    private final static String TAG = "LIST_PENCAIRAN";
    private RequestQueue requestQueue;

    private ArrayList<Pengajuan> pengajuanArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PengajuanAdapter pengajuanAdapter;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpengajuan);
        requestQueue = Volley.newRequestQueue(this);
        initView();

        getPengajuan();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("pengajuan_terkait"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBackPressed();
        }
    };

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rvlistpeng);
        linearLayoutManager = new LinearLayoutManager(this);
        pengajuanAdapter = new PengajuanAdapter(pengajuanArrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pengajuanAdapter);

        search = (EditText) findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 4) {
                    requestQueue.cancelAll(TAG);
                    if (pengajuanArrayList != null) {
                        pengajuanArrayList.clear();
                    } else {
                        pengajuanArrayList = new ArrayList<>();
                    }
                    pengajuanAdapter.notifyDataSetChanged();
                    loading = false;
                    offset = 0;
                    getPengajuan1(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getPengajuan() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(ListPencairanActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_DEBITBCALIST + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
//                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PengajuanResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            pengajuanArrayList.add(klienResponse.getData()[i]);
                            pengajuanAdapter.notifyItemChanged(pengajuanArrayList.size() - 1);
                        }

                        loading = true;
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                visibleItemCount = linearLayoutManager.getChildCount();
                                totalItemCount = linearLayoutManager.getItemCount();
                                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                                if (loading) {
                                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                        loading = false;
                                        offset += 1;
                                        Log.d("offset", Integer.toString(offset));
                                        getPengajuan();
                                    }
                                }
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(ListPencairanActivity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(ListPencairanActivity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(ListPencairanActivity.this, LoginActivity.class);
                    ListPencairanActivity.this.startActivity(intent);
                    ListPencairanActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(ListPencairanActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ListPencairanActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(ListPencairanActivity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(ListPencairanActivity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(ListPencairanActivity.this, LoginActivity.class);
//                    ListPencairanActivity.this.startActivity(intent);
//                    ListPencairanActivity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(ListPencairanActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(ListPencairanActivity.this).addToRequestQueue(stringRequest);

    }

    private void getPengajuan1(final String cari) {
        Log.d("klien_all", "ok");

        String query = "";
        if (cari.compareTo("") != 0) {
            query = "/" + cari;
        }
        SessionManager sessionManager = new SessionManager(ListPencairanActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_FINDAKTIF + query + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
//                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PengajuanResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            pengajuanArrayList.add(klienResponse.getData()[i]);
                            pengajuanAdapter.notifyItemChanged(pengajuanArrayList.size() - 1);
                        }

//                        loading = true;
//                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                visibleItemCount = linearLayoutManager.getChildCount();
//                                totalItemCount = linearLayoutManager.getItemCount();
//                                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
//
//                                if (loading) {
//                                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                                        loading = false;
//                                        offset += 1;
//                                        Log.d("offset", Integer.toString(offset));
//                                        getPengajuan1(cari);
//                                    }
//                                }
//                            }
//                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(ListPencairanActivity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(ListPencairanActivity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(ListPencairanActivity.this, LoginActivity.class);
                    ListPencairanActivity.this.startActivity(intent);
                    ListPencairanActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(ListPencairanActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ListPencairanActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(ListPencairanActivity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(ListPencairanActivity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(ListPencairanActivity.this, LoginActivity.class);
//                    ListPencairanActivity.this.startActivity(intent);
//                    ListPencairanActivity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(ListPencairanActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(ListPencairanActivity.this).addToRequestQueue(stringRequest);

    }
    
}
