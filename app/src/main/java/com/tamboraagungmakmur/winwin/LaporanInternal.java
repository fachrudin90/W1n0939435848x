package com.tamboraagungmakmur.winwin;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.tamboraagungmakmur.winwin.Adapter.InternalAdapter;
import com.tamboraagungmakmur.winwin.Model.Internal;
import com.tamboraagungmakmur.winwin.Model.InternalResponse;
import com.tamboraagungmakmur.winwin.Model.InternalResume1;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.DatabaseHandler;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.UnsafeOkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;


/**
 * A simple {@link Fragment} subclass.
 */
public class LaporanInternal extends Fragment {


    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
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

    private final static String TAG = "LAPORAN_INTERNAL";
    private RequestQueue requestQueue;

    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private ArrayList<Internal> taskArrayList = new ArrayList<>();

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;
    private String date1, date2, date3;

    private View view;
    private Context context;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private Button export;
    private ProgressBar progressBar1;
    private DatabaseHandler db;
    private SQLiteToExcel sqliteToExcel;

    private String tgl1, tgl2, tahun;

    private Call<InternalResponse> call;
    private AsyncTask<String, Void, String> insertdb;


    public LaporanInternal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_laporan_internal1, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
        requestQueue = Volley.newRequestQueue(mActivity);

        db = new DatabaseHandler(mActivity);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        String currentDateandTime = sdf.format(currentTime);
        String currentDateandTime1 = sdf1.format(currentTime);
        tgl1 = currentDateandTime;
        tgl2 = currentDateandTime;
        tahun = currentDateandTime1;

        Intent intent = new Intent("title");
        intent.putExtra("message", "Laporan Internal");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        total.setText("Total Laporan Internal: 0");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressbar1);

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
        adapter = new InternalAdapter(taskArrayList);
        rvList.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        export = (Button) view.findViewById(R.id.export);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    export.setVisibility(View.INVISIBLE);
                    progressBar1.setVisibility(View.VISIBLE);
