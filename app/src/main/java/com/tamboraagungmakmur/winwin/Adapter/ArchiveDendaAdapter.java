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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.DendaDeleteActivity;
import com.tamboraagungmakmur.winwin.Model.ArchiveDendaModel;
import com.tamboraagungmakmur.winwin.Model.Denda;
import com.tamboraagungmakmur.winwin.Model.DendaResponse;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;

import java.util.ArrayList;

import javax.crypto.spec.DESedeKeySpec;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArchiveDendaAdapter extends RecyclerView.Adapter<ArchiveDendaAdapter.MyViewHolder> {


    private ArrayList<Denda> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.btDelete)
        ImageButton btDelete;
        @Bind(R.id.txTglDenda)
        TextView txTglDenda;
        @Bind(R.id.txNoPeng)
        TextView txNoPeng;
        @Bind(R.id.txJmlDenda)
        TextView txJmlDenda;
        @Bind(R.id.txKeterangan)
        TextView txKeterangan;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;
        @Bind(R.id.pg)
        ProgressBar pg;
        @Bind(R.id.no)
        TextView no;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public ArchiveDendaAdapter(Context context, ArrayList<Denda> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_denda, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Denda value = dataSet.get(listPosition);

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
        holder.txTglDenda.setText(value.getDenda_created_at());
        holder.txNoPeng.setText(value.getPengajuan_nomor_pengajuan() + "\n" + value.getCli_nomor_pelanggan() + " - " +  value.getCli_nama_lengkap());
        holder.txJmlDenda.setText(new FormatPrice().format(value.getDenda_biaya()));
        holder.txKeterangan.setText(value.getDenda_catatan());

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DendaDeleteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getDenda_id());
                context.startActivity(intent);
            }
        });

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


