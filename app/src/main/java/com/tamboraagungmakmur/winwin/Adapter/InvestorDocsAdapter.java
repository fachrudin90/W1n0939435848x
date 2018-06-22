package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tamboraagungmakmur.winwin.KlienFileActivity;
import com.tamboraagungmakmur.winwin.Model.InvestorDoc;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.Utils.AppConf;

import java.util.ArrayList;

/**
 * Created by innan on 12/8/2017.
 */

public class InvestorDocsAdapter extends RecyclerView.Adapter<InvestorDocsAdapter.InvDocsViewHolder> {

    public InvestorDocsAdapter(ArrayList<InvestorDoc> investorDocArrayList) {
        this.investorDocArrayList = investorDocArrayList;
    }

    private ArrayList<InvestorDoc> investorDocArrayList;
    private Context context;

    @Override
    public InvDocsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invdoc, parent, false);
        InvDocsViewHolder holder = new InvDocsViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(InvDocsViewHolder holder, final int position) {

        holder.doc.setText(investorDocArrayList.get(position).getJnsfile_code());
        final String img = AppConf.BASE_URL_IMG + "/home/investors/" + investorDocArrayList.get(position).getInvestor_nomor_investor() + "/" + investorDocArrayList.get(position).getInv_doc_file();
        holder.doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KlienFileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("img", img);
                intent.putExtra("type", investorDocArrayList.get(position).getInv_doc_type_id());
                intent.putExtra("id", investorDocArrayList.get(position).getInv_doc_id());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return investorDocArrayList.size();
    }

    public class InvDocsViewHolder extends RecyclerView.ViewHolder {

        private Button doc;

        public InvDocsViewHolder(View itemView) {
            super(itemView);
            doc = (Button) itemView.findViewById(R.id.doc);
        }
    }
}
