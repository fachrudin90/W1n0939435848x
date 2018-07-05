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
import com.tamboraagungmakmur.winwin.Model.RefJabatan;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefBankEditActivity;
import com.tamboraagungmakmur.winwin.RefJabatanEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefJabatanAdapter extends RecyclerView.Adapter<RefJabatanAdapter.EmailTempViewHolder> {

    private ArrayList<RefJabatan> dataArrayList;
    private Context context;

    public RefJabatanAdapter(ArrayList<RefJabatan> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_jabatan, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefJabatan value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.namaLabel.setText(value.getJab_label());
        holder.namaJabatan.setText(value.getJab_code());
        holder.analystStatus.setText(value.getAnalyst());
        holder.managementStatus.setText(value.getManagement());
        holder.status.setText(value.getStat_rlc());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefJabatanEditActivity.class);
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

        private TextView no, namaLabel,namaJabatan, analystStatus, managementStatus, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            namaLabel = (TextView) itemView.findViewById(R.id.namaLabel);
            namaJabatan = (TextView) itemView.findViewById(R.id.namaJabatan);
            analystStatus = (TextView) itemView.findViewById(R.id.analystStatus);
            managementStatus = (TextView) itemView.findViewById(R.id.mngStatus);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
