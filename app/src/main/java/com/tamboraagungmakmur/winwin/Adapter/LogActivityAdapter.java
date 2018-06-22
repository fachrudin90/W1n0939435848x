package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.LogActDetActivity;
import com.tamboraagungmakmur.winwin.Model.LogActivity;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 8/31/2017.
 */

public class LogActivityAdapter extends RecyclerView.Adapter<LogActivityAdapter.AkuntingViewHolder> {

    private ArrayList<LogActivity> akuntingArrayList;
    private Context context;

    public LogActivityAdapter(ArrayList<LogActivity> akuntingArrayList) {
        this.akuntingArrayList = akuntingArrayList;
    }

    @Override
    public AkuntingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logactivity, parent, false);
        AkuntingViewHolder holder = new AkuntingViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(AkuntingViewHolder holder, int position) {

        final LogActivity akunting = akuntingArrayList.get(position);
        holder.klien.setText(akunting.getLog_date());
        holder.pinjam.setText(akunting.getLogin_time());
        holder.transaksi.setText(akunting.getStat_total_rekomendasi_setujui());
        holder.debit.setText(akunting.getStat_total_rekomendasi_tolak());
        holder.kredit.setText(akunting.getStat_total_lihat_profil_klien());
        holder.saldo.setText(akunting.getStat_total_proses_pengajuan());
        holder.logouttime.setText(akunting.getLogout_time());
        holder.logoutby.setText(akunting.getLogout_by());
        holder.no.setText("" + (position+1));
        holder.user.setText(akunting.getUser());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LogActDetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("user", akunting.getUser());
                intent.putExtra("tgl1", akunting.getLog_date());
                intent.putExtra("tgl2", akunting.getLog_date());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return akuntingArrayList.size();
    }

    public class AkuntingViewHolder extends RecyclerView.ViewHolder {

        private TextView klien, pinjam, transaksi, debit, kredit, saldo, logouttime, logoutby, no, user;
        private ImageButton btView, btView1;

        public AkuntingViewHolder(View itemView) {
            super(itemView);

            klien = (TextView) itemView.findViewById(R.id.klien);
            pinjam = (TextView) itemView.findViewById(R.id.pinjam);
            transaksi = (TextView) itemView.findViewById(R.id.transaksi);
            debit = (TextView) itemView.findViewById(R.id.debit);
            kredit = (TextView) itemView.findViewById(R.id.kredit);
            saldo = (TextView) itemView.findViewById(R.id.saldo);
            logouttime = (TextView) itemView.findViewById(R.id.logouttime);
            logoutby = (TextView) itemView.findViewById(R.id.logoutby);
            no = (TextView) itemView.findViewById(R.id.no);
            user = (TextView) itemView.findViewById(R.id.user);

            btView = (ImageButton) itemView.findViewById(R.id.btView);
            btView1 = (ImageButton) itemView.findViewById(R.id.btView1);

        }
    }
}