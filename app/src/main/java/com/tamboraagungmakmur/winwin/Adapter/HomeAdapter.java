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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.HomeModel;
import com.tamboraagungmakmur.winwin.Model.Task;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.TaskDetActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private ArrayList<Task> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txTglWaktu)
        TextView txTglWaktu;
        @Bind(R.id.txTugas)
        TextView txTugas;
        @Bind(R.id.txPengajuan)
        TextView txPengajuan;
        @Bind(R.id.txKlien)
        TextView txKlien;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.no)
        TextView no;
        @Bind(R.id.btView)
        ImageButton btView;
        @Bind(R.id.pg)
        ProgressBar pg;
        @Bind(R.id.checkbox)
        CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public HomeAdapter(Context context, ArrayList<Task> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_home, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

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

        final Task value = dataSet.get(listPosition);

        holder.no.setText(""+(listPosition+1));
        holder.txTglWaktu.setText(value.getCreated_at());
        holder.txTugas.setText(value.getTask());
        holder.txPengajuan.setText(value.getNomor_pengajuan());
        holder.txKlien.setText(value.getNomor_pelanggan() + "\n" + value.getNama_lengkap());

        holder.checkBox.setChecked(false);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent intent = new Intent("task_analisa1");
                intent.putExtra("id", value.getId());
                intent.putExtra("checked", b);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                Intent intent1 = new Intent("task_verifikasi1");
                intent1.putExtra("id", value.getId());
                intent1.putExtra("checked", b);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

                Intent intent4 = new Intent("task_jatuhtempo1");
                intent4.putExtra("id", value.getId());
                intent4.putExtra("checked", b);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent4);

                Intent intent2 = new Intent("task_collection1");
                intent2.putExtra("id", value.getId());
                intent2.putExtra("checked", b);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);

                Intent intent3 = new Intent("task_other1");
                intent3.putExtra("id", value.getId());
                intent3.putExtra("checked", b);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent3);
            }
        });

//        String status = "";
//
//        if (value.is_finished()) {
//            if (value.is_verified()) {
//                status = "Selesai dan telah di-review";
//            } else {
//                status = "Selasai, belum di-review";
//            }
//        } else {
//            status = "Sedang dikerjakan";
//        }

        holder.txStatus.setText(value.getStatus());
//        final String finalStatus = status;

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskDetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getId());
                intent.putExtra("nomor_pengajuan", value.getNomor_pengajuan());
                intent.putExtra("nomor_pelanggan", value.getNomor_pelanggan());
                intent.putExtra("nama_lengkap", value.getNama_lengkap());
                intent.putExtra("tanggal_pengajuan", value.getCreated_at());
                intent.putExtra("deadline", value.getDeadline());
                intent.putExtra("jatuh_tempo", value.getPengajuan_jatuh_tempo());
                intent.putExtra("task", value.getTask());
                intent.putExtra("note", value.getFinished_note());
                intent.putExtra("status", value.getStatus());
                intent.putExtra("finished", value.is_finished());
                context.startActivity(intent);
            }
        });

        if ((listPosition >= getItemCount() - 1))
            load();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("home_analisa"));

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


