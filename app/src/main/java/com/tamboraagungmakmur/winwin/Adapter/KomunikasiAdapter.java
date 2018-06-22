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

import com.tamboraagungmakmur.winwin.Model.Email;
import com.tamboraagungmakmur.winwin.Model.KomunikasiModel;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class KomunikasiAdapter extends RecyclerView.Adapter<KomunikasiAdapter.MyViewHolder> {

    private ArrayList<Email> dataSet;
    private Context context;
    private ProgressBar progressBar;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txNo)
        TextView txNo;
        @Bind(R.id.txTglWaktu)
        TextView txTglWaktu;
        @Bind(R.id.txInfoKlien)
        TextView txInfoKlien;
        @Bind(R.id.txIsiPesan)
        TextView txIsiPesan;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;
        @Bind(R.id.pg)
        ProgressBar pg;


        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public KomunikasiAdapter(Context context, ArrayList<Email> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_komunikasi, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Email value = dataSet.get(listPosition);

        progressBar = holder.pg;
        if (listPosition == dataSet.size()-1) {
            holder.pg.setVisibility(View.VISIBLE);
        } else {
            holder.pg.setVisibility(View.GONE);
        }

        holder.txNo.setText("" + (listPosition+1));
        holder.txTglWaktu.setText(value.getCreated_at());
        holder.txInfoKlien.setText(value.getTo() + "\n" + value.getLabel());
        holder.txIsiPesan.setText(value.getMessage());
        holder.txStatus.setText(value.getStatus());

        if ((listPosition >= getItemCount() - 1))
            load();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("loading"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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


