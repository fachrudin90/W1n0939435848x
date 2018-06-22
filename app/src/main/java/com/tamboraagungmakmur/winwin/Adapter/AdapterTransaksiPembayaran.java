package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.ModelTransaksiPembayaran;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ayo Maju on 27/02/2018.
 * Updated by Muhammad Iqbal on 27/02/2018.
 */

public class AdapterTransaksiPembayaran extends RecyclerView.Adapter<AdapterTransaksiPembayaran.TransaksiHolder> {

    private ArrayList<ModelTransaksiPembayaran> arrayList;
    private Context context;

    public AdapterTransaksiPembayaran(ArrayList<ModelTransaksiPembayaran> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public class TransaksiHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tanggal)
        TextView tanggal;
        @Bind(R.id.keterangan)
        TextView keterangan;
        @Bind(R.id.nominal)
        TextView nominal;
        @Bind(R.id.pengajuan_ter)
        TextView pengajuanTer;
        @Bind(R.id.info_sistem)
        TextView infoSistem;

        public TransaksiHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public TransaksiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_transaksi_pembayaran, parent, false);
        return new TransaksiHolder(view);
    }

    @Override
    public void onBindViewHolder(TransaksiHolder holder, int position) {
        holder.tanggal.setText(arrayList.get(position).getTanggal());
        holder.keterangan.setText(arrayList.get(position).getKeterangan());
        holder.nominal.setText(arrayList.get(position).getNominal());
        holder.pengajuanTer.setText(arrayList.get(position).getPengajuan());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
