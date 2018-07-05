package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefMediaBayar;
import com.tamboraagungmakmur.winwin.Model.RefMediaPembayaran;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefMediaPembayaranAdapter extends RecyclerView.Adapter<RefMediaPembayaranAdapter.EmailTempViewHolder> {

    private ArrayList<RefMediaPembayaran> dataArrayList;
    private Context context;

    public RefMediaPembayaranAdapter(ArrayList<RefMediaPembayaran> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_media_pembayaran, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefMediaPembayaran value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.noId.setText(value.getId());
        holder.mediaLabel.setText(value.getLabel());
        holder.mediaBiaya.setText(value.getId_biaya());
        holder.mediaPendapatan.setText(value.getId_pendapatan());
        holder.flag.setText(value.getFlag());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, RefBankEditActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("id", value.getId());
//                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class EmailTempViewHolder extends RecyclerView.ViewHolder {

        private TextView no, noId, mediaLabel, mediaPendapatan, mediaBiaya, flag;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            noId = (TextView) itemView.findViewById(R.id.noId);
            mediaLabel = (TextView) itemView.findViewById(R.id.mediaLabel);
            mediaPendapatan = (TextView) itemView.findViewById(R.id.mediaPendapatan);
            mediaBiaya = (TextView) itemView.findViewById(R.id.mediaBiaya);
            flag = (TextView) itemView.findViewById(R.id.mediaFlag);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
