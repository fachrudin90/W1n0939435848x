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
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.InvestAdapter;
import com.tamboraagungmakmur.winwin.Model.Invest;
import com.tamboraagungmakmur.winwin.Model.InvestResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 12/8/2017.
 */

public class InvestasiInvest extends android.support.v4.app.Fragment {

    private static final Object TAG = "INVESTASI_INVEST";
    private RequestQueue requestQueue;
    private View view;
    private Context context;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private InvestAdapter adapter;
    private ArrayList<Invest> investasiInvestorArrayList = new ArrayList<>();

    private EditText txCari;
    private Button cari, cari1;
    private ImageButton btAdd;
    private EditText tgl1, tgl2;

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
            view = inflater.inflate(R.layout.fragment_investasi_invest, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();
        requestQueue = Volley.newRequestQueue(context);

        Intent intent = new Intent("title");
        intent.putExtra("message", "Investasi");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvList);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new InvestAdapter(investasiInvestorArrayList);
        recyclerView.setAdapter(adapter);

        initView();

        getKlien();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("invest"));

        return view;
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

    private void initView() {

        btAdd = (ImageButton) view.findViewById(R.id.btAdd);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestAddActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        refresh1 = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        refresh1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                getKlien();
            }
        });

        txCari = (EditText) view.findViewById(R.id.txPencarian);
        cari = (Button) view.findViewById(R.id.btCari1);
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

        tgl1 = (EditText) view.findViewById(R.id.txTgl1);
        tgl2 = (EditText) view.findViewById(R.id.txTgl2);
        cari1 = (Button) view.findViewById(R.id.btCari);
        tgl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
                                tgl1.setText(tgl);
                            }
                        });
                cdp.show(getChildFragmentManager(), null);
            }
        });

        tgl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
                                tgl2.setText(tgl);
                            }
                        });
                cdp.show(getChildFragmentManager(), null);
            }
        });

        cari1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
                if (tgl1.getText().toString().compareTo("") != 0 && tgl2.getText().toString().compareTo("") != 0) {
                    getKlien(tgl1.getText().toString(), tgl2.getText().toString());
                } else {
                    getKlien();
                }
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

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVEST + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                refresh1.setRefreshing(false);
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

    private void getKlien(final String tg11, final String tgl2) {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVEST + "/" + LIMIT + "/" + offset + "/" + tg11 + "/" + tgl2 + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                refresh1.setRefreshing(false);
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
                        getKlien(tg11, tgl2);
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

    private void cari(final String cari) {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVEST_FIND + "/" + cari + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                refresh1.setRefreshing(false);
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
