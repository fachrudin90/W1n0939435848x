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
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Pengajuan;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 8/28/2017.
 */

public class PengajuanAdapter extends RecyclerView.Adapter<PengajuanAdapter.PengajuanViewHolder> {

    private ArrayList<Pengajuan> listPengajuan;
    private Context context;

    public PengajuanAdapter(ArrayList<Pengajuan> listPengajuan) {
        this.listPengajuan = listPengajuan;
    }

    @Override
    public PengajuanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengajuan, parent, false);
        PengajuanViewHolder holder = new PengajuanViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(PengajuanViewHolder holder, int position) {

        final Pengajuan pengajuan = listPengajuan.get(position);
        holder.txPengajuan.setText(pengajuan.getNomor_pengajuan() + " | " + pengajuan.getNomor_pelanggan() + " " + pengajuan.getNama_lengkap());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("pengajuan_terkait");
                intent.putExtra("id", pengajuan.getId());
                intent.putExtra("nomor_pengajuan", pengajuan.getNomor_pengajuan());
                intent.putExtra("nomor_pelanggan", pengajuan.getNomor_pelanggan());
                intent.putExtra("nama_lengkap", pengajuan.getNama_lengkap());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPengajuan.size();
    }

    public class PengajuanViewHolder extends RecyclerView.ViewHolder {

        private TextView txPengajuan;

        public PengajuanViewHolder(View itemView) {
            super(itemView);
            txPengajuan = (TextView) itemView.findViewById(R.id.txPengajuan);
        }
    }
}