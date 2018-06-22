package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.LinksDeleteActivity;
import com.tamboraagungmakmur.winwin.Model.Links;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 9/28/2017.
 */

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.LinksViewHolder> {

    private ArrayList<Links> linksArrayList = new ArrayList<>();
    private Context context;

    public LinksAdapter(ArrayList<Links> linksArrayList) {
        this.linksArrayList = linksArrayList;
    }

    @Override
    public LinksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_links, parent, false);
        LinksViewHolder holder = new LinksViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(LinksViewHolder holder, int position) {

        final Links links = linksArrayList.get(position);
        holder.text1.setText(links.getNomor_pelanggan());
        holder.text2.setText(links.getNama_lengkap());
        holder.text3.setText(links.getType());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LinksDeleteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", links.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return linksArrayList.size();
    }

    public class LinksViewHolder extends RecyclerView.ViewHolder {

        private TextView text1, text2, text3;
        private ImageButton delete;

        public LinksViewHolder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
            text3 = (TextView) itemView.findViewById(R.id.text3);
            delete = (ImageButton) itemView.findViewById(R.id.btDelete);
        }
    }
}
