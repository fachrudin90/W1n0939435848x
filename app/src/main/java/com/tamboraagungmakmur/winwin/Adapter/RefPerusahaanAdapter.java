package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefBank;
import com.tamboraagungmakmur.winwin.Model.RefPerusahaan;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefBankEditActivity;
import com.tamboraagungmakmur.winwin.RefPerusahaanEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefPerusahaanAdapter extends RecyclerView.Adapter<RefPerusahaanAdapter.EmailTempViewHolder> {

    private ArrayList<RefPerusahaan> dataArrayList;
    private Context context;

    public RefPerusahaanAdapter(ArrayList<RefPerusahaan> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_perusahaan, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefPerusahaan value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.nama.setText(value.getNama_perusahaan());
        holder.alamat.setText(value.getAlamat());
        holder.jenis.setText(value.getJenis_komoditi()+"\n"+value.getJenis_industri());
        holder.status.setText(value.getStat_rlc());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefPerusahaanEditActivity.class);
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

        private TextView no, nama, alamat, jenis, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            nama = (TextView) itemView.findViewById(R.id.nama);
            alamat = (TextView) itemView.findViewById(R.id.alamat);
            jenis = (TextView) itemView.findViewById(R.id.jenis);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
