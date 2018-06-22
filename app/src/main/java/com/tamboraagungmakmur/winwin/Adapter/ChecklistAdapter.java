package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Checklist;
import com.tamboraagungmakmur.winwin.Model.Checklist1;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 9/11/2017.
 */

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private ArrayList<Checklist1> checklists;
    private Context context;

    public ChecklistAdapter(ArrayList<Checklist1> checklists) {
        this.checklists = checklists;
    }

    @Override
    public ChecklistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist, parent, false);
        ChecklistViewHolder holder = new ChecklistViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(ChecklistViewHolder holder, int position) {

        final Checklist1 checklist = checklists.get(position);

        holder.text1.setText(checklists.get(position).getQuestion());
        if (checklists.get(position).getAnswer() != null) {
            if (checklists.get(position).getAnswer().compareTo("true") == 0) {
                holder.ya.setChecked(true);
                Intent intent = new Intent("checklist_pilih");
                intent.putExtra("id", checklist.getQuestion_id());
                intent.putExtra("answer", "true");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            } else {
                holder.tidak.setChecked(true);
                Intent intent = new Intent("checklist_pilih");
                intent.putExtra("id", checklist.getQuestion_id());
                intent.putExtra("answer", "false");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        }

        holder.ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("checklist_pilih");
                intent.putExtra("id", checklist.getQuestion_id());
                intent.putExtra("answer", "true");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        holder.tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("checklist_pilih");
                intent.putExtra("id", checklist.getQuestion_id());
                intent.putExtra("answer", "false");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checklists.size();
    }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder {

        private TextView text1;
        private RadioButton ya, tidak;

        public ChecklistViewHolder(View itemView) {
            super(itemView);

            text1 = (TextView) itemView.findViewById(R.id.text1);
            ya = (RadioButton) itemView.findViewById(R.id.ya);
            tidak = (RadioButton) itemView.findViewById(R.id.tidak);
        }
    }
}
