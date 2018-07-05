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
import com.tamboraagungmakmur.winwin.Model.RefKodePos;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.RefBankEditActivity;
import com.tamboraagungmakmur.winwin.RefKodePosEditActivity;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefKodePosAdapter extends RecyclerView.Adapter<RefKodePosAdapter.EmailTempViewHolder> {

    private ArrayList<RefKodePos> dataArrayList;
    private Context context;

    public RefKodePosAdapter(ArrayList<RefKodePos> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_kodepos, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefKodePos value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.namaLabel.setText(value.getId());
        holder.namaKode.setText(value.getKodepos_kode());
        holder.namaKelurahan.setText(value.getKodepos_kelurahan());
        holder.namaKecamatan.setText(value.getKodepos_kecamatan());
        holder.jenis.setText(value.getKodepos_jenis());
        holder.namaKabupaten.setText(value.getKodepos_kabupaten());
        holder.namaProvinsi.setText(value.getKodepos_provinsi());
        holder.status.setText(value.getStat_rlc());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RefKodePosEditActivity.class);
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

        private TextView no, namaLabel, namaKode, namaKelurahan, namaKecamatan, jenis, namaKabupaten, namaProvinsi, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            namaLabel = (TextView) itemView.findViewById(R.id.namaLabel);
            namaKode = (TextView) itemView.findViewById(R.id.namaKode);
            namaKelurahan = (TextView) itemView.findViewById(R.id.namaKelurahan);
            namaKecamatan = (TextView) itemView.findViewById(R.id.namaKecamatan);
            jenis = (TextView) itemView.findViewById(R.id.jenis);
            namaKabupaten = (TextView) itemView.findViewById(R.id.namaKabupaten);
            namaProvinsi = (TextView) itemView.findViewById(R.id.namaProvinsi);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
