package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefStatusPengajuan;
import com.tamboraagungmakmur.winwin.Model.RefTahapPengajuan;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefStatusPengajuanEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusPengajuanAdapter extends RecyclerView.Adapter<RefStatusPengajuanAdapter.EmailTempViewHolder> {

    private ArrayList<RefStatusPengajuan> dataArrayList;
    private Context context;

    public RefStatusPengajuanAdapter(ArrayList<RefStatusPengajuan> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_status_pengajuan, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefStatusPengajuan value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.noId.setText(value.getId());
        holder.namaLabel.setText(value.getAjustat_label());
        holder.namaCode.setText(value.getAjustat_code());
        holder.terimaTolak.setText(value.getWajib());
        holder.status.setText(value.getStat_rlc());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefStatusPengajuanEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class EmailTempViewHolder extends RecyclerView.ViewHolder {

        private TextView no, noId, namaLabel, namaCode, terimaTolak, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            noId = (TextView) itemView.findViewById(R.id.noId);
            namaLabel = (TextView) itemView.findViewById(R.id.namaLabel);
            namaCode = (TextView) itemView.findViewById(R.id.namaCode);
            terimaTolak = (TextView) itemView.findViewById(R.id.terimaTolak);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
