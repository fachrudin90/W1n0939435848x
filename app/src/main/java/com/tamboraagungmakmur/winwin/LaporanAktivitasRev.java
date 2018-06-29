package com.tamboraagungmakmur.winwin;


import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.LogActivityAdapter;
import com.tamboraagungmakmur.winwin.Adapter.LogActivityDetailAdapter;
import com.tamboraagungmakmur.winwin.Adapter.LogActivityRekapAdapter;
import com.tamboraagungmakmur.winwin.Model.LogActivity;
import com.tamboraagungmakmur.winwin.Model.LogActivityDetail;
import com.tamboraagungmakmur.winwin.Model.LogActivityDetailResponse;
import com.tamboraagungmakmur.winwin.Model.LogActivityRekap;
import com.tamboraagungmakmur.winwin.Model.LogActivityRekapResponse;
import com.tamboraagungmakmur.winwin.Model.LogActivityResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class LaporanAktivitasRev extends Fragment {


    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.txTgl1)
    EditText txTgl1;
    @Bind(R.id.txTgl2)
    EditText txTgl2;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.rvList)
    RecyclerView rvList;
    @Bind(R.id.txPrint)
    TextView txPrint;
    @Bind(R.id.total)
    TextView total;

    private final static String TAG = "LAPORAN_AKTIVITAS";
    @Bind(R.id.btCariKata)
    Button btCariKata;
    @Bind(R.id.btClear)
    Button btClear;
    @Bind(R.id.btClearKata)
    Button btClearKata;
    @Bind(R.id.txTglRek1)
    EditText txTglRek1;
    @Bind(R.id.btCariRek)
    Button btCariRek;
    @Bind(R.id.txPencarianRek)
    EditText txPencarianRek;
    @Bind(R.id.btCariKataRek)
    Button btCariKataRek;
    @Bind(R.id.txTglRek2)
    EditText txTglRek2;
    @Bind(R.id.btClearRek)
    Button btClearRek;
    @Bind(R.id.btClearKataRek)
    Button btClearKataRek;
    @Bind(R.id.totalRek)
    TextView totalRek;
    @Bind(R.id.rvListRek)
    RecyclerView rvListRek;
    @Bind(R.id.refreshRek)
    SwipeRefreshLayout refreshRek;
    @Bind(R.id.progressbarRek)
    ProgressBar progressbarRek;
    private RequestQueue requestQueue;

    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager2;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapterRekap;

    private ArrayList<LogActivityDetail> taskArrayList = new ArrayList<>();
    private ArrayList<LogActivityRekap> rekapArrayList = new ArrayList<>();

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private int offset2;
    private static final int LIMIT = 20;
    private String date1, date2, date3, date4, date5;

    private View view;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    public LaporanAktivitasRev() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
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
            view = inflater.inflate(R.layout.fragment_laporan_aktivitasrev, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
        requestQueue = Volley.newRequestQueue(mActivity);

        Intent intent = new Intent("title");
        intent.putExtra("message", "Laporan Aktivitas");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        total.setText("Total Detail Aktivitas: 0");
        totalRek.setText("Total Rekap Aktivitas: 0");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                if (taskArrayList != null) {
                    taskArrayList.clear();
                } else {
                    taskArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getTask(txPencarian.getText().toString(), date2, date3);
            }
        });


        refreshRek.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset2 = 0;
                if (rekapArrayList != null) {
                    rekapArrayList.clear();
                } else {
                    rekapArrayList = new ArrayList<>();
                }
                adapterRekap.notifyDataSetChanged();
                getTask2(txPencarian.getText().toString(), date4, date5);
            }
        });

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new LogActivityDetailAdapter(taskArrayList);
        rvList.setAdapter(adapter);

        layoutManager2 = new LinearLayoutManager(mActivity);
        rvListRek.setLayoutManager(layoutManager2);
        adapterRekap = new LogActivityRekapAdapter(rekapArrayList);
        rvListRek.setAdapter(adapterRekap);

