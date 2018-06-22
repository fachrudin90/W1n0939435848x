package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Karyawan;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 10/5/2017.
 */

public class Karyawan1Adapter extends RecyclerView.Adapter<Karyawan1Adapter.KaryawanViewHolder> {

    private ArrayList<Karyawan> karyawanArrayList;
    private Context context;

    public Karyawan1Adapter(ArrayList<Karyawan> karyawanArrayList) {
        this.karyawanArrayList = karyawanArrayList;
    }

    @Override
    public KaryawanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengajuan, parent, false);
        KaryawanViewHolder holder = new KaryawanViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(KaryawanViewHolder holder, int position) {

        final Karyawan karyawan = karyawanArrayList.get(position);

        holder.txPengajuan.setText(karyawan.getNama_karyawan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("klien_terkait");
                intent.putExtra("id", karyawan.getId());
                intent.putExtra("nama", karyawan.getNama_karyawan());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return karyawanArrayList.size();
    }

    public class KaryawanViewHolder extends RecyclerView.ViewHolder {

        private TextView txPengajuan;

        public KaryawanViewHolder(View itemView) {
            super(itemView);
            txPengajuan = (TextView) itemView.findViewById(R.id.txPengajuan);
        }
    }
}
