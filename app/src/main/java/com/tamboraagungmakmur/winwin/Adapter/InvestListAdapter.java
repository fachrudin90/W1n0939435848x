package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Invest;
import com.tamboraagungmakmur.winwin.Model.Investor;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 12/15/2017.
 */

public class InvestListAdapter extends RecyclerView.Adapter<InvestListAdapter.PengajuanViewHolder> {

    private ArrayList<Invest> listPengajuan;
    private Context context;

    public InvestListAdapter(ArrayList<Invest> listPengajuan) {
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

        final Invest pengajuan = listPengajuan.get(position);
        holder.txPengajuan.setText(pengajuan.getInvest_nomor_investasi() + " [" + pengajuan.getInvest_nomor_investasi() + " - " + pengajuan.getInvestor_nama() + "]");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("investor_terkait");
                intent.putExtra("id", pengajuan.getInvest_id());
                intent.putExtra("nama", pengajuan.getInvest_nomor_investasi() + " [" + pengajuan.getInvest_nomor_investasi() + " - " + pengajuan.getInvestor_nama() + "]");
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
