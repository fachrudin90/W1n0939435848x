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
import com.tamboraagungmakmur.winwin.Adapter.ArchivePencarianAdapter1;
import com.tamboraagungmakmur.winwin.Model.ArchivePencarianModel;
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
 * A simple {@link Fragment} subclass.
 */
public class ArchivePencarian extends Fragment {


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

    private final static String TAG = "ARCHIVE_PENCAIRAN";
    
    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ArchivePencarianModel> dataSet = new ArrayList<>();

    private ArrayList<Pengajuan> pengajuanArrayList = new ArrayList<>();
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

    private FloatingActionButton up;

    private String id;

    private ArrayList<String> pencPilih = new ArrayList<>();
    private Button cairkan;
    private ProgressBar progressBar1;

    public ArchivePencarian() {
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
            view = inflater.inflate(R.layout.fragment_archive_pencarian, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();

        id = getArguments().getString("id");
        Intent intent = new Intent("title");
        intent.putExtra("message", "Archive Pencairan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new ArchivePencarianAdapter1(mActivity, pencairanArrayList);
        rvList.setAdapter(adapter);

        up = (FloatingActionButton) view.findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvList.scrollToPosition(0);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);


        sessionManager = new SessionManager(mActivity);
        sessionId = sessionManager.getSessionId();
//        GetData();
        getKlien(sessionManager.getSessionId());

        cairkan = (Button) view.findViewById(R.id.cairkan);
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressbar1);
        cairkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tasks = "";
                for (int i=0; i<pencPilih.size(); i++) {
                    if (i == 0) {
                        tasks += pencPilih.get(i);
                    } else {
                        tasks += "," + pencPilih.get(i);
                    }
                }
                Intent intent = new Intent(getContext(), PencCairActivity.class);
                intent.putExtra("ids", tasks);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        if (sessionManager.isPENCAIRAN() || sessionManager.isPELUNASAN()) {
            cairkan.setVisibility(View.VISIBLE);
        } else {
            cairkan.setVisibility(View.GONE);
        }

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mMessageReceiver,
                new IntentFilter("tahap"));
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver1,
                new IntentFilter("pencairan_pilih"));

        return view;
    }

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String idTask = intent.getStringExtra("id");
            boolean isChecked = intent.getBooleanExtra("checked", false);

            if (isChecked) {
                pencPilih.add(idTask);
            } else {
                pencPilih.remove(idTask);
            }

        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent("loading");
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent1);
            Log.d("pencairan_aktif", "ok");
            if (pencairanArrayList !=  null) {
                pencairanArrayList.clear();
            } else {
                pencairanArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            offset = 0;
            getKlien(sessionManager.getSessionId());
        }
    };

    @Override
    public void onStop() {
        VolleyHttp.getInstance(mActivity).getRequestQueue().cancelAll(TAG);
        super.onStop();
    }

    private void GetData() {

        dataSet.clear();
        for (int i = 0; i < 15; i++) {

            ArchivePencarianModel pencarianModel = new ArchivePencarianModel("5SDF-54FS-85FF-SDFS", "BCA", "0854548484\nBudi Susanto", "10.000.000", "Sudah Cair\n2017-02-20 08:00:00\nOleh : Elok");
            dataSet.add(pencarianModel);
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

    private void getKlien(final String sessionId) {
        Log.d("pengajuan1_all", "ok");

//        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENCAIRAN_ALL + "/" + id + "/" + LIMIT + "/" + offset + "?_session=" + sessionId, new Response.Listener<String>() {
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
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            pencairanArrayList.add(klienResponse.getData()[i]);
//                            adapter.notifyItemChanged(pencairanArrayList.size() - 1);
                        }
                        adapter.notifyDataSetChanged();
                        offset += 1;
                        Log.d("offset", Integer.toString(offset));
                        getKlien(sessionId);

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
//                                        getKlien(sessionId);
//                                    }
//                                }
//                            }
//                        });

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

    private void getPengajuan1(final String tgl1, final String tgl2) {
        Log.d("pengajuan1_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENCAIRAN_ALL + "/" + id + "/" + LIMIT + "/" + offset + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionId, new Response.Listener<String>() {
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENCAIRAN_FIND + query + "?_session=" + sessionId, new Response.Listener<String>() {
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

    private void getEmail(String klien, final String nomor, String sessionId) {
        Log.d("pencairan_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENCAIRAN_SHOW_BY_PENGAJUAN + "/" + klien + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pencairan_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PencairanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PencairanResponse.class);
                    klienResponse.setPengajuan(nomor);

//                    pencairanArrayList.add(klienResponse);
//                    adapter.notifyItemChanged(pencairanArrayList.size() - 1);


//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
//                    if (klienResponse.getData().length != 0) {
//                        swipeRefreshLayout.setRefreshing(false);
//                        progressBar.setVisibility(View.GONE);
//
//
//
//                        for (int i = 0; i < klienResponse.getData().length; i++) {
////                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
////                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
//                            emailArrayList.add(klienResponse.getData()[i]);
//                            adapter.notifyItemChanged(emailArrayList.size() - 1);
//                        }
//                    }


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
