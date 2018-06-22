package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import com.tamboraagungmakmur.winwin.Adapter.InvestListAdapter;
import com.tamboraagungmakmur.winwin.Model.Invest;
import com.tamboraagungmakmur.winwin.Model.InvestResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 12/15/2017.
 */

public class InvestListActivity extends FragmentActivity {

    private static final Object TAG = "INVEST_LIST";
    private RequestQueue requestQueue;
    private Context context;
    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private InvestListAdapter adapter;
    private ArrayList<Invest> investasiInvestorArrayList = new ArrayList<>();

    private ImageButton btAdd;
    private EditText txCari;
    private Button cari;

    private SwipeRefreshLayout refresh1;
    private static final int LIMIT = 20;
    private int offset = 0;

    private EditText search;

    @Override
    protected void onCreate (Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_investor_list);
        context = InvestListActivity.this;
        requestQueue = Volley.newRequestQueue(context);

        recyclerView = (RecyclerView) findViewById(R.id.rvlist);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new InvestListAdapter(investasiInvestorArrayList);
        recyclerView.setAdapter(adapter);

        initView();

        getKlien();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("investor_terkait"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            onBackPressed();
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    private void initView() {

        search = (EditText) findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 4) {
                    requestQueue.cancelAll(TAG);
                    if (investasiInvestorArrayList != null) {
                        investasiInvestorArrayList.clear();
                    } else {
                        investasiInvestorArrayList = new ArrayList<>();
                    }
                    adapter.notifyDataSetChanged();
                    offset = 0;
                    cari(s.toString().replace(" ", "+"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void refresh() {
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
        if (investasiInvestorArrayList != null) {
            investasiInvestorArrayList.clear();
        } else {
            investasiInvestorArrayList = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();
        offset = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    private void cari(final String cari) {

        Log.d("investor_cari", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVEST_FIND2 + "/" + cari + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("investor_cari", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    InvestResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), InvestResponse.class);

                    if (klienResponse.getData().length > 0) {
                        for (int i=0; i<klienResponse.getData().length; i++) {
                            investasiInvestorArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(investasiInvestorArrayList.size()-1);
                        }
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

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);

    }


    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVEST2 + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    InvestResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), InvestResponse.class);

                    if (klienResponse.getData().length > 0) {
                        for (int i=0; i<klienResponse.getData().length; i++) {
                            investasiInvestorArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(investasiInvestorArrayList.size()-1);
                        }
                        offset += 1;
                        getKlien();
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

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
