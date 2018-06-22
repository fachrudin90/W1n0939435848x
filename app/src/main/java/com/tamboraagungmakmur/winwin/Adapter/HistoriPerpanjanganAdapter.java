package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.HistoriPembDelActivity;
import com.tamboraagungmakmur.winwin.HistoriPerpDelActivity;
import com.tamboraagungmakmur.winwin.Model.Perpanjangan2;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 9/12/2017.
 */

public class HistoriPerpanjanganAdapter extends RecyclerView.Adapter<HistoriPerpanjanganAdapter.HistoriViewHolder> {

    private ArrayList<Perpanjangan2> perpanjangan2ArrayList;
    private Context context;

    public HistoriPerpanjanganAdapter(ArrayList<Perpanjangan2> perpanjangan2ArrayList) {
        this.perpanjangan2ArrayList = perpanjangan2ArrayList;
    }

    @Override
    public HistoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historiperp, parent, false);
        HistoriViewHolder holder = new HistoriViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoriViewHolder holder, final int position) {

        Perpanjangan2 perpanjangan2 = perpanjangan2ArrayList.get(position);
        holder.date.setText(perpanjangan2.getCreated_at());
        holder.amount.setText("Jatuh Tempo Baru: " + perpanjangan2.getEnd());
        holder.via.setText("Status: " + perpanjangan2.getStatus());
        holder.catatan.setText("Catatan: " + perpanjangan2.getNote());


    }

    @Override
    public int getItemCount() {
        return perpanjangan2ArrayList.size();
    }

    public class HistoriViewHolder extends RecyclerView.ViewHolder {

        private TextView date, amount, via, catatan;

        public HistoriViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            amount = (TextView) itemView.findViewById(R.id.amount);
            via = (TextView) itemView.findViewById(R.id.via);
            catatan = (TextView) itemView.findViewById(R.id.catatan);
        }
    }
}