//        GetData();

        date1 = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        date2 = date1;
        date3 = date1;
        date4 = date1;
        date5 = date1;
        getTask(txPencarian.getText().toString(), date2, date3);
        getTask2(txPencarian.getText().toString(), date4, date5);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @OnClick({R.id.txTgl1, R.id.txTgl2, R.id.txTglRek1, R.id.txTglRek2,R.id.btCari, R.id.btCariKata,R.id.btCariRek, R.id.btCariKataRek, R.id.txPrint, R.id.btClear, R.id.btClearKata, R.id.btClearRek, R.id.btClearKataRek})
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
                                date2 = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
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
                                date3 = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
                                txTgl2.setText(tgl);
                            }
                        });
                cdp2.show(getChildFragmentManager(), null);
                break;
            case R.id.txTglRek1:
                CalendarDatePickerDialogFragment cdpR1 = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                date4 = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
                                txTglRek1.setText(tgl);
                            }
                        });
                cdpR1.show(getChildFragmentManager(), null);
                break;
            case R.id.txTglRek2:
                CalendarDatePickerDialogFragment cdpR2 = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                date5 = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
                                txTglRek2.setText(tgl);
                            }
                        });
                cdpR2.show(getChildFragmentManager(), null);
                break;
            case R.id.btCari:
                actionCari();
                break;
            case R.id.btCariKata:
                actionCari();
                break;
            case R.id.btCariRek:
                actionCariRek();
                break;
            case R.id.btCariKataRek:
                actionCariRek();
                break;
            case R.id.txPrint:
                break;
            case R.id.btClear:
                txTgl1.setText("");
                txTgl2.setText("");
                break;
            case R.id.btClearKata:
                txPencarian.setText("");
                break;
            case R.id.btClearRek:
                txTglRek1.setText("");
                txTglRek2.setText("");
                break;
            case R.id.btClearKataRek:
                txPencarianRek.setText("");
                break;
        }
    }

    private void actionCari() {
        progressBar.setVisibility(View.VISIBLE);
        offset = 0;
        if (taskArrayList != null) {
            taskArrayList.clear();
        } else {
            taskArrayList = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();
        getTask(txPencarian.getText().toString(), date2, date3);
    }

    private void actionCariRek() {
        progressbarRek.setVisibility(View.VISIBLE);
        offset2 = 0;
        if (rekapArrayList != null) {
            rekapArrayList.clear();
        } else {
            rekapArrayList = new ArrayList<>();
        }
        adapterRekap.notifyDataSetChanged();
        getTask2(txPencarianRek.getText().toString(), date4, date5);
    }

    private void getTask(final String cari, final String tgl1, final String tgl2) {
        Log.d("internal_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_LOGACTIVITY_ALL + "/" + cari + "/" + LIMIT + "/" + offset + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("internal_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogActivityDetailResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogActivityDetailResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Detail Aktivitas: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            taskArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(taskArrayList.size() - 1);
                        }
//                        adapter.notifyDataSetChanged();
                        offset += 1;
                        Log.d("offset", Integer.toString(offset));
                        getTask(cari, tgl1, tgl2);

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
//                                        getTask(tgl1, tgl2);
//                                    }
//                                }
//                            }
//                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

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
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
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
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getTask2(final String cari, final String tgl1, final String tgl2) {
        Log.d("internal_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_LOGACTIVITY_REKAP + "/" + cari + "/" + LIMIT + "/" + offset2 + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("internal_all", response);
                refreshRek.setRefreshing(false);
                progressbarRek.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogActivityRekapResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogActivityRekapResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (totalRek != null) {
                            totalRek.setText("Total Rekap Aktivitas: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            rekapArrayList.add(klienResponse.getData()[i]);
                            adapterRekap.notifyItemChanged(rekapArrayList.size() - 1);
                        }
//                        adapter.notifyDataSetChanged();
                        offset2 += 1;
                        Log.d("offset", Integer.toString(offset));
                        getTask2(cari, tgl1, tgl2);

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
//                                        getTask(tgl1, tgl2);
//                                    }
//                                }
//                            }
//                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

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
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
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
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }
}

