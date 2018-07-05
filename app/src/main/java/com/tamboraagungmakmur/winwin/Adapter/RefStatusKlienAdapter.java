package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefArea;
import com.tamboraagungmakmur.winwin.Model.RefStatusKlien;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefStatusKlienEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusKlienAdapter extends RecyclerView.Adapter<RefStatusKlienAdapter.EmailTempViewHolder> {

    private ArrayList<RefStatusKlien> dataArrayList;
    private Context context;

    public RefStatusKlienAdapter(ArrayList<RefStatusKlien> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_status_klien, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefStatusKlien value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.namaLabel.setText(value.getStat_label());
        holder.namaCode.setText(value.getStat_code());
        holder.permitStatus.setText(value.getPermittoloan());
        holder.bannedStatus.setText(value.getBanned());
        holder.status.setText(value.getStat_rlc());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefStatusKlienEditActivity.class);
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

        private TextView no, namaLabel, namaCode, permitStatus, bannedStatus, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            namaLabel = (TextView) itemView.findViewById(R.id.namaLabel);
            namaCode = (TextView) itemView.findViewById(R.id.namaCode);
            permitStatus = (TextView) itemView.findViewById(R.id.permitStatus);
            bannedStatus = (TextView) itemView.findViewById(R.id.bannedStatus);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
