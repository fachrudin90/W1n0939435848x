package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.InvestlainDelActivity;
import com.tamboraagungmakmur.winwin.Model.Investarik;
import com.tamboraagungmakmur.winwin.Model.Investlain;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;

import java.util.ArrayList;

/**
 * Created by innan on 12/14/2017.
 */

public class InvestlainAdapter extends RecyclerView.Adapter<InvestlainAdapter.InvestorViewHolder> {

    private Context context;

    public InvestlainAdapter(ArrayList<Investlain> investasiInvestorArrayList) {
        this.investasiInvestorArrayList = investasiInvestorArrayList;
    }

    private ArrayList<Investlain> investasiInvestorArrayList;

    @Override
    public InvestorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_investlain, parent, false);
        InvestorViewHolder holder = new InvestorViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(InvestorViewHolder holder, int position) {

        final Investlain investor = investasiInvestorArrayList.get(position);

        holder.tx1.setText(investor.getInvestrxlain_tanggal());
        holder.tx2.setText(investor.getInvest_nomor_investasi());
        holder.tx3.setText(investor.getInvestor_nomor_investor() + "\n" + investor.getInvestor_nama());
        holder.tx4.setText(new FormatPrice().format(investor.getInvestrxlain_nominal()));
        holder.tx5.setText(investor.getTypetrxinv_label());
        holder.tx7.setText(investor.getInvestrxlain_note());

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestlainDelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestrxlain_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return investasiInvestorArrayList.size();
    }

    public class InvestorViewHolder extends RecyclerView.ViewHolder {

        private TextView tx1, tx2, tx3, tx4, tx5, tx7;
        private ImageButton btDelete;

        public InvestorViewHolder(View itemView) {
            super(itemView);

            tx1 = (TextView) itemView.findViewById(R.id.tx1);
            tx2 = (TextView) itemView.findViewById(R.id.tx2);
            tx3 = (TextView) itemView.findViewById(R.id.tx3);
            tx4 = (TextView) itemView.findViewById(R.id.tx4);
            tx5 = (TextView) itemView.findViewById(R.id.tx5);
            tx7 = (TextView) itemView.findViewById(R.id.tx7);
            btDelete = (ImageButton) itemView.findViewById(R.id.btDelete);
        }
    }
}
