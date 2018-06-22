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
import com.tamboraagungmakmur.winwin.InvestorNotesActivity;
import com.tamboraagungmakmur.winwin.InvestorVerifActivity;
import com.tamboraagungmakmur.winwin.Model.Investor;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by innan on 12/7/2017.
 */

public class InvestorAdapter extends RecyclerView.Adapter<InvestorAdapter.InvestorViewHolder> {

    private Context context;

    public InvestorAdapter(ArrayList<Investor> investasiInvestorArrayList) {
        this.investasiInvestorArrayList = investasiInvestorArrayList;
    }

    private ArrayList<Investor> investasiInvestorArrayList;

    @Override
    public InvestorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_investor1, parent, false);
        InvestorViewHolder holder = new InvestorViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(InvestorViewHolder holder, int position) {

        final Investor investor = investasiInvestorArrayList.get(position);

        holder.tx1.setText(investor.getInvestor_nomor_investor());
        holder.tx2.setText(investor.getInvestor_email());
        holder.tx3.setText(investor.getInvestor_nama());
        holder.tx4.setText(investor.getInvestor_alamat() + "\n" + investor.getInvestor_handphone());
        holder.tx5.setText(investor.getInvestor_nomor_rekening());
        holder.tx7.setText(investor.getRef_inv_stat_label());

        if (investor.getInvestor_id_status().compareTo("1") == 0) {
            holder.verifikasi.setVisibility(View.VISIBLE);
        } else {
            holder.verifikasi.setVisibility(View.GONE);
        }

        if (investor.getInvestor_id_status().compareTo("3") == 0) {
            holder.btInvalid.setVisibility(View.INVISIBLE);
            holder.bukaBlokir.setVisibility(View.VISIBLE);
        } else {
            holder.btInvalid.setVisibility(View.VISIBLE);
            holder.bukaBlokir.setVisibility(View.GONE);

        }


        holder.verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestorVerifActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestor_id());
                context.startActivity(intent);
            }
        });

        holder.lihatCatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestorNotesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestor_id());
                intent.putExtra("keterangan", "Catatan "+investor.getInvestor_nomor_investor()+" - "+investor.getInvestor_nama());
                context.startActivity(intent);
            }
        });

        holder.bukaBlokir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestorVerifActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestor_id());
                context.startActivity(intent);
            }
        });

        holder.btInvalid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestorInvalidActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestor_id());
                context.startActivity(intent);
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.rvDocs.setLayoutManager(linearLayoutManager);
        InvestorDocsAdapter adapter = new InvestorDocsAdapter(new ArrayList<>(Arrays.asList(investor.getDocs())));
        holder.rvDocs.setAdapter(adapter);

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestorEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestor_id());
                context.startActivity(intent);
            }
        });

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InvestorDetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", investor.getInvestor_id());
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
        private RecyclerView rvDocs;
        private Button verifikasi, bukaBlokir,lihatCatatan;
        private ImageButton btView, btEdit, btInvalid;

        public InvestorViewHolder(View itemView) {
            super(itemView);

            tx1 = (TextView) itemView.findViewById(R.id.tx1);
            tx2 = (TextView) itemView.findViewById(R.id.tx2);
            tx3 = (TextView) itemView.findViewById(R.id.tx3);
            tx4 = (TextView) itemView.findViewById(R.id.tx4);
            tx5 = (TextView) itemView.findViewById(R.id.tx5);
            tx7 = (TextView) itemView.findViewById(R.id.tx7);
            rvDocs = (RecyclerView) itemView.findViewById(R.id.rv_docs);
            verifikasi = (Button) itemView.findViewById(R.id.verifikasi);
            bukaBlokir = (Button) itemView.findViewById(R.id.bukablokir);
            btView = (ImageButton) itemView.findViewById(R.id.btView);
            btEdit = (ImageButton) itemView.findViewById(R.id.btEdit);
            btInvalid = (ImageButton) itemView.findViewById(R.id.btInvalid);
            lihatCatatan = (Button) itemView.findViewById(R.id.lihatCatatan);
        }
    }
}
