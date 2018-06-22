package com.tamboraagungmakmur.winwin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Model.SidebarModel;
import com.tamboraagungmakmur.winwin.R;

import java.util.ArrayList;

/**
 * Created by Tambora on 23/02/2017.
 */
public class SidebarAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SidebarModel> mDataSource;

    public SidebarAdapter(Context context, ArrayList<SidebarModel> items) {
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

        View rowView = mInflater.inflate(R.layout.list_sidebar, parent, false);

        SidebarModel value = mDataSource.get(position);
        ImageView imgLogo = (ImageView) rowView.findViewById(R.id.imgLogo);
        TextView txTitle = (TextView) rowView.findViewById(R.id.txTitle);
        ImageView imgArrow = (ImageView) rowView.findViewById(R.id.imgArrowRight);

        imgLogo.setImageResource(value.getImg());
        txTitle.setText(value.getTitle());
        if (value.getCount() > 1) {
            imgArrow.setVisibility(View.VISIBLE);
        } else {
            imgArrow.setVisibility(View.GONE);
        }

        return rowView;
    }
}
