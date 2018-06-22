package com.tamboraagungmakmur.winwin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tamboraagungmakmur.winwin.Adapter.SidebarAdapter;
import com.tamboraagungmakmur.winwin.Model.SidebarModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class SidebarMenuRight2 extends Fragment {


    @Bind(R.id.lvSidebar)
    ListView lvSidebar;

    private FragmentActivity mActivity;
    private SidebarAdapter adapter;
    private ArrayList<SidebarModel> dataSet = new ArrayList<>();

    private View view;
    public SidebarMenuRight2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_sidebar_menu_right2, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        adapter = new SidebarAdapter(mActivity, dataSet);
        lvSidebar.setAdapter(adapter);
        initSidebar();
        lvSidebar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                Intent i = new Intent(getString(R.string.sidebar_right));
                i.putExtra(getString(R.string.index), position);
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(i);


            }
        });

        return view;
    }

    private void initSidebar() {

        dataSet.clear();
        dataSet.add(new SidebarModel(0, getString(R.string.daftar_artikel), 1));
        dataSet.add(new SidebarModel(0, getString(R.string.daftar_testi), 1));
        dataSet.add(new SidebarModel(0, getString(R.string.karyawan_pengguna), 2));
        dataSet.add(new SidebarModel(0, getString(R.string.daftar_ref), 2));


        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
