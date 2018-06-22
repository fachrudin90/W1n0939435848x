package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.HistoriPembDelActivity;
import com.tamboraagungmakmur.winwin.Model.Pembayaran1;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by innan on 9/12/2017.
 */

public class HistoriPembayaranAdapter extends RecyclerView.Adapter<HistoriPembayaranAdapter.HistoriViewHolder> {

    private ArrayList<Pembayaran1> pembayaran1ArrayList;
    private Context context;

    public HistoriPembayaranAdapter(ArrayList<Pembayaran1> pembayaran1ArrayList) {
        this.pembayaran1ArrayList = pembayaran1ArrayList;
    }

    @Override
    public HistoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historipemb, parent, false);
        HistoriViewHolder holder = new HistoriViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoriViewHolder holder, final int position) {

        Pembayaran1 pembayaran1 = pembayaran1ArrayList.get(position);
        holder.date.setText(pembayaran1.getCreated_at());
        holder.amount.setText("Pembayaran: " + pembayaran1.getAmount());
        holder.via.setText("Via: " + pembayaran1.getMedia());
        holder.catatan.setText("Catatan: " + pembayaran1.getNote());

        SessionManager sessionManager = new SessionManager(context);
        if (!sessionManager.isPELUNASAN()) {
            holder.delete.setVisibility(View.INVISIBLE);
        } else {
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HistoriPembDelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", pembayaran1ArrayList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pembayaran1ArrayList.size();
    }

    public class HistoriViewHolder extends RecyclerView.ViewHolder {

        private TextView date, amount, via, catatan;
        private ImageButton delete;

        public HistoriViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            amount = (TextView) itemView.findViewById(R.id.amount);
            via = (TextView) itemView.findViewById(R.id.via);
            catatan = (TextView) itemView.findViewById(R.id.catatan);
            delete = (ImageButton) itemView.findViewById(R.id.delete);
        }
    }
}
