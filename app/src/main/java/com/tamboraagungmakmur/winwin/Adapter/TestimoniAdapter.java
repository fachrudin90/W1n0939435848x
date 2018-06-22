package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Testimoni;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.TestimoniTampilActivity;

import java.util.ArrayList;

/**
 * Created by innan on 9/13/2017.
 */

public class TestimoniAdapter extends RecyclerView.Adapter<TestimoniAdapter.TestimoniViewHolder> {

    public TestimoniAdapter(ArrayList<Testimoni> testimonis) {
        this.testimonis = testimonis;
    }

    private ArrayList<Testimoni> testimonis;
    private Context context;

    @Override
    public TestimoniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_testimoni, parent, false);
        TestimoniViewHolder holder = new TestimoniViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(TestimoniViewHolder holder, final int position) {

        Testimoni testimoni = testimonis.get(position);
        holder.username.setText(testimoni.getUsername());
        holder.nama.setText(testimoni.getNama());
        holder.alamat.setText(testimoni.getAlamat());
        holder.testimoni.setText(testimoni.getTestimoni());

        if (testimoni.getStatus().compareTo("Tidak tampil") == 0) {
            holder.pilihan.setText("Set Tampil");
            holder.pilihan.setTextColor(context.getResources().getColor(R.color.colorBgBlue));
            holder.pilihan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TestimoniTampilActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", testimonis.get(position).getId());
                    context.startActivity(intent);
                }
            });
        } else {
            holder.pilihan.setText(testimoni.getStatus());
            holder.pilihan.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return testimonis.size();
    }

    public class TestimoniViewHolder extends RecyclerView.ViewHolder {

        private TextView username, nama, alamat, testimoni, pilihan;

        public TestimoniViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            nama = (TextView) itemView.findViewById(R.id.nama);
            alamat = (TextView) itemView.findViewById(R.id.alamat);
            testimoni = (TextView) itemView.findViewById(R.id.testimoni);
            pilihan = (TextView) itemView.findViewById(R.id.pilihan);

        }
    }
}
