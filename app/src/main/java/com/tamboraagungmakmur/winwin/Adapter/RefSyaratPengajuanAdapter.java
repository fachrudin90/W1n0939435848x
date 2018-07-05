package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.RefSyaratDaftar;
import com.tamboraagungmakmur.winwin.Model.RefSyaratPengajuan;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 11/27/2017.
 */

public class RefSyaratPengajuanAdapter extends RecyclerView.Adapter<RefSyaratPengajuanAdapter.EmailTempViewHolder> {

    private ArrayList<RefSyaratPengajuan> dataArrayList;
    private Context context;

    public RefSyaratPengajuanAdapter(ArrayList<RefSyaratPengajuan> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ref_syarat_pengajuan, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        final RefSyaratPengajuan value = dataArrayList.get(position);

        holder.no.setText("" + (position+1));
        holder.noId.setText(value.getId());
        holder.namaJenis.setText(value.getJenis_id());
        holder.keterangan.setText(value.getKeterangan());
        holder.catatan.setText(value.getCatatan());
        holder.status.setText(value.getRlc());

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

        private TextView no, noId, namaJenis, keterangan, catatan, status;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            noId = (TextView) itemView.findViewById(R.id.noId);
            namaJenis = (TextView) itemView.findViewById(R.id.namaJenis);
            keterangan = (TextView) itemView.findViewById(R.id.keterangan);
            catatan = (TextView) itemView.findViewById(R.id.catatan);
            status = (TextView) itemView.findViewById(R.id.status);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
