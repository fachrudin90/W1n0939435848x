package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.tamboraagungmakmur.winwin.Model.PengajuanDetail;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/9/2017.
 */

public class DetailPeng1Fragment extends Fragment {

    private View view;
    private Button bt1, bt2;
    private String id, idKlien;
    private TextView text12;

    private Context context;
    private TextView jumlah, durasi, bunga, totalbayar, jatuhtempo;
    private TextInputEditText input1a, input2a;

    private final static String TAG = "DETAIL_PENG1";
    private RequestQueue requestQueue;

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
            view = inflater.inflate(R.layout.fragment_infopeng1, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();

        requestQueue = Volley.newRequestQueue(context);

        id = getArguments().getString("id");
        idKlien = getArguments().getString("id_klien");
        initView();


        Intent intent = new Intent("title");
        intent.putExtra("message", "Detail Pengajuan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);


        bt1 = (Button) view.findViewById(R.id.bt1);
        bt2 = (Button) view.findViewById(R.id.bt2);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap2aActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.putExtra("note", input2a.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
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

        getPengajuanDetail();

        return view;
    }

    private void initView() {
        jumlah = (TextView) view.findViewById(R.id.text5);
        durasi = (TextView) view.findViewById(R.id.text3);
        bunga = (TextView) view.findViewById(R.id.text7);
        totalbayar = (TextView) view.findViewById(R.id.text9);
        jatuhtempo = (TextView) view.findViewById(R.id.text11);
        text12 = (TextView) view.findViewById(R.id.text12);
        input1a = (TextInputEditText) view.findViewById(R.id.input1a);
        input2a = (TextInputEditText) view.findViewById(R.id.input2a);
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

                    if (pengajuanDetail.getData().is_batal()) {
                        bt1.setVisibility(View.GONE);
                        bt2.setVisibility(View.GONE);
                        text12.setVisibility(View.GONE);
                        input2a.setVisibility(View.GONE);
                    }
                    jumlah.setText(new FormatPrice().format(pengajuanDetail.getData().getAmount()));
                    durasi.setText(pengajuanDetail.getData().getDuration() + " HARI");
                    totalbayar.setText(new FormatPrice().format(pengajuanDetail.getData().getBase_bill()));
                    jatuhtempo.setText(pengajuanDetail.getData().getDue_date());
                    input1a.setText(pengajuanDetail.getData().getTujuan_pinjam());
                    bunga.setText(pengajuanDetail.getData().getRate() + "%");


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

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
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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
