package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.InvestDelActivity;
import com.tamboraagungmakmur.winwin.InvestEditActivity;
import com.tamboraagungmakmur.winwin.InvestSelesaiActivity;
import com.tamboraagungmakmur.winwin.InvestVerifActivity;
import com.tamboraagungmakmur.winwin.Model.Invest;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;

import java.util.ArrayList;

/**
 * Created by innan on 12/8/2017.
 */

public class InvestAdapter extends RecyclerView.Adapter<InvestAdapter.InvestorViewHolder> {

    private Context context;

    public InvestAdapter(ArrayList<Invest> investasiInvestorArrayList) {
        this.investasiInvestorArrayList = investasiInvestorArrayList;
    }

    private ArrayList<Invest> investasiInvestorArrayList;

    @Override
    public InvestorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_investor, parent, false);
        InvestorViewHolder holder = new InvestorViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(InvestorViewHolder holder, int position) {

        final Invest investor = investasiInvestorArrayList.get(position);

        holder.tx1.setText(investor.getInvest_tgl_terima());
        holder.tx2.setText(investor.getInvest_tgl_start());
        holder.tx3.setText(investor.getInvest_nomor_investasi());
        holder.tx4.setText(investor.getInvestor_nama());
        holder.tx5.setText(new FormatPrice().format(investor.getInvest_nominal()));
        holder.tx6.setText(new FormatPrice().format(investor.getInvest_total_kembali()));

        if (investor.getInvest_is_finish().compareTo("false") == 0) {
            if (investor.getInvest_approve().compareTo("true") == 0) {
                holder.tx7.setText("Sedang berjalan");
                holder.selesai.setVisibility(View.VISIBLE);
                holder.tx7.setVisibility(View.VISIBLE);
                holder.selesai.setText("Sedang Berjalan");
                holder.btView.setVisibility(View.GONE);
                holder.btDelete.setVisibility(View.GONE);

                holder.selesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, InvestSelesaiActivity.class);
                        intent.putExtra("id", investor.getInvest_id());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                });
            } else {
                holder.tx7.setVisibility(View.GONE);
                holder.selesai.setText("Verifikasi");
                holder.selesai.setVisibility(View.VISIBLE);
                holder.btView.setVisibility(View.VISIBLE);
                holder.btDelete.setVisibility(View.VISIBLE);

                holder.selesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, InvestVerifActivity.class);
                        intent.putExtra("id", investor.getInvest_id());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                });
            }
        } else {
            holder.tx7.setVisibility(View.VISIBLE);
            holder.tx7.setText("Selesai");
            holder.selesai.setVisibility(View.GONE);
            holder.btView.setVisibility(View.GONE);
            holder.btDelete.setVisibility(View.GONE);
        }

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestDelActivity.class);
                intent.putExtra("id", investor.getInvest_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestEditActivity.class);
                intent.putExtra("id", investor.getInvest_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return investasiInvestorArrayList.size();
    }

    public class InvestorViewHolder extends RecyclerView.ViewHolder {

        private TextView tx1, tx2, tx3, tx4, tx5, tx6, tx7;
        private Button selesai;
        private ImageButton btView, btDelete;

        public InvestorViewHolder(View itemView) {
            super(itemView);

            tx1 = (TextView) itemView.findViewById(R.id.tx1);
            tx2 = (TextView) itemView.findViewById(R.id.tx2);
            tx3 = (TextView) itemView.findViewById(R.id.tx3);
            tx4 = (TextView) itemView.findViewById(R.id.tx4);
            tx5 = (TextView) itemView.findViewById(R.id.tx5);
            tx6 = (TextView) itemView.findViewById(R.id.tx6);
            tx7 = (TextView) itemView.findViewById(R.id.tx7);
            selesai = (Button) itemView.findViewById(R.id.selesai);
            btView = (ImageButton) itemView.findViewById(R.id.btView);
            btDelete = (ImageButton) itemView.findViewById(R.id.btDelete);
        }
    }
}