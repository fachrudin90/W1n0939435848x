package com.tamboraagungmakmur.winwin;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class SidebarMenu2 extends Fragment {


    @Bind(R.id.lvSidebar)
    ListView lvSidebar;

    private FragmentActivity mActivity;
    private SidebarAdapter adapter;
    private ArrayList<SidebarModel> dataSet = new ArrayList<>();
    private View view;

    public SidebarMenu2() {
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
            view = inflater.inflate(R.layout.fragment_sidebar_menu2, container, false);
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
            case 6:
                dataSet.add(new SidebarModel(R.drawable.iconmenudatapembayaran, getString(R.string.data_pembayaran), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenudatapencairan, getString(R.string.data_pencairan), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenudatarewardreferral, getString(R.string.data_trf), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenudatapembayaran, getString(R.string.upload_pembayaran), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenudatapencairan, getString(R.string.upload_pencairan), 1));
                break;
            case 7:
                dataSet.add(new SidebarModel(R.drawable.listinvestor, "List Investor", 1));
                dataSet.add(new SidebarModel(R.drawable.listinvestasi, "List Investasi", 1));
                dataSet.add(new SidebarModel(R.drawable.listpermintaantarikan, "List Permintaan Tarikan", 1));
                dataSet.add(new SidebarModel(R.drawable.listpencairanbunga, "List Pencairan Bunga & Pokok", 1));
                dataSet.add(new SidebarModel(R.drawable.listtransaksilain, "List Transaksi Lain", 1));
                break;
            case 2:
                dataSet.add(new SidebarModel(R.drawable.iconmenukirimemail, getString(R.string.kirim_email), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenukirimsms, getString(R.string.kirim_sms), 1));
                break;
            case 4:
                dataSet.add(new SidebarModel(R.drawable.iconmenulaporanakunting, getString(R.string.lap_akunting), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenulaporaninternal, getString(R.string.lap_internal), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenulaporanoperator, getString(R.string.lap_operator), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenulaporansimulasi, getString(R.string.lap_simulasi), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenulaporanaktivitas, getString(R.string.lap_aktivitas), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.lap_nasabah), 1));
                break;
            case 1:
                dataSet.add(new SidebarModel(R.drawable.iconmenupengajuan, getString(R.string.pengajuan), 4));
                dataSet.add(new SidebarModel(R.drawable.iconmenuaktif, getString(R.string.aktif), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenunonaktif, getString(R.string.non_aktif), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenulunas, getString(R.string.lunas), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenujatuhtempo, getString(R.string.jatuh_tempo), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenubayarsebagian, getString(R.string.bayar_sebagian), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenucollection, getString(R.string.collection), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenudebtcollector, getString(R.string.debt_collector), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenubaddebt, "Lost / Bad Debt", 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenujanjibayar, getString(R.string.janji_bayar), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenulenyap, getString(R.string.lenyap), 1));
                dataSet.add(new SidebarModel(R.drawable.iconmenucicilan, getString(R.string.cicilan), 1));
//                dataSet.add(new SidebarModel(R.drawable.iconmenujanji, "Janji Bayar", 1));

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
