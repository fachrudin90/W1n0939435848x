package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefPerusahaan;
import com.tamboraagungmakmur.winwin.Model.RefRlc;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefBankEditActivity;
import com.tamboraagungmakmur.winwin.RefRlcEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefRlcAdapter extends RecyclerView.Adapter<RefRlcAdapter.EmailTempViewHolder> {

    private ArrayList<RefRlc> dataArrayList;
    private Context context;

    public RefRlcAdapter(ArrayList<RefRlc> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_rlc, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefRlc value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.deskripsi.setText(value.getShort_desc());
        holder.admin.setText(value.getAdmin());
        holder.superuser.setText(value.getSuperuser());
        holder.reference.setText(value.getReferenced());
        holder.sendnotif.setText(value.getNotif());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefRlcEditActivity.class);
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

        private TextView no, deskripsi, admin, superuser, reference, sendnotif;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            deskripsi = (TextView) itemView.findViewById(R.id.deskripsi);
            admin = (TextView) itemView.findViewById(R.id.admin);
            superuser = (TextView) itemView.findViewById(R.id.superuser);
            reference = (TextView) itemView.findViewById(R.id.reference);
            sendnotif = (TextView) itemView.findViewById(R.id.sendnotif);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
