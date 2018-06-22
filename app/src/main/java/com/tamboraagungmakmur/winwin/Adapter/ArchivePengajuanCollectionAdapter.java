package com.tamboraagungmakmur.winwin.Adapter;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.tamboraagungmakmur.winwin.KlienResumeActivity;
import com.tamboraagungmakmur.winwin.LoginActivity;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Model.Pengajuan;
import com.tamboraagungmakmur.winwin.PengNote1Activity;
import com.tamboraagungmakmur.winwin.PengajuanDetActivity;
import com.tamboraagungmakmur.winwin.PindahTahap5Activity;
import com.tamboraagungmakmur.winwin.PindahTahap5aActivity;
import com.tamboraagungmakmur.winwin.PindahTahap5bActivity;
import com.tamboraagungmakmur.winwin.PindahTahap5cActivity;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 10/20/2017.
 */

public class ArchivePengajuanCollectionAdapter extends RecyclerView.Adapter<ArchivePengajuanCollectionAdapter.MyViewHolder> {


    private ArrayList<Pengajuan> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.btView)
        ImageButton btView;
        @Bind(R.id.btNote)
        ImageButton btNote;
        @Bind(R.id.txTglBuat)
        TextView txTglBuat;
        @Bind(R.id.txNoPeng)
        TextView txNoPeng;
        @Bind(R.id.txKlien)
        TextView txKlien;
        @Bind(R.id.txPinjam)
        TextView txPinjam;
        @Bind(R.id.txDurasi)
        TextView txDurasi;
        @Bind(R.id.txBayar)
        TextView txBayar;
        @Bind(R.id.txJatuhTempo)
        TextView txJatuhTempo;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;
        @Bind(R.id.pg)
        ProgressBar pg;
        @Bind(R.id.no)
        TextView no;
        @Bind(R.id.lin1)
        LinearLayout lin1;
        @Bind(R.id.resume)
        Button resume;
        @Bind(R.id.btJanjBayar)
        ImageButton btJanjBayar;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public ArchivePengajuanCollectionAdapter(Context context, ArrayList<Pengajuan> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pengajuan_collection, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Pengajuan value = dataSet.get(listPosition);

        progressBar = holder.pg;
        if (listPosition == dataSet.size() - 1) {
            if (isLast) {
                holder.pg.setVisibility(View.GONE);
            } else {
                holder.pg.setVisibility(View.VISIBLE);
            }
        } else {
            holder.pg.setVisibility(View.GONE);
        }

        if (value.is_verified()) {
            holder.lin1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorpink));
        } else {
            holder.lin1.setBackgroundColor(Color.WHITE);
        }

        holder.resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KlienResumeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getId_client());
                context.startActivity(intent);
            }
        });

        holder.no.setText("" + (listPosition + 1));
        holder.txTglBuat.setText(value.getCreated_at() + "\nassign to: " + value.getAssign_to());
        holder.txNoPeng.setText(value.getNomor_pengajuan());
        holder.txKlien.setText(value.getNomor_pelanggan() + "\n" + value.getNama_lengkap());
        holder.txPinjam.setText(new FormatPrice().format(value.getAmount()));
        holder.txDurasi.setText(value.getDuration());
//        holder.txBayar.setText(new FormatPrice().format(value.getBase_bill()) + "\nSisa: " + new FormatPrice().format(value.getTotal_bill()));
        String bill = value.getTotal_bill();
        if (bill != null) {
            if (bill.compareTo("0") == 0) {
                bill = value.getBase_bill();
            }
        }
        int bill1 = 0;
        if (bill != null) {
            bill1 = Integer.parseInt(bill);
        }
        String bill2 = value.getTotal_payment();
        int bill3 = 0;
        if (bill2 != null) {
            bill3 = Integer.parseInt(bill2);
        }
        holder.txBayar.setText(new FormatPrice().format(value.getTotal_payment()) + "\nSisa: " + new FormatPrice().format(Integer.toString(bill1 - bill3)));
        holder.txJatuhTempo.setText(value.getDue_date());
        holder.txStatus.setText(value.getStatus() + "\n" + value.getTahap());

        int tahap = 1;
        if (value.getStatus().compareTo("Pengajuan") != 0) {
            if (value.is_lunas()) {
                tahap = 6;
            } else if (value.is_cair()) {
                tahap = 5;
            } else if (value.getTahap().compareTo("Rencana Pencairan") == 0 || value.is_setujui()) {
                tahap = 4;
            } else if (value.getTahap().compareTo("Analisa") == 0 || value.is_tolak()) {
                tahap = 3;
            } else {
                tahap = 2;
            }
        }

        final int tahap1 = tahap;
        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PengajuanDetActivity.class);
                intent.putExtra("id", value.getId());
                intent.putExtra("nama", value.getNama_lengkap());
                intent.putExtra("id_klien", value.getId_client());
                intent.putExtra("tahap", tahap1);
                intent.putExtra("dari", "Collection");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        holder.btNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PengNote1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getId_client());
                context.startActivity(intent);
            }
        });

//        if (value.getPengajuan_janji_bayar_aktif().equals("0")) {
//            holder.btJanjBayar.setVisibility(View.VISIBLE);
//        } else {
//            holder.btJanjBayar.setVisibility(View.INVISIBLE);
//        }

        holder.btJanjBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogJanji(listPosition);

            }
        });

        if ((listPosition >= getItemCount() - 1))
            load();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("loading"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isLast = true;
            progressBar.setVisibility(View.GONE);
        }
    };

    public void load() {

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public void dialogJanji(final int position) {

        final Dialog dialog = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_janji_bayar, null);

        Button btOk = (Button) view.findViewById(R.id.ok);
        Button btCancel = (Button) view.findViewById(R.id.cancel);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveJanji(position, dialog);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        dialog.show();

    }


    private void saveJanji(final int position, final Dialog dialog) {

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Pengajuan value = dataSet.get(position);
        SessionManager sessionManager = new SessionManager(context);

        AndLog.ShowLog("DERRO", AppConf.URL_PENGAJUAN_JANJI_BAYAR + "/" + value.getId() + "?_session=" + sessionManager.getSessionId());

        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_PENGAJUAN_JANJI_BAYAR + "/" + value.getId() + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                Log.d("tahap_2", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse logoutResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

//                    Toast.makeText(context, logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if (!logoutResponse.isError()) {
                        Toast.makeText(context, "Janji bayar telah diaktifkan", Toast.LENGTH_LONG).show();
                        dataSet.get(position).setPengajuan_janji_bayar_aktif("1");
                        notifyItemChanged(position);
                        dialog.dismiss();

                    } else {

                        Toast.makeText(context, "Janji bayar gagal diaktifkan, silahkan coba kembali", Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, context.getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "no connection", Toast.LENGTH_SHORT).show();
                } else {
                    GlobalToast.ShowToast(context, context.getString(R.string.session_expired));
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

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

        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }


}
