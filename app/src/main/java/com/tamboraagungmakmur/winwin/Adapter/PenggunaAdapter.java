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
import com.tamboraagungmakmur.winwin.Model.Pengguna;
import com.tamboraagungmakmur.winwin.PenggunaEditActivity;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 9/25/2017.
 */

public class PenggunaAdapter extends RecyclerView.Adapter<PenggunaAdapter.MyViewHolder> {


    private ArrayList<Pengguna> dataSet;
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

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public PenggunaAdapter(Context context, ArrayList<Pengguna> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pengguna, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Pengguna value = dataSet.get(listPosition);

        holder.txTglWaktu.setText(value.getId());
        holder.txJudul.setText(value.getNama_karyawan());
        holder.txPenulis.setText(value.getUsername());
        holder.txKataKunci.setText(value.is_disabled()?"Tidak Aktif":"Aktif");
        holder.txStatus.setText(value.getRoles());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PenggunaEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getId());
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