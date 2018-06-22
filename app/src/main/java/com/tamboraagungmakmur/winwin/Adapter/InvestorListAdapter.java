package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Investor;
import com.tamboraagungmakmur.winwin.Model.Pengajuan;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 12/15/2017.
 */

public class InvestorListAdapter extends RecyclerView.Adapter<InvestorListAdapter.PengajuanViewHolder> {

    private ArrayList<Investor> listPengajuan;
    private Context context;

    public InvestorListAdapter(ArrayList<Investor> listPengajuan) {
        this.listPengajuan = listPengajuan;
    }

    @Override
    public PengajuanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengajuan, parent, false);
        PengajuanViewHolder holder = new PengajuanViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(PengajuanViewHolder holder, int position) {

        final Investor pengajuan = listPengajuan.get(position);
        holder.txPengajuan.setText(pengajuan.getInvestor_nama() + " - " + pengajuan.getInvestor_nomor_investor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("investor_terkait");
                intent.putExtra("id", pengajuan.getInvestor_id());
                intent.putExtra("nama", pengajuan.getInvestor_nama());
                intent.putExtra("nomor", pengajuan.getInvestor_nomor_investor());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPengajuan.size();
    }

    public class PengajuanViewHolder extends RecyclerView.ViewHolder {

        private TextView txPengajuan;

        public PengajuanViewHolder(View itemView) {
            super(itemView);
            txPengajuan = (TextView) itemView.findViewById(R.id.txPengajuan);
        }
    }
}
