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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Task;
import com.tamboraagungmakmur.winwin.Model.TaskListModel;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.TaskDetActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {


    private ArrayList<Task> dataSet;
    private Context context;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txNo)
        TextView txNo;
        @Bind(R.id.txTglWaktu)
        TextView txTglWaktu;
        @Bind(R.id.txTugas)
        TextView txTugas;
        @Bind(R.id.txPengajuan)
        TextView txPengajuan;
        @Bind(R.id.txKlien)
        TextView txKlien;
        @Bind(R.id.txTipeTugas)
        TextView txTipeTugas;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.btView)
        ImageButton btView;
        @Bind(R.id.pg)
        ProgressBar pg;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public TaskListAdapter(Context context, ArrayList<Task> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_tasklist, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Task value = dataSet.get(listPosition);

        progressBar = holder.pg;
        if (listPosition == dataSet.size()-1) {
            if (isLast){
                holder.pg.setVisibility(View.GONE);
            } else {
                holder.pg.setVisibility(View.VISIBLE);
            }
        } else {
            holder.pg.setVisibility(View.GONE);
        }

        holder.txTglWaktu.setText(value.getCreated_at());
        holder.txTugas.setText(value.getTask());
        holder.txPengajuan.setText(value.getNomor_pengajuan());
        holder.txKlien.setText(value.getNomor_pelanggan() + "\n" + value.getNama_lengkap());
        holder.txTipeTugas.setText(value.getTipe_tugas());
        String status = "";
        holder.txNo.setText("" + (listPosition+1));

        if (value.is_finished()) {
            if (value.is_verified()) {
                status = "Selesai dan telah di-review";
            } else {
                status = "Selasai, belum di-review";
            }
        } else {
            status = "Sedang dikerjakan";
        }

        holder.txStatus.setText(status);

        final String finalStatus = status;
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
                intent.putExtra("task", value.getTask());
                intent.putExtra("note", value.getFinished_note());
                intent.putExtra("status", finalStatus);
                intent.putExtra("finished", value.is_finished());
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


