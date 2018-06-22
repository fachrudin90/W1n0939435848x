package com.tamboraagungmakmur.winwin.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.KlienReward;
import com.tamboraagungmakmur.winwin.Model.KlienRewardModel;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class KlienRewardAdapter extends RecyclerView.Adapter<KlienRewardAdapter.MyViewHolder> {


    private ArrayList<KlienReward> dataSet;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txNopel)
        TextView txNopel;
        @Bind(R.id.txNama)
        TextView txNama;
        @Bind(R.id.txJmlRef)
        TextView txJmlRef;
        @Bind(R.id.txAksi)
        TextView txAksi;
        @Bind(R.id.lyTable)
        LinearLayout lyTable;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public KlienRewardAdapter(Context context, ArrayList<KlienReward> data) {
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_klien_reward, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final KlienReward value = dataSet.get(listPosition);

        holder.txNopel.setText(value.getNomor_pelanggan());
        holder.txNama.setText(value.getNama_lengkap());
        holder.txJmlRef.setText(value.getJumlah_referral());

        if ((listPosition >= getItemCount() - 1))
            load();

    }

    public void load() {

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


}


