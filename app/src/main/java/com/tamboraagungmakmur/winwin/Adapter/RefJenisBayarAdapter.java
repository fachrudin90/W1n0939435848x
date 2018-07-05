package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefJenisBayar;
import com.tamboraagungmakmur.winwin.Model.RefTipeTransaksi;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefJenisBayarEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefJenisBayarAdapter extends RecyclerView.Adapter<RefJenisBayarAdapter.EmailTempViewHolder> {

    private ArrayList<RefJenisBayar> dataArrayList;
    private Context context;

    public RefJenisBayarAdapter(ArrayList<RefJenisBayar> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_jenis_bayar, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefJenisBayar value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.namaLabel.setText(value.getJenisbayar_label());
        holder.namaCode.setText(value.getJenisbayar_code());
        holder.keterangan.setText(value.getJenisbayar_penjelasan());
        holder.managementStatus.setText(value.getStat_trx());
        holder.status.setText(value.getStat_rlc());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefJenisBayarEditActivity.class);
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

        private TextView no, namaLabel, namaCode, keterangan, managementStatus, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            namaLabel = (TextView) itemView.findViewById(R.id.namaLabel);
            namaCode = (TextView) itemView.findViewById(R.id.namaCode);
            keterangan = (TextView) itemView.findViewById(R.id.keterangan);
            managementStatus = (TextView) itemView.findViewById(R.id.managementStatus);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
