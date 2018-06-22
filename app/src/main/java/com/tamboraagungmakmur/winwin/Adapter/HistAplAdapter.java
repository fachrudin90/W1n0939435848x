package com.tamboraagungmakmur.winwin.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.LoginActivity;
import com.tamboraagungmakmur.winwin.Model.HistoriTahapan;
import com.tamboraagungmakmur.winwin.Model.HistoriTahapanResponse;
import com.tamboraagungmakmur.winwin.Model.Notes;
import com.tamboraagungmakmur.winwin.Model.PengajuanDetail;
import com.tamboraagungmakmur.winwin.Model.PengajuanDetailData;
import com.tamboraagungmakmur.winwin.R;
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
 * Created by innan on 9/27/2017.
 */

public class HistAplAdapter extends RecyclerView.Adapter<HistAplAdapter.HistAplViewHolder> {

    private ArrayList<PengajuanDetailData> pengajuanDetailDataArrayList;
    private Context context;
    private Activity activity;

    private LinearLayoutManager linearLayoutManager;
    private ArrayList<HistoriTahapan> historiTahapanArrayList = new ArrayList<>();
    HistoriTahapanAdapter adapter;

    private LinearLayoutManager linearLayoutManager1;
    private ArrayList<Notes> notesArrayList = new ArrayList<>();
    HistoriTahapan1Adapter adapter1;

    public HistAplAdapter(ArrayList<PengajuanDetailData> pengajuanDetailDataArrayList) {
        this.pengajuanDetailDataArrayList = pengajuanDetailDataArrayList;
    }

    @Override
    public HistAplViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_histapl, parent, false);
        HistAplViewHolder holder = new HistAplViewHolder(view);
        context = parent.getContext();
        activity = (Activity)parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(HistAplViewHolder holder, int position) {
        PengajuanDetailData pengajuanDetailData = pengajuanDetailDataArrayList.get(position);

        holder.text1.setText("Pengajuan Ke " + (pengajuanDetailDataArrayList.size()-position));
        holder.text5.setText(new FormatPrice().format(pengajuanDetailData.getAmount()));
        holder.text3.setText(pengajuanDetailData.getDuration() + " Hari");
        holder.text7.setText(pengajuanDetailData.getRate() + "%");
        holder.text9.setText(new FormatPrice().format(pengajuanDetailData.getBase_bill()));
        holder.text11.setText(pengajuanDetailData.getDue_date());

        holder.input1a.setText(pengajuanDetailData.getTujuan_pinjam());
        holder.input2a.setText(pengajuanDetailData.getStatus() + " / " + pengajuanDetailData.getTahap());

        linearLayoutManager = new LinearLayoutManager(context);
        holder.rv_hist.setLayoutManager(linearLayoutManager);
        adapter = new HistoriTahapanAdapter(historiTahapanArrayList);
        holder.rv_hist.setAdapter(adapter);

        linearLayoutManager1 = new LinearLayoutManager(context);
        holder.rv_note.setLayoutManager(linearLayoutManager1);
        adapter1 = new HistoriTahapan1Adapter(notesArrayList);
        holder.rv_note.setAdapter(adapter1);

        getHistori(pengajuanDetailData.getId());
        getNotes(pengajuanDetailData.getId());

    }

    @Override
    public int getItemCount() {
        return pengajuanDetailDataArrayList.size();
    }

    public class HistAplViewHolder extends RecyclerView.ViewHolder {

        private TextView text1, text5, text3, text7, text9, text11;
        private TextInputEditText input1a, input2a;
        private RecyclerView rv_hist, rv_note;

        public HistAplViewHolder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(R.id.text1);
            text5 = (TextView) itemView.findViewById(R.id.text5);
            text3 = (TextView) itemView.findViewById(R.id.text3);
            text7 = (TextView) itemView.findViewById(R.id.text7);
            text9 = (TextView) itemView.findViewById(R.id.text9);
            text11 = (TextView) itemView.findViewById(R.id.text11);

            input1a = (TextInputEditText) itemView.findViewById(R.id.input1a);
            input2a = (TextInputEditText) itemView.findViewById(R.id.input2a);

            rv_hist = (RecyclerView) itemView.findViewById(R.id.rv_hist);
            rv_note = (RecyclerView) itemView.findViewById(R.id.rv_note);

        }
    }

    private void getHistori(final String idpeng) {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_HISTORI + "/" + idpeng + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
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
                    GlobalToast.ShowToast(context, context.getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    activity.finish();
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
                    GlobalToast.ShowToast(context, context.getString(R.string.session_expired));
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    activity.finish();
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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void getNotes(final String idpeng) {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_DETAIL + "/" + idpeng + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanDetail pengajuanDetail = new Gson().fromJson(jsonObject1.toString(), PengajuanDetail.class);

                        for (int i=0; i<pengajuanDetail.getData().getNotes().length; i++) {
                            notesArrayList.add(pengajuanDetail.getData().getNotes()[i]);
                            adapter1.notifyItemChanged(notesArrayList.size()-1);
                        }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, context.getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    activity.finish();
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
                    GlobalToast.ShowToast(context, context.getString(R.string.session_expired));
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    activity.finish();
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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }
}
