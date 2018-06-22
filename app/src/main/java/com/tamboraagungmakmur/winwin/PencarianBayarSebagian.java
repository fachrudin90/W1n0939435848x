package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.tamboraagungmakmur.winwin.Adapter.ArchivePencarianAdapter;
import com.tamboraagungmakmur.winwin.Model.ArchivePengajuanModel;
import com.tamboraagungmakmur.winwin.Model.Pencairan;
import com.tamboraagungmakmur.winwin.Model.PencairanResponse;
import com.tamboraagungmakmur.winwin.Model.Pengajuan;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
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
 * Created by innan on 8/22/2017.
 */

public class PencarianBayarSebagian extends Fragment {

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
    @Bind(R.id.total)
    TextView total;

    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ArchivePengajuanModel> dataSet = new ArrayList<>();

    private ArrayList<Pengajuan> klienArrayList = new ArrayList<>();
    private ArrayList<Pencairan> pencairanArrayList = new ArrayList<>();


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private String sessionId;
    private View view;

    public PencarianBayarSebagian() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent("title");
        intent.putExtra("message", "Pencairan Bayar Sebagian");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
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
            view = inflater.inflate(R.layout.fragment_archive_pencarian, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
//        title.setText(getResources().getString(R.string.pengajuan_aktif));

        Intent intent = new Intent("title");
        intent.putExtra("message", "Pencairan Bayar Sebagian");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        total.setText("Total Pencairan Bayar Sebagian: 0");

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new ArchivePencarianAdapter(mActivity, pencairanArrayList);
        rvList.setAdapter(adapter);

//        GetData();
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                if (pencairanArrayList != null) {
                    pencairanArrayList.clear();
                } else {
                    pencairanArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getPengajuan();
            }
        });

        sessionManager = new SessionManager(mActivity);
        sessionId = sessionManager.getSessionId();
        getPengajuan();

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mMessageReceiver,
                new IntentFilter("tahap"));
        return view;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("pencairan_aktif", "ok");
            if (pencairanArrayList !=  null) {
                pencairanArrayList.clear();
            } else {
                pencairanArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            offset = 0;
            getPengajuan();
        }
    };

    @Override
    public void onDestroy() {
        Log.d("pencairan_aktif", "on_destroy");
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        VolleyHttp.getInstance(mActivity).getRequestQueue().cancelAll(AppConf.httpTag);
        super.onStop();
    }


    private void GetData() {

        dataSet.clear();
        for (int i = 0; i < 15; i++) {

            ArchivePengajuanModel pengajuanModel = new ArchivePengajuanModel("03-01-2017", "5SDF-54FS-85FF-SDFS", "Budi Santoso", "10.000.000", "28 Hari", "2.000.000\nSisa: 1.250.000", "15-07-2017", "Ditolak");
            dataSet.add(pengajuanModel);
        }

        adapter.notifyDataSetChanged();
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
                if (pencairanArrayList != null) {
                    pencairanArrayList.clear();
                } else {
                    pencairanArrayList = new ArrayList<>();
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
                if (pencairanArrayList != null) {
                    pencairanArrayList.clear();
                } else {
                    pencairanArrayList = new ArrayList<>();
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

    private void getPengajuan() {
        Log.d("pengajuan1_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENCAIRAN_BAYAR_SEBAGIAN + "/" + LIMIT + "/" + offset + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan1_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PencairanResponse1 klienResponse = new Gson().fromJson(jsonObject1.toString(), PencairanResponse1.class);

                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Pencairan Bayar Sebagian: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            pencairanArrayList.add(klienResponse.getData()[i]);
//                            adapter.notifyItemChanged(pencairanArrayList.size() - 1);
                        }
                        adapter.notifyDataSetChanged();

                        offset += 1;
                        getPengajuan();

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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getPengajuan1(final String tgl1, final String tgl2) {
        Log.d("pengajuan1_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENCAIRAN_BAYAR_SEBAGIAN + "/" + LIMIT + "/" + offset + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan1_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PencairanResponse1 klienResponse = new Gson().fromJson(jsonObject1.toString(), PencairanResponse1.class);

                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Pencairan Bayar Sebagian: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            pencairanArrayList.add(klienResponse.getData()[i]);
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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getPengajuan2(final String cari) {
        Log.d("pengajuan1_all", "ok");

        String query = "";
        if (cari.compareTo("") != 0) {
            query = "/" + cari;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENCAIRAN_FINDBAYARSEBAGIAN + query + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan1_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PencairanResponse1 klienResponse = new Gson().fromJson(jsonObject1.toString(), PencairanResponse1.class);

                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            pencairanArrayList.add(klienResponse.getData()[i]);
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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getEmail(String klien, final String nomor) {
        Log.d("pencairan1_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENCAIRAN_SHOW_BY_PENGAJUAN + "/" + klien + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pencairan1_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PencairanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PencairanResponse.class);
                    klienResponse.setPengajuan(nomor);

//                    pencairanArrayList.add(klienResponse);
//                    adapter.notifyItemChanged(pencairanArrayList.size() - 1);



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
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                SessionManager sessionManager = new SessionManager(mActivity);
                sessionManager.errorHandling(error);
//                sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
//                Intent intent = new Intent(mActivity, LoginActivity.class);
//                mActivity.startActivity(intent);
//                mActivity.finish();

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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

}
