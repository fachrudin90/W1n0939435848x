package com.tamboraagungmakmur.winwin.Adapter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.ArchivePembayaranModel;
import com.tamboraagungmakmur.winwin.Model.Pembayaran;
import com.tamboraagungmakmur.winwin.Model.PembayaranResponse;
import com.tamboraagungmakmur.winwin.PembDeleteActivity;
import com.tamboraagungmakmur.winwin.PengDeleteActivity;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArchivePembayaranAdapter extends RecyclerView.Adapter<ArchivePembayaranAdapter.MyViewHolder> {


    private ArrayList<Pembayaran> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.btDelete)
        ImageButton btDelete;
        @Bind(R.id.txTglBayar)
        TextView txTglBayar;
        @Bind(R.id.txNominal)
        TextView txNominal;
        @Bind(R.id.txNoPeng)
        TextView txNoPeng;
        @Bind(R.id.txCatatan)
        TextView txCatatan;
        @Bind(R.id.txMediaBayar)
        TextView txMediaBayar;
        @Bind(R.id.txJenisPembayaran)
        TextView txJenisPembayaran;
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

    public ArchivePembayaranAdapter(Context context, ArrayList<Pembayaran> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pembayaran, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Pembayaran value = dataSet.get(listPosition);

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
        holder.txTglBayar.setText(value.getPemb_tanggal() + " " + value.getPemb_waktu());
        holder.txNominal.setText(new FormatPrice().format(value.getPemb_nominal()));
        holder.txNoPeng.setText(value.getPengajuan_nomor_pengajuan() + "\n" + value.getCli_nomor_pelanggan() + " - " + value.getCli_nama_lengkap());
        holder.txCatatan.setText(value.getPemb_catatan());
        holder.txMediaBayar.setText(value.getMediabayar_label());
        holder.txJenisPembayaran.setText(value.getJenisbayar_label());


        SessionManager sessionManager = new SessionManager(context);
        Log.d("roles", sessionManager.isPELUNASAN()?"true":"false");
        if (!sessionManager.isPELUNASAN()) {
            Log.d("Pelunasan", "false");
            holder.btDelete.setVisibility(View.INVISIBLE);
        } else {
            Log.d("Pelunasan", "true");
            holder.btDelete.setVisibility(View.VISIBLE);
        }

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PembDeleteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getPemb_id());
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


