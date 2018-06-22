package com.tamboraagungmakmur.winwin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Notes;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 9/25/2017.
 */

public class HistoriTahapan1Adapter extends RecyclerView.Adapter<HistoriTahapan1Adapter.HistoriViewHolder> {

    private ArrayList<Notes> historiTahapanArrayList;

    public HistoriTahapan1Adapter(ArrayList<Notes> historiTahapanArrayList) {
        this.historiTahapanArrayList = historiTahapanArrayList;
    }

    @Override
    public HistoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tahapan1, parent, false);
        HistoriViewHolder holder = new HistoriViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoriViewHolder holder, int position) {
        Notes historiTahapan = historiTahapanArrayList.get(position);

        if (historiTahapan.getPengajuan() != null) {
            holder.text1.setText(historiTahapan.getCreated_by() + " - Pengajuan " + historiTahapan.getPengajuan());
        } else {
            holder.text1.setText(historiTahapan.getCreated_by());
        }
        holder.text2.setText(historiTahapan.getCreated_at());
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

