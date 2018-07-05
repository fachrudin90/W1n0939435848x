package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefMediaBayar;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefMediaBayarEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefMediaBayarAdapter extends RecyclerView.Adapter<RefMediaBayarAdapter.EmailTempViewHolder> {

    private ArrayList<RefMediaBayar> dataArrayList;
    private Context context;

    public RefMediaBayarAdapter(ArrayList<RefMediaBayar> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_media_bayar, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefMediaBayar value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.noId.setText(value.getId());
        holder.namaLabel.setText(value.getLabel());
        holder.namaCode.setText(value.getCode());
        holder.keterangan.setText(value.getKeterangan());
        holder.status.setText(value.getRlc());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefMediaBayarEditActivity.class);
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

        private TextView no, noId, namaLabel, namaCode, keterangan, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            noId = (TextView) itemView.findViewById(R.id.noId);
            namaLabel = (TextView) itemView.findViewById(R.id.mediaLabel);
            namaCode = (TextView) itemView.findViewById(R.id.mediaCode);
            keterangan = (TextView) itemView.findViewById(R.id.keterangan);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
