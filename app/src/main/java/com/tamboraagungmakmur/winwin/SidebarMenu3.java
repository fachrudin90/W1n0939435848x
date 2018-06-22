package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tamboraagungmakmur.winwin.Adapter.SidebarAdapter;
import com.tamboraagungmakmur.winwin.Model.SidebarModel;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 10/3/2017.
 */

public class SidebarMenu3 extends Fragment {

    @Bind(R.id.lvSidebar)
    ListView lvSidebar;

    private FragmentActivity mActivity;
    private SidebarAdapter adapter;
    private ArrayList<SidebarModel> dataSet = new ArrayList<>();
    private View view;

    public SidebarMenu3() {
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
            view = inflater.inflate(R.layout.fragment_sidebar_menu3, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);
        mActivity = getActivity();
        adapter = new SidebarAdapter(mActivity, dataSet);
        lvSidebar.setAdapter(adapter);
        lvSidebar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


//                SessionManager sess = new SessionManager(mActivity);
//                Intent i = new Intent(getString(R.string.sidebar));
//                i.putExtra(getString(R.string.index), sess.getIdMenu());
//                i.putExtra(getString(R.string.menu), position);
//                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(i);

                SessionManager sess = new SessionManager(mActivity);
                if (sess.getIdMenu() == 6) {
                    sess.setTitle(getString(R.string.pengajuan) + " " + dataSet.get(position).getTitle());
                }

                AndLog.ShowLog("Sidebars", sess.getIdMenu() + " " + new SessionManager(mActivity).getTitle());

                Intent i = new Intent(getString(R.string.sidebar));
                i.putExtra(getString(R.string.index), position);
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(i);


            }
        });

        return view;
    }

    private void initSidebar(int index) {

        dataSet.clear();

        switch (index) {
            case 0:
                Log.d("sidebar", "ok");
                dataSet.add(new SidebarModel(R.drawable.iconmenupengajuanbaru, "Baru", 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenurepeatorder, "Repeat Order", 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenurekomsetuju, "Rekomendasi Disetujui", 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenurekontolak, "Rekomendasi Tolak", 1));
                break;

        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @Override
//    public void setUserVisibleHint(boolean isFragmentVisible_) {
//        super.setUserVisibleHint(true);
//
//
//        if (this.isVisible() && isFragmentVisible_) {
//
//            initSidebar();
//
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onNotice);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(onNotice, new IntentFilter(getString(R.string.sidebar)));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onNotice);

    }

    public BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                initSidebar(bundle.getInt(getString(R.string.index)));
            }

        }
    };

}
