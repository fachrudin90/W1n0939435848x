package com.tamboraagungmakmur.winwin.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Pencairan;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 8/26/2017.
 */

public class ArchivePencarianAdapter1 extends RecyclerView.Adapter<ArchivePencarianAdapter1.MyViewHolder> {


    private ArrayList<Pencairan> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txPengajuan)
        TextView txPengajuan;
        @Bind(R.id.txBank)
        TextView txBank;
        @Bind(R.id.txNoRek)
        TextView txNoRek;
        @Bind(R.id.txJmlTrf)
        TextView txJmlTrf;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.no)
        TextView no;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;
        @Bind(R.id.pg)
        ProgressBar pg;
        @Bind(R.id.checkbox)
        CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public ArchivePencarianAdapter1(Context context, ArrayList<Pencairan> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pencarian, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Pencairan value = dataSet.get(listPosition);

        progressBar = holder.pg;
        if (listPosition == dataSet.size()-1) {
            if (isLast) {
                holder.pg.setVisibility(View.GONE);
            } else {
                holder.pg.setVisibility(View.VISIBLE);
            }
        } else {
            holder.pg.setVisibility(View.GONE);
        }

        holder.no.setText(""+(listPosition+1));
        holder.txPengajuan.setText(value.getNomor_pengajuan() + "\n" + value.getNama_lengkap());
//        holder.txBank.setText(value.getNama_rekening());
        holder.txNoRek.setText(value.getNomor_rekening() + "\n" + value.getNama_rekening());
        holder.txJmlTrf.setText(new FormatPrice().format(value.getAmount()));
        holder.txStatus.setText(value.getStatus());

        if (value.getStatus().compareTo("Belum cair") == 0) {
            holder.itemView.setBackgroundResource(R.color.colorgreencair1);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setBackgroundResource(R.color.colorgreencair);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        holder.checkBox.setChecked(false);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent intent = new Intent("pencairan_pilih");
                intent.putExtra("id", value.getId());
                intent.putExtra("checked", b);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("loading"));

        if ((listPosition >= getItemCount() - 1))
            load();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("loading"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isLast = true;
            progressBar.setVisibility(View.GONE);
        }
    };

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

