package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.HistoriDendaAdapter;
import com.tamboraagungmakmur.winwin.Adapter.HistoriPembayaranAdapter;
import com.tamboraagungmakmur.winwin.Adapter.HistoriPerpanjanganAdapter;
import com.tamboraagungmakmur.winwin.Model.Denda1;
import com.tamboraagungmakmur.winwin.Model.Pembayaran1;
import com.tamboraagungmakmur.winwin.Model.PengajuanDetail;
import com.tamboraagungmakmur.winwin.Model.PengajuanDetailData;
import com.tamboraagungmakmur.winwin.Model.Perpanjangan2;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/12/2017.
 */

public class DetailPeng5Fragment extends Fragment {

    private View view;
    private Context context;

    private String id, idKlien;
    private boolean isDendaAktif = false;
    private boolean isDebtCollector = false;
    private boolean isCollection = false;
    private boolean isBadDebt = false;

    private ArrayList<Pembayaran1> pembayaran1ArrayList = new ArrayList<>();
    private ArrayList<Perpanjangan2> perpanjangan2ArrayList = new ArrayList<>();
    private ArrayList<Denda1> denda1ArrayList = new ArrayList<>();
    private RecyclerView rvPembayaran, rvPencairan, rvDenda;
    private LinearLayoutManager layoutManager1, layoutManager2, layoutManager3;
    private HistoriPembayaranAdapter adapter;
    private HistoriPerpanjanganAdapter adapter1;
    private HistoriDendaAdapter adapter2;

    private Button addPembayaran, addPerpanjangan, addDenda, debtcollector, manajemendata, collection, baddebt, baddebt1, coll, bayarseb, btLenyap;
    private TextView text2, text3, text4, text5, text6, text7, text8, text18, text19;
    private RelativeLayout rel1;

    private final static String TAG = "DETAIL_PENG5";
    private RequestQueue requestQueue;

