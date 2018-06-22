package com.tamboraagungmakmur.winwin.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.LogAktivitasModel;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LogAktivitasAdapter extends RecyclerView.Adapter<LogAktivitasAdapter.MyViewHolder> {


    private ArrayList<LogAktivitasModel> dataSet;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txTglWaktu)
        TextView txTglWaktu;
        @Bind(R.id.txWaktu)
        TextView txWaktu;
        @Bind(R.id.txTipe)
        TextView txTipe;
        @Bind(R.id.txTabel)
        TextView txTabel;
        @Bind(R.id.txAksesIp)
        TextView txAksesIp;
        @Bind(R.id.txAksesBrowser)
        TextView txAksesBrowser;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public LogAktivitasAdapter(Context context, ArrayList<LogAktivitasModel> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_log_aktivitas, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final LogAktivitasModel value = dataSet.get(listPosition);

        holder.txTglWaktu.setText(value.getTglWaktu());
        holder.txWaktu.setText(value.getWaktu());
        holder.txTipe.setText(value.getTipe());
        holder.txTabel.setText(value.getTabel());
        holder.txAksesIp.setText(value.getAksesIp());
        holder.txAksesBrowser.setText(value.getAksesBrowser());


        if ((listPosition >= getItemCount() - 1))
            load();

    }

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


