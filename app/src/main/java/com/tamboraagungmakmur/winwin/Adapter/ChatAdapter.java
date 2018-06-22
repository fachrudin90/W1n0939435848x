package com.tamboraagungmakmur.winwin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.Chat;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by innan on 10/23/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<Chat> chatArrayList;

    public ChatAdapter(ArrayList<Chat> chatArrayList) {
        this.chatArrayList = chatArrayList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        ChatViewHolder holder = new ChatViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {

        Chat chat = chatArrayList.get(position);
        String[] time = chat.getTime_post().split(" ");

        if (chat.getSender().compareTo("admin") == 0) {
            holder.rel1.setVisibility(View.GONE);
            holder.rel2.setVisibility(View.VISIBLE);

            holder.message1.setText(chat.getMessage());
            holder.time1.setText(time[1]);
        } else {
            holder.rel2.setVisibility(View.GONE);
            holder.rel1.setVisibility(View.VISIBLE);

            holder.message.setText(chat.getMessage());
            holder.time.setText(time[1]);
        }

    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rel1, rel2;
        private TextView message, time, message1, time1;

        public ChatViewHolder(View itemView) {
            super(itemView);

            rel1 = (RelativeLayout) itemView.findViewById(R.id.rel1);
            rel2 = (RelativeLayout) itemView.findViewById(R.id.rel2);
            message = (TextView) itemView.findViewById(R.id.message);
            message1 = (TextView) itemView.findViewById(R.id.message1);
            time = (TextView) itemView.findViewById(R.id.time);
            time1 = (TextView) itemView.findViewById(R.id.time1);
        }
    }
}
