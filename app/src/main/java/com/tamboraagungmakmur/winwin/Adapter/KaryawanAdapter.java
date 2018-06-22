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

import com.tamboraagungmakmur.winwin.KaryawanEditActivity;
import com.tamboraagungmakmur.winwin.Model.Karyawan;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 10/2/2017.
 */

public class KaryawanAdapter extends RecyclerView.Adapter<KaryawanAdapter.MyViewHolder> {


    private ArrayList<Karyawan> dataSet;
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

    public KaryawanAdapter(Context context, ArrayList<Karyawan> data) {
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

        final Karyawan value = dataSet.get(listPosition);

        holder.txTglWaktu.setText(value.getNama_karyawan());
        holder.txJudul.setText(value.getJabatan());
        holder.txPenulis.setText(value.isKar_sudah_keluar()?"Sudah Keluar":"Masih Aktif");
        holder.txKataKunci.setText(value.getKar_tgl_masuk());
        holder.txStatus.setText(value.getKar_tgl_keluar());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KaryawanEditActivity.class);
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