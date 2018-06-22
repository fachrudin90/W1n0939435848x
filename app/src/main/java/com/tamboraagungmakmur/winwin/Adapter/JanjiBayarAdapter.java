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

import com.tamboraagungmakmur.winwin.Model.JanjiBayar;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JanjiBayarAdapter extends RecyclerView.Adapter<JanjiBayarAdapter.MyViewHolder> {



    private ArrayList<JanjiBayar> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.no)
        TextView no;
        @Bind(R.id.txTglPermintaan)
        TextView txTglPermintaan;
        @Bind(R.id.txNoPeng)
        TextView txNoPeng;
        @Bind(R.id.txKlien)
        TextView txKlien;
        @Bind(R.id.txTglBayar)
        TextView txTglBayar;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;
        @Bind(R.id.pg)
        ProgressBar pg;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public JanjiBayarAdapter(Context context, ArrayList<JanjiBayar> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_janji_bayar, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final JanjiBayar value = dataSet.get(listPosition);

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

        holder.no.setText("" + (listPosition + 1));
        holder.txTglPermintaan.setText(value.getPengajuan_janji_bayar_aktif_pada());
        holder.txNoPeng.setText(value.getNomor_pengajuan());
        holder.txKlien.setText(value.getNama_lengkap());
        holder.txTglBayar.setText(value.getPengajuan_janji_bayar_tgl());


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


