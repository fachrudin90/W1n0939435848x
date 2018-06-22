package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Internal;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;

import java.util.ArrayList;

/**
 * Created by innan on 8/30/2017.
 */

public class InternalAdapter extends RecyclerView.Adapter<InternalAdapter.AkuntingViewHolder> {

    private ArrayList<Internal> akuntingArrayList;
    private Context context;

    public InternalAdapter(ArrayList<Internal> akuntingArrayList) {
        this.akuntingArrayList = akuntingArrayList;
    }

    @Override
    public AkuntingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_internal, parent, false);
        AkuntingViewHolder holder = new AkuntingViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(AkuntingViewHolder holder, int position) {

        Internal akunting = akuntingArrayList.get(position);
        holder.no.setText("" + (position+1));
        holder.klien.setText(akunting.getKlien());
        holder.pinjam.setText(new FormatPrice().format(akunting.getNilai_pinjam()));
        holder.transaksi.setText(akunting.getDurasi());
        holder.debit.setText(new FormatPrice().format(akunting.getTotal_kembali()));
        holder.kredit.setText(new FormatPrice().format(akunting.getMargin()));
        holder.saldo.setText(akunting.getInterest());

    }

    @Override
    public int getItemCount() {
        return akuntingArrayList.size();
    }

    public class AkuntingViewHolder extends RecyclerView.ViewHolder {

        private TextView klien, pinjam, transaksi, debit, kredit, saldo, no;

        public AkuntingViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            klien = (TextView) itemView.findViewById(R.id.klien);
            pinjam = (TextView) itemView.findViewById(R.id.pinjam);
            transaksi = (TextView) itemView.findViewById(R.id.transaksi);
            debit = (TextView) itemView.findViewById(R.id.debit);
            kredit = (TextView) itemView.findViewById(R.id.kredit);
            saldo = (TextView) itemView.findViewById(R.id.saldo);

        }
    }
}
