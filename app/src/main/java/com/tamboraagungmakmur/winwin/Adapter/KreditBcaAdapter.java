package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.KreditPengajuanActivity;
import com.tamboraagungmakmur.winwin.ListPengajuanActivity;
import com.tamboraagungmakmur.winwin.Model.KreditBca;
import com.tamboraagungmakmur.winwin.Model.Testimoni;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.TestimoniTampilActivity;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;

import java.util.ArrayList;

/**
 * Created by innan on 10/4/2017.
 */

public class KreditBcaAdapter extends RecyclerView.Adapter<KreditBcaAdapter.TestimoniViewHolder> {

    public KreditBcaAdapter(ArrayList<KreditBca> testimonis) {
        this.testimonis = testimonis;
    }

    private ArrayList<KreditBca> testimonis;
    private Context context;

    @Override
    public TestimoniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kreditbca, parent, false);
        TestimoniViewHolder holder = new TestimoniViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(TestimoniViewHolder holder, final int position) {

        final KreditBca testimoni = testimonis.get(position);
        holder.username.setText(testimoni.getDate());
        holder.nama.setText(testimoni.getNote());
        holder.alamat.setText(new FormatPrice().format(testimoni.getAmount()));
        holder.testimoni.setText(testimoni.getMatch_loan());
        holder.pilihan.setText(testimoni.getStatus());

        if (testimoni.is_processable()) {
            if (testimoni.getMatch_loan() != null) {
                holder.testimoni.setVisibility(View.VISIBLE);
                holder.klien.setVisibility(View.GONE);
                holder.klien.setText(testimoni.getMatch_loan());
            } else {
                holder.testimoni.setVisibility(View.GONE);
                holder.klien.setVisibility(View.VISIBLE);
            }
        } else {
            holder.testimoni.setVisibility(View.VISIBLE);
            holder.klien.setVisibility(View.GONE);
        }

        holder.klien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KreditPengajuanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", testimoni.getId());
                intent.putExtra("id_upload", testimoni.getId_upload());
                intent.putExtra("note", testimoni.getNote());
                intent.putExtra("amount", testimoni.getAmount());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return testimonis.size();
    }

    public class TestimoniViewHolder extends RecyclerView.ViewHolder {

        private TextView username, nama, alamat, testimoni, pilihan;
        private Button klien;

        public TestimoniViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            nama = (TextView) itemView.findViewById(R.id.nama);
            alamat = (TextView) itemView.findViewById(R.id.alamat);
            testimoni = (TextView) itemView.findViewById(R.id.testimoni);
            pilihan = (TextView) itemView.findViewById(R.id.pilihan);

            klien = (Button) itemView.findViewById(R.id.klien);
        }
    }
}
