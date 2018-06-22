package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.InvestorDetActivity;
import com.tamboraagungmakmur.winwin.InvestorEditActivity;
import com.tamboraagungmakmur.winwin.InvestorInvalidActivity;
import com.tamboraagungmakmur.winwin.InvestorVerifActivity;
import com.tamboraagungmakmur.winwin.Model.Investor;
import com.tamboraagungmakmur.winwin.Model.InvestorNotes;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by innan on 12/7/2017.
 */

public class InvestorNotesAdapter extends RecyclerView.Adapter<InvestorNotesAdapter.InvestorViewHolder> {

    private Context context;

    public InvestorNotesAdapter(ArrayList<InvestorNotes> investasiInvestorArrayList) {
        this.investasiInvestorArrayList = investasiInvestorArrayList;
    }

    private ArrayList<InvestorNotes> investasiInvestorArrayList;

    @Override
    public InvestorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_investornotes, parent, false);
        InvestorViewHolder holder = new InvestorViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(InvestorViewHolder holder, int position) {

        final InvestorNotes investor = investasiInvestorArrayList.get(position);

        holder.tx1.setText(investor.getNote_created_at());
        holder.tx2.setText(investor.getNote_catatan());
        holder.tx3.setText(investor.getKar_namalengkap());



        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletes(investor.getNote_id());
            }
        });


    }

    @Override
    public int getItemCount() {
        return investasiInvestorArrayList.size();
    }

    public class InvestorViewHolder extends RecyclerView.ViewHolder {

        private TextView tx1, tx2, tx3;
        private ImageButton btDelete;

        public InvestorViewHolder(View itemView) {
            super(itemView);

            tx1 = (TextView) itemView.findViewById(R.id.tx1);
            tx2 = (TextView) itemView.findViewById(R.id.tx2);
            tx3 = (TextView) itemView.findViewById(R.id.tx3);
            btDelete = (ImageButton) itemView.findViewById(R.id.btDelete);
        }
    }

    public void deletes(String id){

    }
}
