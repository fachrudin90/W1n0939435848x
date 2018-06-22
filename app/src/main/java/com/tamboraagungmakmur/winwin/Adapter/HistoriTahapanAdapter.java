package com.tamboraagungmakmur.winwin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.HistoriTahapan;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 9/11/2017.
 */

public class HistoriTahapanAdapter extends RecyclerView.Adapter<HistoriTahapanAdapter.HistoriViewHolder> {

    private ArrayList<HistoriTahapan> historiTahapanArrayList;

    public HistoriTahapanAdapter(ArrayList<HistoriTahapan> historiTahapanArrayList) {
        this.historiTahapanArrayList = historiTahapanArrayList;
    }

    @Override
    public HistoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tahapan, parent, false);
        HistoriViewHolder holder = new HistoriViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoriViewHolder holder, int position) {
        HistoriTahapan historiTahapan = historiTahapanArrayList.get(position);
        holder.text1.setText(historiTahapan.getTahap() + " - " + historiTahapan.getUser());
        holder.text2.setText(historiTahapan.getDate());
        holder.text3.setText(historiTahapan.getNote());
    }

    @Override
    public int getItemCount() {
        return historiTahapanArrayList.size();
    }

    public class HistoriViewHolder extends RecyclerView.ViewHolder {

        private TextView text1, text2, text3;

        public HistoriViewHolder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
            text3 = (TextView) itemView.findViewById(R.id.text3);
        }
    }
}