//                List<Internal> internals = db.getAllInternals();
//                Log.d("internals", internals.get(0).getKlien());
                sqliteToExcel = new SQLiteToExcel(mActivity, "winwin");
                ArrayList<String> tables = new ArrayList<String>();
                tables.add("internal");
                tables.add("internal_resume1");
                sqliteToExcel.exportSpecificTables(tables, "internal_"+txTgl1.getText().toString()+"_"+txTgl2.getText().toString()+".xls", new SQLiteToExcel.ExportListener() {
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
//        getTask(date1, date1);
        saveFile(date1, date1);

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
                                tgl1 = tgl;
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
                                tgl2 = tgl;
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
//                getTask(date2, date3);
                saveFile(date2, date3);
                break;
            case R.id.txPrint:
                break;
        }
    }

    private void getTask(final String tgl1, final String tgl2) {
        Log.d("internal_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_LOGINTERNAL_ALL + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("internal_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    InternalResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), InternalResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Laporan Internal: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            taskArrayList.add(klienResponse.getData()[i]);
                            db.addInternal(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(taskArrayList.size() - 1);
                        }
                        InternalResume1 internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Profit");
                        internalResume1.setKol2(klienResponse.getResume().getProfit());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Profit Unpaid");
                        internalResume1.setKol2(klienResponse.getResume().getProfit_unpaid());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Additional Profit");
                        internalResume1.setKol2(klienResponse.getResume().getProfit_additional());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("INCOME");
                        internalResume1.setKol2(klienResponse.getResume().getIncome());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Fees");
                        internalResume1.setKol2(klienResponse.getResume().getFees());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Loss / Bad Debt");
                        internalResume1.setKol2(klienResponse.getResume().getLoss());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Net Income");
                        internalResume1.setKol2(klienResponse.getResume().getNetincome());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Turn Over");
                        internalResume1.setKol2(klienResponse.getResume().getTurn_over());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("ROI");
                        internalResume1.setKol2(klienResponse.getResume().getRoi());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("New Customer");
                        internalResume1.setKol2(klienResponse.getResume().getNewcust());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Bad Debt Customer");
                        internalResume1.setKol2(klienResponse.getResume().getNewcust_default());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol2("Periode " + tgl1 + " - " + tgl2);
                        internalResume1.setKol3("Periode " + tahun);
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total dana klien saat ini");
                        internalResume1.setKol2(klienResponse.getResume().getDana_aktif());
                        internalResume1.setKol3(klienResponse.getResume().getDana_aktif_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Kewajiban pengembalian oleh klien saat ini (pokok + bunga)");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_kembali());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_kembali_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Denda klien saat ini");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_denda());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_denda_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Pembayaran sebagian yang diterima dari klien saat ini");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_sebagian());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_sebagian_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Sisa pengembalian yang harus dibayar oleh klien saat ini");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_sisa());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_sisa_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Pinjaman Masuk");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_masuk());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_masuk_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Pinjaman Aktif & Sudah cair");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_setujui());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_setujui_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Pinjaman Ditolak");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_tolak());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_tolak_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Pinjaman Lunas");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_lunas());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_lunas_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Pinjaman JT");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_jt());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_jt_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Pinjaman JT & Bayar Sebagian");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_bayar_sebagian());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_bayar_sebagian_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Pinjaman JT & Belum Bayar");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_nunggak());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_nunggak_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Dana cair");
                        internalResume1.setKol2(klienResponse.getResume().getDana_keluar());
                        internalResume1.setKol3(klienResponse.getResume().getDana_keluar_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Dana telah kembali");
                        internalResume1.setKol2(klienResponse.getResume().getDana_masuk());
                        internalResume1.setKol3(klienResponse.getResume().getDana_masuk_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Dana Perpanjangan");
                        internalResume1.setKol2(klienResponse.getResume().getDana_perpanjang());
                        internalResume1.setKol3(klienResponse.getResume().getDana_perpanjang_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Turn Over");
                        internalResume1.setKol2(klienResponse.getResume().getTurn_over());
                        internalResume1.setKol3(klienResponse.getResume().getTurn_over_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Bruto Income");
                        internalResume1.setKol2(klienResponse.getResume().getBruto_income());
                        internalResume1.setKol3(klienResponse.getResume().getBruto_income_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Net Income");
                        internalResume1.setKol2(klienResponse.getResume().getNet_income());
                        internalResume1.setKol3(klienResponse.getResume().getNet_income_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Dana cair (sudah lunas)");
                        internalResume1.setKol2(klienResponse.getResume().getDana_keluar_lunas());
                        internalResume1.setKol3(klienResponse.getResume().getDana_keluar_lunas_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Dana telah kembali (sudah lunas)");
                        internalResume1.setKol2(klienResponse.getResume().getDana_masuk_lunas());
                        internalResume1.setKol3(klienResponse.getResume().getDana_masuk_lunas_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Total Dana Perpanjangan (sudah lunas)");
                        internalResume1.setKol2(klienResponse.getResume().getDana_perpanjang_lunas());
                        internalResume1.setKol3(klienResponse.getResume().getDana_perpanjang_lunas_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Turn Over (sudah lunas)");
                        internalResume1.setKol2(klienResponse.getResume().getTurn_over_lunas());
                        internalResume1.setKol3(klienResponse.getResume().getTurn_over_lunas_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Bruto Income (sudah lunas)");
                        internalResume1.setKol2(klienResponse.getResume().getBruto_income_lunas());
                        internalResume1.setKol3(klienResponse.getResume().getBruto_income_lunas_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Net Income (sudah lunas)");
                        internalResume1.setKol2(klienResponse.getResume().getNet_income_lunas());
                        internalResume1.setKol3(klienResponse.getResume().getNet_income_lunas_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah New customer disetujui dan cair");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_setujui_new());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_setujui_new_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah New customer disetujui dan batal");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_batal_new());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_batal_new_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah Repeat customer disetujui dan cair");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_setujui_repeat());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_setujui_repeat_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah Repeat customer disetujui dan batal");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_batal_repeat());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_batal_repeat_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah dibatalkan (batal pengajuan pinjaman)");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_batal());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_batal_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah Batal transfer");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_batal_cair());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_batal_cair_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah klien perpanjangan pinjaman");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_perpanjangan());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_perpanjangan_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah klien new customer perpanjangan pinjaman");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_perpanjangan_new());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_perpanjangan_new_tahun());
                        db.addInternalResume1(internalResume1);

                        internalResume1 = new InternalResume1();
                        internalResume1.setKol1("Jumlah klien repeat cutomer perpanjangan pinjaman");
                        internalResume1.setKol2(klienResponse.getResume().getTotal_perpanjangan_repeat());
                        internalResume1.setKol3(klienResponse.getResume().getTotal_perpanjangan_repeat_tahun());
                        db.addInternalResume1(internalResume1);


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
        Call<InternalResponse> upload(@Url String url);
    }

    private void saveFile(final String tgl1, final String tgl2) {

        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);


        SessionManager sessionManager = new SessionManager(mActivity);

        call = service.upload(AppConf.URL_LOGINTERNAL_ALL + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionManager.getSessionId());
        call.enqueue(new Callback<InternalResponse>() {

            @Override
            public void onResponse(Call<InternalResponse> call, retrofit2.Response<InternalResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                InternalResponse klienResponse = response.body();

                if (klienResponse.getData().length != 0) {

                    if (total != null) {
                        total.setText("Total Laporan Internal: " + klienResponse.getCount());
                    }
                    for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                        taskArrayList.add(klienResponse.getData()[i]);
//                        db.addInternal(klienResponse.getData()[i]);
                        adapter.notifyItemInserted(taskArrayList.size() - 1);
                    }
//                    adapter.notifyDataSetChanged();
                    InternalResume1 internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Profit");
                    internalResume1.setKol2(klienResponse.getResume().getProfit());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Profit Unpaid");
                    internalResume1.setKol2(klienResponse.getResume().getProfit_unpaid());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Additional Profit");
                    internalResume1.setKol2(klienResponse.getResume().getProfit_additional());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("INCOME");
                    internalResume1.setKol2(klienResponse.getResume().getIncome());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Fees");
                    internalResume1.setKol2(klienResponse.getResume().getFees());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Loss / Bad Debt");
                    internalResume1.setKol2(klienResponse.getResume().getLoss());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Net Income");
                    internalResume1.setKol2(klienResponse.getResume().getNetincome());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Turn Over");
                    internalResume1.setKol2(klienResponse.getResume().getTurn_over());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("ROI");
                    internalResume1.setKol2(klienResponse.getResume().getRoi());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("New Customer");
                    internalResume1.setKol2(klienResponse.getResume().getNewcust());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Bad Debt Customer");
                    internalResume1.setKol2(klienResponse.getResume().getNewcust_default());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol2("Periode " + tgl1 + " - " + tgl2);
                    internalResume1.setKol3("Periode " + tahun);
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total dana klien saat ini");
                    internalResume1.setKol2(klienResponse.getResume().getDana_aktif());
                    internalResume1.setKol3(klienResponse.getResume().getDana_aktif_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Kewajiban pengembalian oleh klien saat ini (pokok + bunga)");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_kembali());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_kembali_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Denda klien saat ini");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_denda());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_denda_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Pembayaran sebagian yang diterima dari klien saat ini");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_sebagian());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_sebagian_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Sisa pengembalian yang harus dibayar oleh klien saat ini");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_sisa());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_sisa_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Pinjaman Masuk");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_masuk());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_masuk_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Pinjaman Aktif & Sudah cair");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_setujui());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_setujui_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Pinjaman Ditolak");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_tolak());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_tolak_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Pinjaman Lunas");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_lunas());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_lunas_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Pinjaman JT");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_jt());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_jt_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Pinjaman JT & Bayar Sebagian");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_bayar_sebagian());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_bayar_sebagian_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Pinjaman JT & Belum Bayar");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_nunggak());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_nunggak_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Dana cair");
                    internalResume1.setKol2(klienResponse.getResume().getDana_keluar());
                    internalResume1.setKol3(klienResponse.getResume().getDana_keluar_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Dana telah kembali");
                    internalResume1.setKol2(klienResponse.getResume().getDana_masuk());
                    internalResume1.setKol3(klienResponse.getResume().getDana_masuk_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Dana Perpanjangan");
                    internalResume1.setKol2(klienResponse.getResume().getDana_perpanjang());
                    internalResume1.setKol3(klienResponse.getResume().getDana_perpanjang_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Turn Over");
                    internalResume1.setKol2(klienResponse.getResume().getTurn_over());
                    internalResume1.setKol3(klienResponse.getResume().getTurn_over_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Bruto Income");
                    internalResume1.setKol2(klienResponse.getResume().getBruto_income());
                    internalResume1.setKol3(klienResponse.getResume().getBruto_income_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Net Income");
                    internalResume1.setKol2(klienResponse.getResume().getNet_income());
                    internalResume1.setKol3(klienResponse.getResume().getNet_income_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Dana cair (sudah lunas)");
                    internalResume1.setKol2(klienResponse.getResume().getDana_keluar_lunas());
                    internalResume1.setKol3(klienResponse.getResume().getDana_keluar_lunas_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Dana telah kembali (sudah lunas)");
                    internalResume1.setKol2(klienResponse.getResume().getDana_masuk_lunas());
                    internalResume1.setKol3(klienResponse.getResume().getDana_masuk_lunas_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Total Dana Perpanjangan (sudah lunas)");
                    internalResume1.setKol2(klienResponse.getResume().getDana_perpanjang_lunas());
                    internalResume1.setKol3(klienResponse.getResume().getDana_perpanjang_lunas_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Turn Over (sudah lunas)");
                    internalResume1.setKol2(klienResponse.getResume().getTurn_over_lunas());
                    internalResume1.setKol3(klienResponse.getResume().getTurn_over_lunas_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Bruto Income (sudah lunas)");
                    internalResume1.setKol2(klienResponse.getResume().getBruto_income_lunas());
                    internalResume1.setKol3(klienResponse.getResume().getBruto_income_lunas_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Net Income (sudah lunas)");
                    internalResume1.setKol2(klienResponse.getResume().getNet_income_lunas());
                    internalResume1.setKol3(klienResponse.getResume().getNet_income_lunas_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah New customer disetujui dan cair");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_setujui_new());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_setujui_new_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah New customer disetujui dan batal");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_batal_new());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_batal_new_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah Repeat customer disetujui dan cair");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_setujui_repeat());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_setujui_repeat_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah Repeat customer disetujui dan batal");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_batal_repeat());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_batal_repeat_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah dibatalkan (batal pengajuan pinjaman)");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_batal());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_batal_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah Batal transfer");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_batal_cair());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_batal_cair_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah klien perpanjangan pinjaman");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_perpanjangan());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_perpanjangan_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah klien new customer perpanjangan pinjaman");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_perpanjangan_new());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_perpanjangan_new_tahun());
                    db.addInternalResume1(internalResume1);

                    internalResume1 = new InternalResume1();
                    internalResume1.setKol1("Jumlah klien repeat cutomer perpanjangan pinjaman");
                    internalResume1.setKol2(klienResponse.getResume().getTotal_perpanjangan_repeat());
                    internalResume1.setKol3(klienResponse.getResume().getTotal_perpanjangan_repeat_tahun());
                    db.addInternalResume1(internalResume1);


                    insertdb = new InsertDb().execute("");

                }


            }

            @Override
            public void onFailure(Call<InternalResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                if (call.isCanceled()) {
                    Log.e(TAG, "request was cancelled");
                } else {
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
            }
        });
    }

    private class InsertDb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            for (int i = 0 ; i < taskArrayList.size(); i++) {
                db.addInternal(taskArrayList.get(i));
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

