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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.JanjiBayarAdapter;
import com.tamboraagungmakmur.winwin.Model.JanjiBayar;
import com.tamboraagungmakmur.winwin.Model.JanjiBayarResponse1;
import com.tamboraagungmakmur.winwin.Model.PerpanjanganResponse1;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.UnsafeOkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by innan on 8/22/2017.
 */

public class JanjiBayarDebtCollector extends Fragment {


    private static final int MY_SOCKET_TIMEOUT_MS = 20000;
    private final static String TAG = "JANJIBAYAR_DEBTCOLLECTOR";
    @Bind(R.id.txTgl1)
    EditText txTgl1;
    @Bind(R.id.txTgl2)
    EditText txTgl2;
    @Bind(R.id.btCari1)
    Button btCari1;
    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.total)
    TextView total;
    @Bind(R.id.rvList)
    RecyclerView rvList;
    @Bind(R.id.refresh1)
    SwipeRefreshLayout refresh1;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;

    private RequestQueue requestQueue;
    private Call<PerpanjanganResponse1> call;

    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<JanjiBayar> dataSet = new ArrayList<>();




    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private String sessionId;

    private View view;
    private FloatingActionButton up, down;

    public JanjiBayarDebtCollector() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent("title");
        intent.putExtra("message", "Janji Bayar Dari Debt Collector");
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
            view = inflater.inflate(R.layout.fragment_janji_bayar_collection, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
        requestQueue = Volley.newRequestQueue(mActivity);
//        title.setText(getResources().getString(R.string.pengajuan_aktif));

        Intent intent = new Intent("title");
        intent.putExtra("message", "Janji Bayar Dari Debt Collector");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        total.setText("Total Janji Bayar Dari Debt Collector : 0");

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new JanjiBayarAdapter(mActivity, dataSet);
        rvList.setAdapter(adapter);

        up = (FloatingActionButton) view.findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvList.scrollToPosition(0);
            }
        });

        down = (FloatingActionButton) view.findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvList.scrollToPosition(dataSet.size() - 1);
            }
        });

//        GetData();
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent("loading");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                requestQueue.cancelAll(TAG);
                if (call != null) {
                    call.cancel();
                }
                offset = 0;
                if (dataSet != null) {
                    dataSet.clear();
                } else {
                    dataSet = new ArrayList<>();
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
            Intent intent1 = new Intent("loading");
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent1);
            requestQueue.cancelAll(TAG);
            if (call != null) {
                call.cancel();
            }
            if (dataSet != null) {
                dataSet.clear();
            }

            adapter.notifyDataSetChanged();
            offset = 0;
            getPengajuan();
        }
    };

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        if (call != null) {
            call.cancel();
        }
