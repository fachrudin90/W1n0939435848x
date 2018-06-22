package com.tamboraagungmakmur.winwin;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.ArchivePengajuanAdapter;
import com.tamboraagungmakmur.winwin.Model.ArchivePengajuanModel;
import com.tamboraagungmakmur.winwin.Model.Pengajuan;
import com.tamboraagungmakmur.winwin.Model.PengajuanResponse;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArchivePengajuan extends Fragment {


    @Bind(R.id.swActive)
    Switch swActive;
    @Bind(R.id.txTgl1)
    EditText txTgl1;
    @Bind(R.id.txTgl2)
    EditText txTgl2;
    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.btCari1)
    Button btCari1;
    @Bind(R.id.rvList)
    RecyclerView rvList;
    @Bind(R.id.pbLoading)
    ProgressBar pbLoading;
    @Bind(R.id.pbLoadMore)
    ProgressBar pbLoadMore;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;
    @Bind(R.id.refresh1)
    SwipeRefreshLayout swipeRefreshLayout;

    private final static String TAG = "ARCHIVE_PENGAJUAN";
    
    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ArchivePengajuanModel> dataSet = new ArrayList<>();


    private ArrayList<Pengajuan> klienArrayList = new ArrayList<>();


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    private View view;

//    private SwipeRefreshLayout swipeRefreshLayout;

    private SessionManager sessionManager;
    private String sessionId;
    private boolean refresh, endpage;
    private int currentPage;

    private FloatingActionButton up;

    private String id;

    public ArchivePengajuan() {
        // Required empty public constructor
    }


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
            view = inflater.inflate(R.layout.fragment_archive_pengajuan, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        id = getArguments().getString("id");
        Intent intent = new Intent("title");
        intent.putExtra("message", "Archive Pengajuan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        up = (FloatingActionButton) view.findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvList.scrollToPosition(0);
            }
        });
        mActivity = getActivity();
        sessionManager = new SessionManager(mActivity);
        refresh = false;
        currentPage = 0;
        endpage = false;

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new ArchivePengajuanAdapter(mActivity, klienArrayList);
        rvList.setAdapter(adapter);


        sessionManager = new SessionManager(mActivity);
        sessionId = sessionManager.getSessionId();

//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                if (klienArrayList != null) {
                    klienArrayList.clear();
                } else {
                    klienArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getPengajuan(sessionManager.getSessionId());
            }
        });

        getPengajuan(sessionManager.getSessionId());

//        GetData();
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Refresh items
//                currentPage = 0;
//                refresh = true;
//                endpage = false;
//
//                GetData();
//
//            }
//        });

        return view;
    }

    @Override
    public void onStop() {
        VolleyHttp.getInstance(mActivity).getRequestQueue().cancelAll(TAG);
        super.onStop();
    }

