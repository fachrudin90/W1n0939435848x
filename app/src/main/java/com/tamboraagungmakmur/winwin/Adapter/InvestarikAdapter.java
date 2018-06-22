package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.InvestarikApproveActivity;
import com.tamboraagungmakmur.winwin.InvestarikDisapproveActivity;
import com.tamboraagungmakmur.winwin.Model.Investarik;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;

import java.util.ArrayList;

/**
 * Created by innan on 12/8/2017.
 */

public class InvestarikAdapter extends RecyclerView.Adapter<InvestarikAdapter.InvestorViewHolder> {

    private Context context;

    public InvestarikAdapter(ArrayList<Investarik> investasiInvestorArrayList) {
        this.investasiInvestorArrayList = investasiInvestorArrayList;
    }

    private ArrayList<Investarik> investasiInvestorArrayList;

    @Override
    public InvestorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_investarik, parent, false);
        InvestorViewHolder holder = new InvestorViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(InvestorViewHolder holder, int position) {

        final Investarik investor = investasiInvestorArrayList.get(position);

        holder.tx1.setText(investor.getInvest_nomor_investasi());
        holder.tx2.setText(investor.getInvestor_nama());
        holder.tx3.setText(investor.getInvestarik_ajukan_tanggal() + " " + investor.getInvestarik_ajukan_waktu());
        holder.tx4.setText(new FormatPrice().format(investor.getInvestarik_ajukan_nominal()));
        holder.tx5.setText(investor.getInvestarik_ajukan_note());
        holder.tx8.setText(investor.getInvestarik_respond_note());
        if (investor.getInvestarik_respond_is().compareTo("true") == 0) {
            holder.tx6.setText("Sudah Direspon");
            holder.approve.setVisibility(View.GONE);
            holder.disapprove.setVisibility(View.GONE);
        } else {
            holder.tx6.setText("Belum Direspon");
            holder.approve.setVisibility(View.VISIBLE);
            holder.disapprove.setVisibility(View.VISIBLE);
        }

        if (investor.getInvestarik_respond_approve().compareTo("true") == 0) {
            holder.tx7.setText("Approve");
        } else {
            holder.tx7.setText("Not Approve");
        }

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestarikApproveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestarik_id());
                context.startActivity(intent);
            }
        });

        holder.disapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestarikDisapproveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestarik_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return investasiInvestorArrayList.size();
    }

    public class InvestorViewHolder extends RecyclerView.ViewHolder {

        private TextView tx1, tx2, tx3, tx4, tx5, tx6, tx7, tx8, approve, disapprove;

        public InvestorViewHolder(View itemView) {
            super(itemView);

            tx1 = (TextView) itemView.findViewById(R.id.tx1);
            tx2 = (TextView) itemView.findViewById(R.id.tx2);
            tx3 = (TextView) itemView.findViewById(R.id.tx3);
            tx4 = (TextView) itemView.findViewById(R.id.tx4);
            tx5 = (TextView) itemView.findViewById(R.id.tx5);
            tx6 = (TextView) itemView.findViewById(R.id.tx6);
            tx7 = (TextView) itemView.findViewById(R.id.tx7);
            tx8 = (TextView) itemView.findViewById(R.id.tx8);
            approve = (Button) itemView.findViewById(R.id.approve);
            disapprove = (Button) itemView.findViewById(R.id.disapprove);

        }
    }
}