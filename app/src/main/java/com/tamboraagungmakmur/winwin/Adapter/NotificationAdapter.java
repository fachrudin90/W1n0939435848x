package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.NotifiactionModel;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by Tambora on 23/02/2017.
 */
public class NotificationAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NotifiactionModel> mDataSource;

    public NotificationAdapter(Context context, ArrayList<NotifiactionModel> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.list_notification, parent, false);

        NotifiactionModel value = mDataSource.get(position);
        ImageView imgLogo = (ImageView) rowView.findViewById(R.id.imgLogo);
        TextView txTitle = (TextView) rowView.findViewById(R.id.txTitle);
        TextView txTime = (TextView) rowView.findViewById(R.id.txTime);

        txTitle.setText(value.getTitle());
        txTime.setText(value.getTimes());


        return rowView;
    }
}
