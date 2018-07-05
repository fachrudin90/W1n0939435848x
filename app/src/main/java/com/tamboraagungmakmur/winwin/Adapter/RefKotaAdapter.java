package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefKota;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefKotaEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefKotaAdapter extends RecyclerView.Adapter<RefKotaAdapter.EmailTempViewHolder> {

    private ArrayList<RefKota> emailTemplateArrayList;
    private Context context;

    public RefKotaAdapter(ArrayList<RefKota> emailTemplateArrayList) {
        this.emailTemplateArrayList = emailTemplateArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_kota, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefKota refKota = emailTemplateArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.prov.setText(refKota.getKodepos_provinsi());
        holder.kota.setText(refKota.getKodepos_kabupaten());
        holder.status.setText(refKota.getTolak());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefKotaEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", refKota.getId());
//                intent.putExtra("id", refKota.getKodepos_provinsi()+","+refKota.getKodepos_kabupaten());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return emailTemplateArrayList.size();
    }

    public class EmailTempViewHolder extends RecyclerView.ViewHolder {

        private TextView no, prov, kota, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            prov = (TextView) itemView.findViewById(R.id.prov);
            kota = (TextView) itemView.findViewById(R.id.kota);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
