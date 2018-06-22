package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Simulasi;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 8/31/2017.
 */

public class SimulasiAdapter extends RecyclerView.Adapter<SimulasiAdapter.AkuntingViewHolder> {

    private ArrayList<Simulasi> akuntingArrayList;
    private Context context;

    public SimulasiAdapter(ArrayList<Simulasi> akuntingArrayList) {
        this.akuntingArrayList = akuntingArrayList;
    }

    @Override
    public AkuntingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simulasi, parent, false);
        AkuntingViewHolder holder = new AkuntingViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(AkuntingViewHolder holder, int position) {

        Simulasi akunting = akuntingArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.klien.setText(akunting.getNilai_pinjam());
        holder.pinjam.setText(akunting.getJangka_pinjam());
        holder.transaksi.setText(akunting.getTotal());
        holder.debit.setText(akunting.getPrint_pinjam());

    }

    @Override
    public int getItemCount() {
        return akuntingArrayList.size();
    }

    public class AkuntingViewHolder extends RecyclerView.ViewHolder {

        private TextView klien, pinjam, transaksi, debit, no;

        public AkuntingViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            klien = (TextView) itemView.findViewById(R.id.klien);
            pinjam = (TextView) itemView.findViewById(R.id.pinjam);
            transaksi = (TextView) itemView.findViewById(R.id.transaksi);
            debit = (TextView) itemView.findViewById(R.id.debit);

        }
    }
}
