package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.User;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 9/6/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.PengajuanViewHolder> {

    private ArrayList<User> listPengajuan;
    private Context context;

    public UserAdapter(ArrayList<User> listPengajuan) {
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

        final User pengajuan = listPengajuan.get(position);
        holder.txPengajuan.setText(pengajuan.getNama_karyawan());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("user_terkait");
                intent.putExtra("id", pengajuan.getId());
                intent.putExtra("nama", pengajuan.getNama_karyawan());
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
