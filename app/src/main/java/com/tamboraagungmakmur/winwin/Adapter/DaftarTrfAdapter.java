package com.tamboraagungmakmur.winwin.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.DaftarTrfModel;
import com.tamboraagungmakmur.winwin.Model.Referral;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.TransferReferralActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DaftarTrfAdapter extends RecyclerView.Adapter<DaftarTrfAdapter.MyViewHolder> {


    private ArrayList<Referral> dataSet;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txTanggal)
        TextView txTanggal;
        @Bind(R.id.txNopel)
        TextView txNopel;
        @Bind(R.id.txBank)
        TextView txBank;
        @Bind(R.id.txNoRek)
        TextView txNoRek;
        @Bind(R.id.txNama)
        TextView txNama;
        @Bind(R.id.txJmlTrf)
        TextView txJmlTrf;
        @Bind(R.id.txNoPengTer)
        TextView txNoPengTer;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public DaftarTrfAdapter(Context context, ArrayList<Referral> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_daftar_transfer, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Referral value = dataSet.get(listPosition);

        holder.txTanggal.setText(value.getCreated_at());
        holder.txNopel.setText(value.getNomor_pelanggan());
        holder.txBank.setText(value.getNama_bank());
        holder.txNoRek.setText(value.getNomor_rekening());
        holder.txNama.setText(value.getNama_rekening());
        holder.txJmlTrf.setText(value.getNominal());
        holder.txNoPengTer.setText(Arrays.toString(value.getList_nomor_pengajuan()));

        if (value.getStatus().compareTo("Belum ditransfer") == 0) {
            holder.txStatus.setText("Set Sudah Transfer");
            holder.txStatus.setTextColor(context.getResources().getColor(R.color.greendark));
            holder.txStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TransferReferralActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", value.getId());
                    context.startActivity(intent);
                }
            });
        } else {
            holder.txStatus.setText(value.getStatus());
            holder.txStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        if ((listPosition >= getItemCount() - 1))
            load();

    }

    public void load() {

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


}


