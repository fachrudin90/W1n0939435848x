package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Akunting;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innani on 8/30/2017.
 */

public class AkuntingAdapter extends RecyclerView.Adapter<AkuntingAdapter.AkuntingViewHolder> {

    private ArrayList<Akunting> akuntingArrayList;
    private Context context;

    public AkuntingAdapter(ArrayList<Akunting> akuntingArrayList) {
        this.akuntingArrayList = akuntingArrayList;
    }

    @Override
    public AkuntingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_akunting, parent, false);
        AkuntingViewHolder holder = new AkuntingViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(AkuntingViewHolder holder, int position) {

        Akunting akunting = akuntingArrayList.get(position);
        holder.no.setText("" + (position+1));
        holder.waktu.setText(akunting.getDate());
        holder.klien.setText(akunting.getKlien());
        holder.pinjam.setText(akunting.getNomor_pengajuan());
        holder.transaksi.setText(akunting.getTipe());
        holder.debit.setText(akunting.getDebit());
        holder.kredit.setText(akunting.getKredit());
        holder.saldo.setText(akunting.getSaldo());

    }

    @Override
    public int getItemCount() {
        return akuntingArrayList.size();
    }

    public class AkuntingViewHolder extends RecyclerView.ViewHolder {

        private TextView waktu, klien, pinjam, transaksi, debit, kredit, saldo, no;

        public AkuntingViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            waktu = (TextView) itemView.findViewById(R.id.waktu);
            klien = (TextView) itemView.findViewById(R.id.klien);
            pinjam = (TextView) itemView.findViewById(R.id.pinjam);
            transaksi = (TextView) itemView.findViewById(R.id.transaksi);
            debit = (TextView) itemView.findViewById(R.id.debit);
            kredit = (TextView) itemView.findViewById(R.id.kredit);
            saldo = (TextView) itemView.findViewById(R.id.saldo);

        }
    }
}
