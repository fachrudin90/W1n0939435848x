package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.KlienAutoLinkActivity;
import com.tamboraagungmakmur.winwin.KlienDetailActivity;
import com.tamboraagungmakmur.winwin.Model.Klien1;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 9/27/2017.
 */

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {

    private ArrayList<Klien1> linkList;
    private Context context;

    public LinkAdapter(ArrayList<Klien1> linkList) {
        this.linkList = linkList;
    }

    @Override
    public LinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent, false);
        LinkViewHolder holder = new LinkViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(LinkViewHolder holder, final int position) {

        holder.text1.setText(linkList.get(position).getKlien());

        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KlienDetailActivity.class);
                intent.putExtra("id", linkList.get(position).getId1());
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        holder.autolilnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, KlienAutoLinkActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", linkList.get(position).getId());
                intent.putExtra("id1", linkList.get(position).getId1());
                intent.putExtra("type", linkList.get(position).getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }

    public class LinkViewHolder extends RecyclerView.ViewHolder {

        private TextView text1, autolilnk;

        public LinkViewHolder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(R.id.text1);
            autolilnk = (TextView) itemView.findViewById(R.id.autolink);
        }
    }
}
