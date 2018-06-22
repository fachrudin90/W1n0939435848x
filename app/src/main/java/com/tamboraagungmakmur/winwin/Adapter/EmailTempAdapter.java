package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.EmailTemplateRefAddActivity;
import com.tamboraagungmakmur.winwin.EmailTemplateRefEditActivity;
import com.tamboraagungmakmur.winwin.Model.EmailTemplate;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 11/10/2017.
 */

public class EmailTempAdapter extends RecyclerView.Adapter<EmailTempAdapter.EmailTempViewHolder> {

    private ArrayList<EmailTemplate> emailTemplateArrayList;
    private Context context;

    public EmailTempAdapter(ArrayList<EmailTemplate> emailTemplateArrayList) {
        this.emailTemplateArrayList = emailTemplateArrayList;
    }

    @Override
    public EmailTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_email_temp, parent, false);
        EmailTempViewHolder holder = new EmailTempViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(EmailTempViewHolder holder, final int position) {

        holder.no.setText("" + (position+1));
        holder.label.setText(emailTemplateArrayList.get(position).getLabel());
        holder.konten.setText(emailTemplateArrayList.get(position).getKonten());

        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EmailTemplateRefEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", emailTemplateArrayList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return emailTemplateArrayList.size();
    }

    public class EmailTempViewHolder extends RecyclerView.ViewHolder {

        private TextView no, label, konten;
        private ImageButton btView;

        public EmailTempViewHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            label = (TextView) itemView.findViewById(R.id.label);
            konten = (TextView) itemView.findViewById(R.id.konten);
            btView = (ImageButton) itemView.findViewById(R.id.btView);

        }
    }
}
