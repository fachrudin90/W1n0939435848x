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
import android.widget.Button;
import android.widget.EditText;
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
import com.tamboraagungmakmur.winwin.Adapter.HomeAdapter;
import com.tamboraagungmakmur.winwin.Model.HomeModel;
import com.tamboraagungmakmur.winwin.Model.Task;
import com.tamboraagungmakmur.winwin.Model.TaskResponse;
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
public class HomeOther extends Fragment {


    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.txTgl1)
    EditText txTgl1;
    @Bind(R.id.txTgl2)
    EditText txTgl2;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.btCari1)
    Button btCari1;
    @Bind(R.id.rvList)
    RecyclerView rvList;
    @Bind(R.id.setselesai)
    Button setSelesai;
    @Bind(R.id.hapus)
    Button hapus;
    @Bind(R.id.total)
    TextView total;
    
    private final static String TAG = "HOME_OTHER";
    private RequestQueue requestQueue;

    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<HomeModel> dataSet = new ArrayList<>();

    private ArrayList<Task> taskArrayList = new ArrayList<>();
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    private View view;

    private ArrayList<String> taskId = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    public HomeOther() {
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
            view = inflater.inflate(R.layout.fragment_home_other, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
        requestQueue = Volley.newRequestQueue(mActivity);

        total.setText("Total Task Other: 0");

        Intent intent = new Intent("title");
        intent.putExtra("message", "Task");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestQueue.cancelAll(TAG);
                Intent intent = new Intent("home_analisa");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                offset = 0;
                if (taskArrayList != null) {
                    taskArrayList.clear();
                } else {
                    taskArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getTask();
            }
        });

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(mActivity, taskArrayList);
        rvList.setAdapter(adapter);

//        GetData();
        getTask();

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tasks = "";
                for (int i=0; i<taskId.size(); i++) {
                    if (i == 0) {
                        tasks += taskId.get(i);
                    } else {
                        tasks += "," + taskId.get(i);
                    }
                }
                Intent intent = new Intent(getContext(), TaskRemoveActivity.class);
                intent.putExtra("ids", tasks);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        setSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tasks = "";
                for (int i=0; i<taskId.size(); i++) {
                    if (i == 0) {
                        tasks += taskId.get(i);
                    } else {
                        tasks += "," + taskId.get(i);
                    }
                }
                Intent intent = new Intent(getContext(), TaskSelesaiActivity.class);
                intent.putExtra("ids", tasks);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("task_update"));
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver1,
                new IntentFilter("task_other1"));

        return view;
    }

    @Override
    public void onStop() {
//        VolleyHttp.getInstance(mActivity).getRequestQueue().cancelAll(TAG);
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String idTask = intent.getStringExtra("id");
            boolean isChecked = intent.getBooleanExtra("checked", false);

            if (isChecked) {
                taskId.add(idTask);
            } else {
                taskId.remove(idTask);
            }

        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            requestQueue.cancelAll(TAG);
            Intent intent1 = new Intent("home_analisa");
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent1);
            offset = 0;
            if (taskArrayList != null) {
                taskArrayList.clear();
            } else {
                taskArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            getTask();
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void GetData() {

        dataSet.clear();
        for (int i = 0; i < 15; i++) {

            HomeModel homeModel = new HomeModel("2017-03-01", "Analisa", "0432-20020-135325 Richy", "Sherly", "Aktif");
            dataSet.add(homeModel);
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
                requestQueue.cancelAll(TAG);
                Intent intent = new Intent("home_analisa");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                offset = 0;
                if (taskArrayList != null) {
                    taskArrayList.clear();
                } else {
                    taskArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getTask1(txTgl1.getText().toString(), txTgl2.getText().toString());
                break;
            case R.id.btCari1:
                requestQueue.cancelAll(TAG);
                Intent intent1 = new Intent("home_analisa");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent1);
                offset = 0;
                if (taskArrayList != null) {
                    taskArrayList.clear();
                } else {
                    taskArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getTask2(txPencarian.getText().toString().replace(" ", "+"));
                break;
        }
    }

    private void getTask() {
        Log.d("task_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_TASK_OTHERS + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("task_all", response);
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Task Other: " + klienResponse.getData()[0].getCount());
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
                        getTask();

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
//                                        getTask();
//                                    }
//                                }
//                            }
//                        });
                    } else {
                        Intent intent = new Intent("home_analisa");
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

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getTask1(final String tgl1, final String tgl2) {
        Log.d("task_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_TASK_OTHERS + "/" + LIMIT + "/" + offset + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("task_all", response);
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Task Other: " + klienResponse.getData()[0].getCount());
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
                        getTask1(tgl1, tgl2);

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
//                                        getTask1(tgl1, tgl2);
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
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getTask2(final String cari) {
        Log.d("task_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        String query = "";
        if (cari.compareTo("") != 0) {
            query = "/" + cari;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_TASK_OTHERS_FIND + query + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("task_all", response);
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Task Other: " + klienResponse.getData()[0].getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            taskArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(taskArrayList.size() - 1);
                        }

                    } else {
                        Intent intent = new Intent("home_analisa");
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

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }
}
