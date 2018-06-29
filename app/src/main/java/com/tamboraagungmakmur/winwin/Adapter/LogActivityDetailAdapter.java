package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.LogActDetActivity;
import com.tamboraagungmakmur.winwin.Model.LogActivity;
import com.tamboraagungmakmur.winwin.Model.LogActivityDetail;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 8/31/2017.
 */

public class LogActivityDetailAdapter extends RecyclerView.Adapter<LogActivityDetailAdapter.AkuntingViewHolder> {

    private ArrayList<LogActivityDetail> akuntingArrayList;
    private Context context;

    public LogActivityDetailAdapter(ArrayList<LogActivityDetail> akuntingArrayList) {
        this.akuntingArrayList = akuntingArrayList;
    }

    @Override
    public AkuntingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logactivitydetail, parent, false);
        AkuntingViewHolder holder = new AkuntingViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(AkuntingViewHolder holder, int position) {

        final LogActivityDetail akunting = akuntingArrayList.get(position);
        holder.klien.setText(akunting.getLog_date());
        holder.pinjam.setText(akunting.getLogin_time());
        holder.transaksi.setText(akunting.getStat_total_rekomendasi_setujui());
        holder.debit.setText(akunting.getStat_total_rekomendasi_tolak());
        holder.kredit.setText(akunting.getStat_total_lihat_profil_klien());
        holder.saldo.setText(akunting.getStat_total_proses_pengajuan());
        holder.catatan.setText(akunting.getStat_total_catatan_pengajuan());
        holder.logouttime.setText(akunting.getLogout_time());
        holder.logoutby.setText(akunting.getLogout_by());
        holder.user.setText(akunting.getUser());



    }

    @Override
    public int getItemCount() {
        return akuntingArrayList.size();
    }

    public class AkuntingViewHolder extends RecyclerView.ViewHolder {

        private TextView klien, pinjam, transaksi, debit, kredit, saldo, logouttime, logoutby, user, catatan;

        public AkuntingViewHolder(View itemView) {
            super(itemView);

            klien = (TextView) itemView.findViewById(R.id.klien);
            pinjam = (TextView) itemView.findViewById(R.id.pinjam);
            transaksi = (TextView) itemView.findViewById(R.id.transaksi);
            debit = (TextView) itemView.findViewById(R.id.debit);
            kredit = (TextView) itemView.findViewById(R.id.kredit);
            saldo = (TextView) itemView.findViewById(R.id.saldo);
            catatan = (TextView) itemView.findViewById(R.id.catatan);
            logouttime = (TextView) itemView.findViewById(R.id.logouttime);
            logoutby = (TextView) itemView.findViewById(R.id.logoutby);
            user = (TextView) itemView.findViewById(R.id.user);


        }
    }
}
