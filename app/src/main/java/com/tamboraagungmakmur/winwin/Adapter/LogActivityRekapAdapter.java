package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.LogActivityDetail;
import com.tamboraagungmakmur.winwin.Model.LogActivityRekap;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 8/31/2017.
 */

public class LogActivityRekapAdapter extends RecyclerView.Adapter<LogActivityRekapAdapter.AkuntingViewHolder> {

    private ArrayList<LogActivityRekap> akuntingArrayList;
    private Context context;

    public LogActivityRekapAdapter(ArrayList<LogActivityRekap> akuntingArrayList) {
        this.akuntingArrayList = akuntingArrayList;
    }

    @Override
    public AkuntingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logactivityrekap, parent, false);
        AkuntingViewHolder holder = new AkuntingViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(AkuntingViewHolder holder, int position) {

        final LogActivityRekap akunting = akuntingArrayList.get(position);
        holder.user.setText(akunting.getMember_user());
        holder.setujui.setText(akunting.getSetujui());
        holder.tolak.setText(akunting.getTolak());
        holder.profil.setText(akunting.getProfil());
        holder.telat.setText(akunting.getTelat());



    }

    @Override
    public int getItemCount() {
        return akuntingArrayList.size();
    }

    public class AkuntingViewHolder extends RecyclerView.ViewHolder {

        private TextView user, setujui, tolak, profil, telat;

        public AkuntingViewHolder(View itemView) {
            super(itemView);

            user = (TextView) itemView.findViewById(R.id.user);
            setujui = (TextView) itemView.findViewById(R.id.setujui);
            tolak = (TextView) itemView.findViewById(R.id.tolak);
            profil = (TextView) itemView.findViewById(R.id.profil);
            telat = (TextView) itemView.findViewById(R.id.telat);



        }
    }
}
