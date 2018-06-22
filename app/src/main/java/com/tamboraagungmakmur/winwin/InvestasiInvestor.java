package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tamboraagungmakmur.winwin.Adapter.InvestorAdapter;
import com.tamboraagungmakmur.winwin.Model.Investor;
import com.tamboraagungmakmur.winwin.Model.InvestorResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 12/7/2017.
 */

public class InvestasiInvestor extends android.support.v4.app.Fragment {

    private static final Object TAG = "INVESTASI_INVESTOR";
    private RequestQueue requestQueue;
    private View view;
    private Context context;
    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private InvestorAdapter adapter;
    private ArrayList<Investor> investasiInvestorArrayList = new ArrayList<>();

    private ImageButton btAdd;
    private EditText txCari;
    private Button cari;

    private SwipeRefreshLayout refresh1;
    private static final int LIMIT = 20;
    private int offset = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_investasi_investor, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();
        requestQueue = Volley.newRequestQueue(context);

        Intent intent = new Intent("title");
        intent.putExtra("message", "Investor");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvList);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new InvestorAdapter(investasiInvestorArrayList);
        recyclerView.setAdapter(adapter);

        initView();

        getKlien();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("invest"));

        return view;
    }

    private void initView() {
        btAdd = (ImageButton) view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestorAddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        refresh1 = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        refresh1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                getKlien();
            }
        });

        txCari = (EditText) view.findViewById(R.id.txPencarian);
        cari = (Button) view.findViewById(R.id.btCari);
        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txCari.getText().toString().compareTo("") == 0) {
                    refresh();
                    getKlien();
                } else {
                    refresh();
                    cari(txCari.getText().toString().replace(" ", "+"));
                }
            }
        });


    }

    private void cari(final String cari) {

        Log.d("investor_cari", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVESTOR_FIND + "/" + cari + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                refresh1.setRefreshing(false);
                Log.d("investor_cari", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    InvestorResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), InvestorResponse.class);

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
                    getActivity().finish();
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
//                    getActivity().finish();
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();
            getKlien();
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVESTOR + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                refresh1.setRefreshing(false);
                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    InvestorResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), InvestorResponse.class);

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
                    getActivity().finish();
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
//                    getActivity().finish();
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
