package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.LogActDetActivity;
import com.tamboraagungmakmur.winwin.Model.LogActivity;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;

import java.util.ArrayList;

/**
 * Created by innan on 10/11/2017.
 */

public class LogActivity1Adapter extends RecyclerView.Adapter<LogActivity1Adapter.AkuntingViewHolder> {

    private ArrayList<LogActivity> akuntingArrayList;
    private Context context;

    public LogActivity1Adapter(ArrayList<LogActivity> akuntingArrayList) {
        this.akuntingArrayList = akuntingArrayList;
    }

    @Override
    public AkuntingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logactivity1, parent, false);
        AkuntingViewHolder holder = new AkuntingViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(AkuntingViewHolder holder, int position) {

        final LogActivity akunting = akuntingArrayList.get(position);
        holder.no.setText("" + (position+1));
        holder.klien.setText(akunting.getLogin_time().split(" ")[1]);
        if (akunting.getLogout_time().compareTo("-") == 0) {
            holder.user.setText(akunting.getLogout_time());
        } else {
            holder.user.setText(akunting.getLogout_time().split(" ")[1]);
        }
        holder.aktif.setText(FormatDate.format(akunting.getDiff(), "H:m:s", "HH:mm:ss"));


    }

    @Override
    public int getItemCount() {
        return akuntingArrayList.size();
    }

    public class AkuntingViewHolder extends RecyclerView.ViewHolder {

        private TextView klien, no, user, aktif;

        public AkuntingViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            user = (TextView) itemView.findViewById(R.id.user);
            klien = (TextView) itemView.findViewById(R.id.klien);
            aktif = (TextView) itemView.findViewById(R.id.aktif);

        }
    }
}