//        VolleyHttp.getInstance(mActivity).getRequestQueue().cancelAll(AppConf.httpTag);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
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
                Intent intent = new Intent("loading");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                requestQueue.cancelAll(TAG);
                if (call != null) {
                    call.cancel();
                }
                progressBar.setVisibility(View.VISIBLE);
                offset = 0;
                if (dataSet != null) {
                    dataSet.clear();
                } else {
                    dataSet = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                if (txPencarian.getText().toString().compareTo("") == 0) {
                    getPengajuan();
                } else {
//                    saveFile(txPencarian.getText().toString().replace(" ", "+"));
                    getPengajuan2(txPencarian.getText().toString().replace(" ", "+"));

                }
                View view1 = mActivity.getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
            case R.id.btCari1:
                Intent intent1 = new Intent("loading");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent1);
                requestQueue.cancelAll(TAG);
                if (call != null) {
                    call.cancel();
                }
                progressBar.setVisibility(View.VISIBLE);
                offset = 0;
                if (dataSet != null) {
                    dataSet.clear();
                } else {
                    dataSet = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                if (txTgl1.getText().toString().compareTo("") == 0 || txTgl2.getText().toString().compareTo("") == 0) {
                    getPengajuan();
                } else {
                    getPengajuan1(txTgl1.getText().toString(), txTgl2.getText().toString());
                }
                View view2 = mActivity.getCurrentFocus();
                if (view2 != null) {
                    InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
        }
    }

    private void getPengajuan() {
        Log.d("pengajuan1_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_JANJI_BAYAR+ "/" + LIMIT + "/" + offset + "/" + "Debt+Collector" +"?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan1_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JanjiBayarResponse1 klienResponse = new Gson().fromJson(jsonObject1.toString(), JanjiBayarResponse1.class);


                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Janji Bayar Dari Debt Collector : " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            dataSet.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(dataSet.size() - 1);
                        }
//                        adapter.notifyDataSetChanged();

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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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

            @Override
            public RetryPolicy getRetryPolicy() {
                // here you can write a custom retry policy
                return new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getPengajuan1(final String tgl1, final String tgl2) {
        Log.d("pengajuan1_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_JANJI_BAYAR + "/" + LIMIT + "/" + offset + "/" + "Debt+Collector" + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan1_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JanjiBayarResponse1 klienResponse = new Gson().fromJson(jsonObject1.toString(), JanjiBayarResponse1.class);


                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Janji Bayar Dari Debt Collector : " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            dataSet.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(dataSet.size() - 1);
                        }
//                        adapter.notifyDataSetChanged();

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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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

            @Override
            public RetryPolicy getRetryPolicy() {
                // here you can write a custom retry policy
                return new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }


    private void getPengajuan2(String query) {
        Log.d("pengajuan1_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_JANJI_BAYAR_FIND+ "/" + query + "/" + "Debt+Collector" +"?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan1_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    JanjiBayarResponse1 klienResponse = new Gson().fromJson(jsonObject1.toString(), JanjiBayarResponse1.class);


                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Janji Bayar Dari Debt Collector : " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            dataSet.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(dataSet.size() - 1);
                        }
//                        adapter.notifyDataSetChanged();

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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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

            @Override
            public RetryPolicy getRetryPolicy() {
                // here you can write a custom retry policy
                return new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }



    public static class ServiceGenerator {

        public static final String API_BASE_URL = AppConf.BASE_URL;

        static OkHttpClient.Builder okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);

        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        public static <S> S createService(Class<S> serviceClass) {
            Retrofit retrofit = builder.client(okHttpClient.build()).build();
            return retrofit.create(serviceClass);
        }
    }

    public interface FileUploadService {
        @GET
        Call<PerpanjanganResponse1> upload(@Url String url);
    }

//    private void saveFile(final String cari) {
//
//        FileUploadService service =
//                ServiceGenerator.createService(FileUploadService.class);
//
//
//        SessionManager sessionManager = new SessionManager(mActivity);
//
//        call = service.upload(AppConf.URL_PERPANJANGAN_FINDCOLLECTION + "/" + cari + "?_session=" + sessionManager.getSessionId());
//        call.enqueue(new Callback<PerpanjanganResponse1>() {
//
//            @Override
//            public void onResponse(Call<PerpanjanganResponse1> call, retrofit2.Response<PerpanjanganResponse1> response) {
////                Log.d("share foto", response.body());
//
//                swipeRefreshLayout.setRefreshing(false);
//                progressBar.setVisibility(View.GONE);
//                PerpanjanganResponse1 klienResponse = response.body();
//
//                if (klienResponse.getData().length != 0) {
//
//                    for (int i = 0; i < klienResponse.getData().length; i++) {
////                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
////                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
//                        pencairanArrayList.add(klienResponse.getData()[i]);
//                        adapter.notifyItemChanged(pencairanArrayList.size() - 1);
//                    }
////                        adapter.notifyDataSetChanged();
//                } else {
//                    Intent intent = new Intent("loading");
//                    LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<PerpanjanganResponse1> call, Throwable t) {
//                Log.e("Upload error:", t.getMessage());
//                if (call.isCanceled()) {
////                    Log.e(TAG, "request was cancelled");
//                } else {
//                    SessionManager sess = new SessionManager(mActivity);
//                    sess.setPage(0);
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
////                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
//                    SessionManager sessionManager = new SessionManager(mActivity);
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
//
//                    Intent intent = new Intent(mActivity, LoginActivity.class);
//                    mActivity.startActivity(intent);
//                    mActivity.finish();
//                }
//            }
//        });
//    }

}