    private static PengajuanDetail pengdet;

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
            view = inflater.inflate(R.layout.fragment_infopeng5, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();
        requestQueue = Volley.newRequestQueue(context);
        initView();

        id = getArguments().getString("id");
        idKlien = getArguments().getString("id_klien");

        Intent intent = new Intent("title");
        intent.putExtra("message", "Detail Pengajuan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        rvPembayaran = (RecyclerView) view.findViewById(R.id.rv_pembayaran);
        layoutManager1 = new LinearLayoutManager(view.getContext());
        adapter = new HistoriPembayaranAdapter(pembayaran1ArrayList);
        rvPembayaran.setLayoutManager(layoutManager1);
        rvPembayaran.setAdapter(adapter);

        rvPencairan = (RecyclerView) view.findViewById(R.id.rv_pencairan);
        layoutManager2 = new LinearLayoutManager(view.getContext());
        adapter1 = new HistoriPerpanjanganAdapter(perpanjangan2ArrayList);
        rvPencairan.setLayoutManager(layoutManager2);
        rvPencairan.setAdapter(adapter1);

        rvDenda = (RecyclerView) view.findViewById(R.id.rv_denda);
        layoutManager3 = new LinearLayoutManager(view.getContext());
        adapter2 = new HistoriDendaAdapter(denda1ArrayList);
        rvDenda.setLayoutManager(layoutManager3);
        rvDenda.setAdapter(adapter2);

        addPembayaran = (Button) view.findViewById(R.id.add_pembayaran);
        addPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PembayaranAddActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        SessionManager sessionManager = new SessionManager(context);
        if (!sessionManager.isPELUNASAN()) {
            addPembayaran.setVisibility(View.INVISIBLE);
        } else {
            addPembayaran.setVisibility(View.VISIBLE);
        }

        addPerpanjangan = (Button) view.findViewById(R.id.add_perpanjangan);
        addPerpanjangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PerpanjanganAddActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        if (!sessionManager.isCOLLECTION()) {
            addPerpanjangan.setVisibility(View.INVISIBLE);
        } else {
            addPerpanjangan.setVisibility(View.VISIBLE);
        }

        addDenda = (Button) view.findViewById(R.id.add_denda);
        addDenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), DendaAddActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        if (!sessionManager.isDENDA()) {
            addDenda.setVisibility(View.INVISIBLE);
        } else {
            addDenda.setVisibility(View.VISIBLE);
        }

        debtcollector = (Button) view.findViewById(R.id.debtcollctor);
        manajemendata = (Button) view.findViewById(R.id.manajemendata);
        collection = (Button) view.findViewById(R.id.collection);
        baddebt = (Button) view.findViewById(R.id.baddebt);
        coll = (Button) view.findViewById(R.id.coll);
        bayarseb = (Button) view.findViewById(R.id.bayarseb);
        btLenyap = (Button) view.findViewById(R.id.btLenyap);

        if (!sessionManager.isCOLLECTION()) {
            manajemendata.setVisibility(View.INVISIBLE);
            debtcollector.setVisibility(View.INVISIBLE);
            collection.setVisibility(View.INVISIBLE);
            baddebt.setVisibility(View.INVISIBLE);
        } else {
            manajemendata.setVisibility(View.VISIBLE);
            debtcollector.setVisibility(View.VISIBLE);
            collection.setVisibility(View.VISIBLE);
            baddebt.setVisibility(View.VISIBLE);
        }

        manajemendata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), DetPeng5aActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("denda", isDendaAktif);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        debtcollector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), DetPeng5bActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                intent.putExtra("denda", isDebtCollector);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                startActivity(intent);
            }
        });

        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), DetPeng5cActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                intent.putExtra("denda", isCollection);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                startActivity(intent);
            }
        });

        baddebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), DetPeng5dActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                intent.putExtra("denda", isBadDebt);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                startActivity(intent);
            }
        });

        btLenyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SetLenyapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                intent.putExtra("denda", isBadDebt);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                startActivity(intent);
            }
        });

        coll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), DetPeng5eActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                intent.putExtra("denda", isBadDebt);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                startActivity(intent);
            }
        });

        bayarseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), DetPeng5fActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                intent.putExtra("denda", isBadDebt);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                startActivity(intent);
            }
        });

        getPengajuanDetail();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("tahap5a"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver2,
                new IntentFilter("set_lenyap"));

        return view;
    }

    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (pembayaran1ArrayList != null) {
                pembayaran1ArrayList.clear();
            } else {
                pembayaran1ArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            if (perpanjangan2ArrayList != null) {
                perpanjangan2ArrayList.clear();
            } else {
                perpanjangan2ArrayList = new ArrayList<>();
            }
            adapter1.notifyDataSetChanged();
            if (denda1ArrayList != null) {
                denda1ArrayList.clear();
            } else {
                denda1ArrayList = new ArrayList<>();
            }
            adapter2.notifyDataSetChanged();
            getPengajuanDetail();
        }
    };

    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (pembayaran1ArrayList != null) {
                pembayaran1ArrayList.clear();
            } else {
                pembayaran1ArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            if (perpanjangan2ArrayList != null) {
                perpanjangan2ArrayList.clear();
            } else {
                perpanjangan2ArrayList = new ArrayList<>();
            }
            adapter1.notifyDataSetChanged();
            if (denda1ArrayList != null) {
                denda1ArrayList.clear();
            } else {
                denda1ArrayList = new ArrayList<>();
            }
            adapter2.notifyDataSetChanged();
            getPengajuanDetail();
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver2);
        super.onDestroy();
    }

    private void initView() {
        text2 = (TextView) view.findViewById(R.id.text2);
        text3 = (TextView) view.findViewById(R.id.text3);
        text4 = (TextView) view.findViewById(R.id.text4);
        text5 = (TextView) view.findViewById(R.id.text5);
        text6 = (TextView) view.findViewById(R.id.text6);
        text7 = (TextView) view.findViewById(R.id.text7);
        text8 = (TextView) view.findViewById(R.id.text8);
        text18 = (TextView) view.findViewById(R.id.text18);
        text19 = (TextView) view.findViewById(R.id.text19);

        rel1 = (RelativeLayout) view.findViewById(R.id.rel1);
        baddebt1 = (Button) view.findViewById(R.id.baddebt1);
    }

    private void getPengajuanDetail() {
        Log.d("klien_all", "ok");

        final SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanDetail pengajuanDetail = new Gson().fromJson(jsonObject1.toString(), PengajuanDetail.class);
                    pengdet = pengajuanDetail;

                    PengajuanDetailData pengajuanDetailData = pengajuanDetail.getData();
                    text2.setText("Jumlah Bayar: " + new FormatPrice().format(pengajuanDetailData.getBase_bill()));


                    int total = 0;
                    for (int i = 0; i < pengajuanDetailData.getPembayaran().length; i++) {
                        total += Integer.parseInt(pengajuanDetailData.getPembayaran()[i].getAmount());
                        pembayaran1ArrayList.add(pengajuanDetailData.getPembayaran()[i]);
                        adapter.notifyItemChanged(pembayaran1ArrayList.size() - 1);
                    }
                    for (int i = 0; i < pengajuanDetailData.getPerpanjangan().length; i++) {
                        perpanjangan2ArrayList.add(pengajuanDetailData.getPerpanjangan()[i]);
                        adapter1.notifyItemChanged(perpanjangan2ArrayList.size() - 1);
                    }
                    for (int i = 0; i < pengajuanDetailData.getDenda().length; i++) {
                        denda1ArrayList.add(pengajuanDetailData.getDenda()[i]);
                        adapter2.notifyItemChanged(denda1ArrayList.size() - 1);
                    }


                    if (pengajuanDetailData.getPencairan().length > 0) {
                        text18.setText("Tanggal Transfer: " + pengajuanDetailData.getPencairan()[0].getCreated_at());
                        text19.setText("Jumlah Transfer: " + new FormatPrice().format(pengajuanDetailData.getPencairan()[0].getAmount()));
                    }

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format.parse(pengajuanDetailData.getDue_date());
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    start.setTime(date);
                    Date startDate = start.getTime();
                    Date endDate = end.getTime();
                    long startTime = startDate.getTime();
                    long endTime = endDate.getTime();
                    long diffTime = endTime - startTime;
                    long diffDays = diffTime / (1000 * 60 * 60 * 24);

                    if (diffDays > 30) {
                        addPerpanjangan.setVisibility(View.INVISIBLE);
                    } else {
                        if (!sessionManager.isCOLLECTION()) {
                            addPerpanjangan.setVisibility(View.INVISIBLE);
                        } else {
                            addPerpanjangan.setVisibility(View.VISIBLE);
                        }
                    }

                    text3.setText("Telah Dibayar: " + new FormatPrice().format(Integer.toString(total)));
                    text4.setText("Jatuh Tempo: " + pengajuanDetailData.getDue_date());
                    text5.setText("Status Lunas: " + (pengajuanDetailData.is_lunas() ? "Lunas" : "Belum Lunas"));
                    text6.setText("Status denda (Jika telah melewati jatuh tempo): " + (pengajuanDetailData.is_denda_aktif() ? "Tidak Aktif" : "Aktif"));
                    text7.setText("Permanent Collection? " + (pengajuanDetailData.is_collection() ? "Ya" : "Tidak"));
                    text8.setText("Masuk Debt Collector? " + (pengajuanDetailData.is_debtcollector() ? "Ya" : "Tidak"));

                    isDendaAktif = pengajuanDetailData.is_denda_aktif();
                    isDebtCollector = pengajuanDetailData.is_debtcollector();
                    isCollection = pengajuanDetailData.is_collection();
                    isBadDebt = pengajuanDetailData.is_baddebt();

                    if (isBadDebt) {
                        rel1.setVisibility(View.GONE);
//                        addDenda.setVisibility(View.INVISIBLE);
//                        addPembayaran.setVisibility(View.INVISIBLE);
//                        addPerpanjangan.setVisibility(View.INVISIBLE);
                        baddebt1.setVisibility(View.VISIBLE);
                        coll.setVisibility(View.VISIBLE);
                        bayarseb.setVisibility(View.VISIBLE);


                    } else {
                        rel1.setVisibility(View.VISIBLE);
//                        addDenda.setVisibility(View.INVISIBLE);
//                        addPembayaran.setVisibility(View.INVISIBLE);
//                        addPerpanjangan.setVisibility(View.INVISIBLE);
                        baddebt1.setVisibility(View.GONE);
                        coll.setVisibility(View.GONE);
                        bayarseb.setVisibility(View.GONE);
                    }

//                    if (pengajuanDetail.getData().is_batal()) {
//                        rel1.setVisibility(View.GONE);
//                        addDenda.setVisibility(View.INVISIBLE);
//                        addPembayaran.setVisibility(View.INVISIBLE);
//                        addPerpanjangan.setVisibility(View.INVISIBLE);
//                    } else {
//                        rel1.setVisibility(View.VISIBLE);
//                        addDenda.setVisibility(View.VISIBLE);
//                        addPembayaran.setVisibility(View.VISIBLE);
//                        addPerpanjangan.setVisibility(View.VISIBLE);
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    getActivity().finish();
                } catch (ParseException e) {
                    e.printStackTrace();
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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
//
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
