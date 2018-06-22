package com.tamboraagungmakmur.winwin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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
public class SidebarMenuRight4 extends Fragment {


    @Bind(R.id.lvSidebar)
    ListView lvSidebar;

    private FragmentActivity mActivity;
    private SidebarAdapter adapter;
    private ArrayList<SidebarModel> dataSet = new ArrayList<>();

    public SidebarMenuRight4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sidebar_menu_right4, container, false);
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
        SessionManager sess = new SessionManager(mActivity);
        AndLog.ShowLog(SidebarMenu2.class.getSimpleName(), sess.getIdMenu() + " menu");
        switch (sess.getIdMenu()) {
            case 0:
                dataSet.add(new SidebarModel(0, "Daftar Template Email", 1));
                dataSet.add(new SidebarModel(0, getString(R.string.daftar_rekom_kota), 1));
                dataSet.add(new SidebarModel(0, getString(R.string.daftar_bank_klien), 1));
                dataSet.add(new SidebarModel(0, getString(R.string.daftar_pekerjaan), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_hubungan_keluarga), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_proses_import), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_jenis_kelamin), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_status_klien), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_tahap_pengajuan), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_tahap_pencairan), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_status_pengajuan), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_status_testimoni), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_jenis_pembayaran), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_media_pembayaran), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_syarat_daftar), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_syarat_pengajuan), 1));
                break;
            case 1:
                dataSet.add(new SidebarModel(0, getString(R.string.daftar_pekerjaan), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_media_bayar), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_hubungan_keluarga), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_proses_import), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_jenis_kelamin), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_status_klien), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_tahap_pengajuan), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_tahap_pencairan), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_status_pengajuan), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_status_testimoni), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_jenis_pembayaran), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_media_pembayaran), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_syarat_daftar), 1));
//                dataSet.add(new SidebarModel(0, getString(R.string.daftar_syarat_pengajuan), 1));
                break;

        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