//    private void GetData() {
//
//        dataSet.clear();
//        for (int i = 0; i < 15; i++) {
//
//            ArchivePengajuanModel pengajuanModel = new ArchivePengajuanModel("03-01-2017", "5SDF-54FS-85FF-SDFS", "Budi Santoso", "10.000.000", "28 Hari","2.000.000\nSisa: 1.250.000","15-07-2017","Disetujui sesuai pengajuan");
//            dataSet.add(pengajuanModel);
//        }
//
//        adapter.notifyDataSetChanged();
//    }

    private void GetData() {

        ToggleLoading(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_PENGAJUAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                AndLog.ShowLog("PENGJ", response);

                try {

                    dataSet.clear();


                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jos = ja.getJSONObject(i);

                        String pengajuan_id = jos.getString("pengajuan_id");
                        String pengajuan_tanggal = jos.getString("pengajuan_tanggal");
                        String pengajuan_nomor_pengajuan = jos.getString("pengajuan_nomor_pengajuan");
                        String cli_nama_lengkap = jos.getString("cli_nama_lengkap");
                        String pengajuan_nilai_pengajuan = jos.getString("pengajuan_nilai_pengajuan");
                        String pengajuan_durasi_hari = jos.getString("pengajuan_durasi_hari");
                        String bayar = jos.getString("bayar");
                        String jatuh_tempo = jos.getString("jatuh_tempo");
                        String ajustat_label = jos.getString("ajustat_label");
                        String ajutahap_label = jos.getString("ajutahap_label");

                        ArchivePengajuanModel pengajuanModel = new ArchivePengajuanModel(pengajuan_tanggal, pengajuan_nomor_pengajuan, cli_nama_lengkap, pengajuan_nilai_pengajuan, pengajuan_durasi_hari + " Hari", bayar.replaceAll("\\<.*?>", "").replace("Sisa", "\nSisa"), jatuh_tempo, ajustat_label + "\n" + ajutahap_label);
                        dataSet.add(pengajuanModel);
                    }

                    adapter.notifyDataSetChanged();

                    currentPage++;

                    if (ja.length() == 0) {
                        endpage = true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //ShowToast("Request Timeout");
                }

                ToggleLoading(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ToggleLoading(false);
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + AppConf.APIKEY);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("tipe", sessionManager.getTitle().replace(getString(R.string.pengajuan), ""));
                params.put("page", currentPage + "");
                return params;
            }
        };

        stringRequest.setTag(TAG);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    public void ToggleLoading(boolean tampilkan) {

        try {

            if (tampilkan) {
                rvList.setVisibility(View.GONE);
                pbLoading.setVisibility(View.VISIBLE);
            } else {

                rvList.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                pbLoadMore.setVisibility(View.GONE);

                refresh = false;
                swipeRefreshLayout.setRefreshing(false);
//                pbLoadMore.animate().translationY(pbLoadMore.getHeight());
            }

        } catch (Exception e) {

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.txTgl1, R.id.txTgl2, R.id.btCari, R.id.btCari1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txTgl1:
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTgl1.setText(tgl);
                            }
                        });
                cdp.show(getChildFragmentManager(), null);
                break;
            case R.id.txTgl2:
                CalendarDatePickerDialogFragment cdp2 = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTgl2.setText(tgl);
                            }
                        });
                cdp2.show(getChildFragmentManager(), null);
                break;
            case R.id.btCari:
                offset = 0;
                if (klienArrayList != null) {
                    klienArrayList.clear();
                } else {
                    klienArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getPengajuan2(txPencarian.getText().toString().replace(" ", "+"));
                View view1 = mActivity.getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
            case R.id.btCari1:
                offset = 0;
                if (klienArrayList != null) {
                    klienArrayList.clear();
                } else {
                    klienArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getPengajuan1(txTgl1.getText().toString(), txTgl2.getText().toString());
                View view2 = mActivity.getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onNotice);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(onNotice, new IntentFilter(getString(R.string.archive)));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onNotice);

    }


    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            sessionManager = new SessionManager(mActivity);
            GetData();

        }
    };

    private void getPengajuan(final String sessionId) {
        Log.d("pengajuan_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_ALL + "/" + id + "/" + LIMIT + "/" + offset + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan_all", response);
//                swipeRefreshLayout.setRefreshing(false);
//                pbLoading.setVisibility(View.GONE);
                ToggleLoading(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PengajuanResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            klienArrayList.add(klienResponse.getData()[i]);
//                            adapter.notifyItemChanged(klienArrayList.size() - 1);
                        }
                        adapter.notifyDataSetChanged();
                        offset += 1;
                        Log.d("offset", Integer.toString(offset));
                        getPengajuan(sessionId);

//                        loading = true;
//                        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                visibleItemCount = layoutManager.getChildCount();
//                                totalItemCount = layoutManager.getItemCount();
//                                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//
//                                if (loading) {
//                                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                                        loading = false;
//                                        offset += 1;
//                                        Log.d("offset", Integer.toString(offset));
//                                        getPengajuan(sessionId);
//                                    }
//                                }
//                            }
//                        });
                    } else {
                        Intent intent = new Intent("loading");
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(mActivity, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mActivity, "no connection", Toast.LENGTH_SHORT).show();
                } else {


//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.errorHandling(error);
//                    sessionManager.setPage(0);
//                    sessionManager.logoutUser();
//
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
//
//                    Intent intent = new Intent(mActivity, LoginActivity.class);
//                    mActivity.startActivity(intent);
//                    mActivity.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(mActivity);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getPengajuan1(final String tgl1, final String tgl2) {
        Log.d("pengajuan1_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_ALL + "/" + id + "/" + LIMIT + "/" + offset + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan1_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PengajuanResponse.class);

                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            klienArrayList.add(klienResponse.getData()[i]);
//                            adapter.notifyItemChanged(pencairanArrayList.size() - 1);
                        }
                        adapter.notifyDataSetChanged();

                        offset += 1;
                        getPengajuan1(tgl1, tgl2);

                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent("loading");
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(mActivity, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mActivity, "no connection", Toast.LENGTH_SHORT).show();
                } else {

//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.errorHandling(error);
//                    sessionManager.setPage(0);
//                    sessionManager.logoutUser();
//
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
//
//
//                    Intent intent = new Intent(mActivity, LoginActivity.class);
//                    mActivity.startActivity(intent);
//                    mActivity.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(mActivity);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getPengajuan2(final String cari) {
        Log.d("pengajuan1_all", "ok");

        String query = "";
        if (cari.compareTo("") != 0) {
            query = "/" + cari;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_FIND + query + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan1_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PengajuanResponse.class);

                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            klienArrayList.add(klienResponse.getData()[i]);
//                            adapter.notifyItemChanged(pencairanArrayList.size() - 1);
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent("loading");
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(mActivity, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mActivity, "no connection", Toast.LENGTH_SHORT).show();
                } else {

//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.errorHandling(error);
//                    sessionManager.setPage(0);
//                    sessionManager.logoutUser();
//
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
//
//                    Intent intent = new Intent(mActivity, LoginActivity.class);
//                    mActivity.startActivity(intent);
//                    mActivity.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(mActivity);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

}
