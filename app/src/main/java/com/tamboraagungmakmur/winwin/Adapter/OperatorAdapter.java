package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Operator;
import com.tamboraagungmakmur.winwin.Model.Operator1;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 8/31/2017.
 */

public class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.AkuntingViewHolder> {

    private ArrayList<Operator1> akuntingArrayList;
    private Context context;

    public OperatorAdapter(ArrayList<Operator1> akuntingArrayList) {
        this.akuntingArrayList = akuntingArrayList;
    }

    @Override
    public AkuntingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operator, parent, false);
        AkuntingViewHolder holder = new AkuntingViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(AkuntingViewHolder holder, int position) {

        Operator1 akunting = akuntingArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.klien.setText(akunting.getNama());
        holder.pinjam.setText(akunting.getDate());
        holder.transaksi.setText(akunting.getBaru());
        holder.debit.setText(akunting.getSelesai());
        holder.kredit.setText(akunting.getSisa());

    }

    @Override
    public int getItemCount() {
        return akuntingArrayList.size();
    }

    public class AkuntingViewHolder extends RecyclerView.ViewHolder {

        private TextView klien, pinjam, transaksi, debit, kredit, no;

        public AkuntingViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            klien = (TextView) itemView.findViewById(R.id.klien);
            pinjam = (TextView) itemView.findViewById(R.id.pinjam);
            transaksi = (TextView) itemView.findViewById(R.id.transaksi);
            debit = (TextView) itemView.findViewById(R.id.debit);
            kredit = (TextView) itemView.findViewById(R.id.kredit);

        }
    }
}
