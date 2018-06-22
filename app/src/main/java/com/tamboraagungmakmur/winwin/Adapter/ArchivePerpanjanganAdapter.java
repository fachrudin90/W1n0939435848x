package com.tamboraagungmakmur.winwin.Adapter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.ArchivePerpanjanganModel;
import com.tamboraagungmakmur.winwin.Model.Perpanjangan;
import com.tamboraagungmakmur.winwin.Model.Perpanjangan1;
import com.tamboraagungmakmur.winwin.Model.PerpanjanganResponse;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArchivePerpanjanganAdapter extends RecyclerView.Adapter<ArchivePerpanjanganAdapter.MyViewHolder> {


    private ArrayList<Perpanjangan> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txTglPermintaan)
        TextView txTglPermintaan;
        @Bind(R.id.txNoPeng)
        TextView txNoPeng;
        @Bind(R.id.txTglMulai)
        TextView txTglMulai;
        @Bind(R.id.txDurasi)
        TextView txDurasi;
        @Bind(R.id.txJatuhTempo)
        TextView txJatuhTempo;
        @Bind(R.id.txCatatan)
        TextView txCatatan;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;
        @Bind(R.id.pg)
        ProgressBar pg;
        @Bind(R.id.no)
        TextView no;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public ArchivePerpanjanganAdapter(Context context, ArrayList<Perpanjangan> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_perpanjangan, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Perpanjangan value = dataSet.get(listPosition);

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

        holder.no.setText(""+(listPosition+1));
        holder.txTglPermintaan.setText(value.getPerpanjang_diajukan_tanggal() + " " + value.getPerpanjang_diajukan_waktu());
        holder.txNoPeng.setText(value.getPengajuan_nomor_pengajuan() + "\n" + value.getCli_nomor_pelanggan() + " - " +  value.getCli_nama_lengkap());
        holder.txTglMulai.setText(value.getPerpanjang_dimulai());
        holder.txDurasi.setText(value.getPerpanjang_durasi_hari());
        holder.txJatuhTempo.setText(value.getPengajuan_jatuh_tempo());
        holder.txCatatan.setText(value.getPerpanjang_catatan());
        holder.txStatus.setText(value.getAjustat_label());


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


