package com.tamboraagungmakmur.winwin;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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

import com.ajts.androidmads.library.SQLiteToExcel;
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
import com.tamboraagungmakmur.winwin.Adapter.OperatorAdapter;
import com.tamboraagungmakmur.winwin.Model.Operator1;
import com.tamboraagungmakmur.winwin.Model.OperatorResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.DatabaseHandler;
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
import retrofit2.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class LaporanOperator extends Fragment {


//    @Bind(R.id.txPencarian)
//    EditText txPencarian;
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

    private final static String TAG = "LAPORAN_OPERATOR";
    private RequestQueue requestQueue;

    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ArrayList<Operator1> taskArrayList = new ArrayList<>();

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;
    private String date1, date2, date3;

    private View view;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private Button export;
    private ProgressBar progressBar1;
    private DatabaseHandler db;
    private SQLiteToExcel sqliteToExcel;

    private Call<OperatorResponse> call;
    private AsyncTask<String, Void, String> insertdb;

    public LaporanOperator() {
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
            view = inflater.inflate(R.layout.fragment_laporan_operator1, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
        requestQueue = Volley.newRequestQueue(mActivity);

        db = new DatabaseHandler(mActivity);

        Intent intent = new Intent("title");
        intent.putExtra("message", "Laporan Operator");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        total.setText("Total Laporan Operator: 0");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                export.setEnabled(false);
                export.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_grey2));
                if (call != null) {
                    call.cancel();
                }
                if (insertdb != null) {
                    insertdb.cancel(true);
                }
                requestQueue.cancelAll(TAG);
                db = new DatabaseHandler(mActivity);
                offset = 0;
                if (taskArrayList != null) {
                    taskArrayList.clear();
                } else {
                    taskArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getTask(date1, date1);
            }
        });

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new OperatorAdapter(taskArrayList);
        rvList.setAdapter(adapter);

        export = (Button) view.findViewById(R.id.export);
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressbar1);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                export.setVisibility(View.INVISIBLE);
                progressBar1.setVisibility(View.VISIBLE);
//                List<Internal> internals = db.getAllInternals();
//                Log.d("internals", internals.get(0).getKlien());
                sqliteToExcel = new SQLiteToExcel(mActivity, "winwin");
                sqliteToExcel.exportSingleTable("operator", "operator_"+txTgl1.getText().toString()+"_"+txTgl2.getText().toString()+".xls", new SQLiteToExcel.ExportListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(mActivity, "Exporting...", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCompleted(String filePath) {
                        export.setVisibility(View.VISIBLE);
                        progressBar1.setVisibility(View.INVISIBLE);
                        Toast.makeText(mActivity, filePath, Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.e("export", e.toString());
                        Toast.makeText(mActivity, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

//        GetData();

        date1 = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        date2 = date1;
        date3 = date1;
        getTask(date1, date1);

        return view;
    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        if (call != null) {
            call.cancel();
        }
        if (insertdb != null) {
            insertdb.cancel(true);
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @OnClick({R.id.txTgl1, R.id.txTgl2, R.id.btCari, R.id.txPrint})
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
            case R.id.btCari:
                export.setEnabled(false);
                export.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_grey2));
                if (call != null) {
                    call.cancel();
                }
                if (insertdb != null) {
                    insertdb.cancel(true);
                }
                requestQueue.cancelAll(TAG);
                db = new DatabaseHandler(mActivity);
                progressBar.setVisibility(View.VISIBLE);
                offset = 0;
                if (taskArrayList != null) {
                    taskArrayList.clear();
                } else {
                    taskArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getTask(date2, date3);
                break;
            case R.id.txPrint:
                break;
        }
    }

    private void getTask(final String tgl1, final String tgl2) {
        Log.d("internal_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_LOGOPERATOR_ALL + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("internal_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    OperatorResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), OperatorResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getOperators().length != 0) {

                        if (total != null) {
                            total.setText("Total Laporan Operator: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getOperators().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            String name = klienResponse.getOperators()[i].getNama();
                            for (int j=0; j < klienResponse.getOperators()[i].getStat().length; j++) {
                                klienResponse.getOperators()[i].getStat()[j].setNama(name);
                                taskArrayList.add(klienResponse.getOperators()[i].getStat()[j]);
//                                db.addOperator(klienResponse.getOperators()[i].getStat()[j]);
                                adapter.notifyItemChanged(taskArrayList.size() - 1);
                            }
                        }

                        insertdb = new InsertDb().execute("");

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

    private class InsertDb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            for (int i = 0 ; i < taskArrayList.size(); i++) {
                db.addOperator(taskArrayList.get(i));
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            export.setEnabled(true);
            export.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.bg_green));

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}

    }
}
