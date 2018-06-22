package com.tamboraagungmakmur.winwin.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.ArtikelDelActivity;
import com.tamboraagungmakmur.winwin.ArtikelEditActivity;
import com.tamboraagungmakmur.winwin.Model.Artikel;
import com.tamboraagungmakmur.winwin.Model.ArtikelModel;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.MyViewHolder> {


    private ArrayList<Artikel> dataSet;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.txTglWaktu)
        TextView txTglWaktu;
        @Bind(R.id.txJudul)
        TextView txJudul;
        @Bind(R.id.txPenulis)
        TextView txPenulis;
        @Bind(R.id.txKataKunci)
        TextView txKataKunci;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;
        @Bind(R.id.btView)
        ImageButton btView;
        @Bind(R.id.btDelete)
        ImageButton btDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public ArtikelAdapter(Context context, ArrayList<Artikel> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_artikel, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Artikel value = dataSet.get(listPosition);

        holder.txTglWaktu.setText(value.getTanggal_input());
        holder.txJudul.setText(value.getJudul());
        holder.txPenulis.setText(value.getPenulis());
        holder.txKataKunci.setText(value.getTags());
        holder.txStatus.setText(value.getStatus());

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArtikelDelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getId());
                context.startActivity(intent);
            }
        });

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArtikelEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getId());
                intent.putExtra("tanggal", value.getTanggal_input());
                intent.putExtra("status", value.getStatus());
                intent.putExtra("judul", value.getJudul());
                intent.putExtra("konten", value.getKonten());
                intent.putExtra("penulis", value.getPenulis());
                intent.putExtra("tags", value.getTags());
                context.startActivity(intent);
            }
        });

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


