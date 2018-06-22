package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import com.tamboraagungmakmur.winwin.Adapter.HistoriTahapanAdapter;
import com.tamboraagungmakmur.winwin.Model.HistoriTahapan;
import com.tamboraagungmakmur.winwin.Model.HistoriTahapanResponse;
import com.tamboraagungmakmur.winwin.Model.PengajuanDetail;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/11/2017.
 */

public class DetailPeng3Fragment extends Fragment {

    private View view;
    private Context context;

    private ArrayList<HistoriTahapan> historiTahapanArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private HistoriTahapanAdapter adapter;

    private Button prev, tolak, setuju;
    private TextView jumlah, durasi, bunga, totalbayar, jatuhtempo;
    private Button txSetuju, txTolak;
    private TextInputEditText input1a, input2a;
    private TextInputLayout input2;
    private TextView text15, text16;

    private String id, idKlien;

    private final static String TAG = "DETAIL_PENG3";
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
            view = inflater.inflate(R.layout.fragment_infopeng3, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();

        requestQueue = Volley.newRequestQueue(context);

        id = getArguments().getString("id");
        idKlien = getArguments().getString("id_klien");

        Intent intent = new Intent("title");
        intent.putExtra("message", "Detail Pengajuan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);


        recyclerView = (RecyclerView) view.findViewById(R.id.rv1);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        adapter = new HistoriTahapanAdapter(historiTahapanArrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        prev = (Button) view.findViewById(R.id.prev);
        tolak = (Button) view.findViewById(R.id.tolak);
        setuju = (Button) view.findViewById(R.id.setuju);

        SessionManager sessionManager = new SessionManager(context);
        if (!sessionManager.isPERSETUJUAN()) {
            tolak.setVisibility(View.INVISIBLE);
            setuju.setVisibility(View.INVISIBLE);
        } else {
            tolak.setVisibility(View.VISIBLE);
            setuju.setVisibility(View.VISIBLE);
        }

        initView();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap2Activity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.putExtra("note", input2a.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        setuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap4Activity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.putExtra("note", input2a.getText().toString());
                intent.putExtra("rekomendasi", "setujui");
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap4bActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.putExtra("note", input2a.getText().toString());
                intent.putExtra("rekomendasi", "tolak");
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        getPengajuanDetail();
        getHistori();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("tahap4b"));

        return view;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (historiTahapanArrayList != null) {
                historiTahapanArrayList.clear();
            } else {
                historiTahapanArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            getPengajuanDetail();
            getHistori();
        }
    };

    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void initView() {
        jumlah = (TextView) view.findViewById(R.id.text5);
        durasi = (TextView) view.findViewById(R.id.text3);
        bunga = (TextView) view.findViewById(R.id.text7);
        totalbayar = (TextView) view.findViewById(R.id.text9);
        jatuhtempo = (TextView) view.findViewById(R.id.text11);
        text15 = (TextView) view.findViewById(R.id.text15);
        text16 = (TextView) view.findViewById(R.id.text16);

        txSetuju = (Button) view.findViewById(R.id.tx_setuju);
        txTolak = (Button) view.findViewById(R.id.tx_tolak);
        input1a = (TextInputEditText) view.findViewById(R.id.input1a);
        input2a = (TextInputEditText) view.findViewById(R.id.input2a);
        input2 = (TextInputLayout) view.findViewById(R.id.input2);
    }

    private void getPengajuanDetail() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanDetail pengajuanDetail = new Gson().fromJson(jsonObject1.toString(), PengajuanDetail.class);
                    pengdet = pengajuanDetail;

                    if (pengajuanDetail.getData().is_tolak() || pengajuanDetail.getData().is_batal()) {
                        tolak.setVisibility(View.GONE);
                        setuju.setVisibility(View.GONE);
                        prev.setVisibility(View.GONE);
                        text15.setVisibility(View.GONE);
                        input2.setVisibility(View.GONE);
                        text16.setVisibility(View.VISIBLE);
                        txTolak.setVisibility(View.VISIBLE);
                    }

                    jumlah.setText(new FormatPrice().format(pengajuanDetail.getData().getAmount()));
                    durasi.setText(pengajuanDetail.getData().getDuration() + " HARI");
                    totalbayar.setText(new FormatPrice().format(pengajuanDetail.getData().getBase_bill()));
                    jatuhtempo.setText(pengajuanDetail.getData().getDue_date());
                    input1a.setText(pengajuanDetail.getData().getTujuan_pinjam());
                    bunga.setText(pengajuanDetail.getData().getRate() + "%");

                    txSetuju.setText(pengajuanDetail.getData().getRekomendasi());


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

    private void getHistori() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_HISTORI + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    HistoriTahapanResponse pengajuanDetail = new Gson().fromJson(jsonObject1.toString(), HistoriTahapanResponse.class);

                    if (pengajuanDetail.getData().length > 0) {
                        for (int i=0; i<pengajuanDetail.getData().length; i++) {
                            historiTahapanArrayList.add(pengajuanDetail.getData()[i]);
                            adapter.notifyItemChanged(historiTahapanArrayList.size()-1);
                        }
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
