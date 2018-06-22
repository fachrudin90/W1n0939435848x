package com.tamboraagungmakmur.winwin.Adapter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.KlienResumeActivity;
import com.tamboraagungmakmur.winwin.Model.ArchivePengajuanModel;
import com.tamboraagungmakmur.winwin.Model.Pengajuan;
import com.tamboraagungmakmur.winwin.PengajuanDetActivity;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArchivePengajuanNonAktifAdapter extends RecyclerView.Adapter<ArchivePengajuanNonAktifAdapter.MyViewHolder> {


    private ArrayList<Pengajuan> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.btView)
        ImageButton btView;
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
        @Bind(R.id.resume)
        Button resume;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public ArchivePengajuanNonAktifAdapter(Context context, ArrayList<Pengajuan> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pengajuan1, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Pengajuan value = dataSet.get(listPosition);

        progressBar = holder.pg;
        if (listPosition == dataSet.size()-1) {
            if (isLast) {
                holder.pg.setVisibility(View.GONE);
            } else {
                holder.pg.setVisibility(View.VISIBLE);
            }
        } else {
            holder.pg.setVisibility(View.GONE);
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

        holder.no.setText(""+(listPosition+1));
        holder.txTglBuat.setText(value.getCreated_at() + "\nassign to: " + value.getAssign_to());
        holder.txNoPeng.setText(value.getNomor_pengajuan());
        holder.txKlien.setText(value.getNomor_pelanggan() + "\n" + value.getNama_lengkap());
        holder.txPinjam.setText(new FormatPrice().format(value.getAmount()));
        holder.txDurasi.setText(value.getDuration());
        holder.txBayar.setText(new FormatPrice().format(value.getTotal_bill()));
        holder.txJatuhTempo.setText(value.getDue_date());
        holder.txStatus.setText(value.getStatus());
        holder.lyTable.setBackgroundColor(Color.parseColor("#fff7f9"));

        if (value.is_verified()) {
            holder.lyTable.setBackgroundColor(ContextCompat.getColor(context, R.color.colorpink));
        } else {
            holder.lyTable.setBackgroundColor(Color.WHITE);
        }

        int tahap = 1;
        if (value.getStatus().compareTo("Pengajuan") != 0) {
            if (value.is_lunas()) {
                tahap = 6;
            } else if (value.is_cair()) {
                tahap = 5;
            } else if (value.getTahap().compareTo("Rencana Pencairan") == 0 || value.is_setujui()) {
                tahap = 4;
            } else if (value.getTahap().compareTo("Ditolak") == 0 || value.is_tolak()) {
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
                intent.putExtra("id_klien", value.getId_client());
                intent.putExtra("tahap", tahap1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
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


}


