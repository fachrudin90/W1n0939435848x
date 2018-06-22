package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.HistoriDendDelActivity;
import com.tamboraagungmakmur.winwin.HistoriPembDelActivity;
import com.tamboraagungmakmur.winwin.Model.Denda;
import com.tamboraagungmakmur.winwin.Model.Denda1;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by innan on 9/12/2017.
 */

public class HistoriDendaAdapter extends RecyclerView.Adapter<HistoriDendaAdapter.HistoriViewHolder> {

    private ArrayList<Denda1> denda1ArrayList;
    private Context context;

    public HistoriDendaAdapter(ArrayList<Denda1> denda1ArrayList) {
        this.denda1ArrayList = denda1ArrayList;
    }

    @Override
    public HistoriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historidend, parent, false);
        HistoriViewHolder holder = new HistoriViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoriViewHolder holder, final int position) {

        Denda1 denda1 = denda1ArrayList.get(position);
        holder.date.setText(denda1.getCreated_at());
        holder.amount.setText(new FormatPrice().format(denda1.getAmount()));
        holder.via.setText("Diinput oleh: " + denda1.getCreated_by());
        holder.catatan.setText("Catatan: " + denda1.getNote());

        SessionManager sessionManager = new SessionManager(context);
        if (!sessionManager.isDENDA()) {
            holder.delete.setVisibility(View.INVISIBLE);
        } else {
            holder.delete.setVisibility(View.VISIBLE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HistoriDendDelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", denda1ArrayList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return denda1ArrayList.size();
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
