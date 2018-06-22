package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Adapter.ViewPagerAdapter;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 8/14/2017.
 */

public class ArchiveParentJanjiBayar extends Fragment {

    @Bind(R.id.vPager)
    ViewPager vPager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.txTitle)
    TextView txTitle;

    private ViewPagerAdapter viewPagerAdapter;
    private FragmentActivity mActivity;
    private View view;

    public ArchiveParentJanjiBayar() {
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
            view = inflater.inflate(R.layout.fragment_archive_parent, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragments(new PinjamanJanjiBayar(), getString(R.string.pengajuan));
        viewPagerAdapter.addFragments(new PencairanJanjiBayar(), getString(R.string.pencarian));
        viewPagerAdapter.addFragments(new PembayaranJanjiBayar(), getString(R.string.pembayaran));
        viewPagerAdapter.addFragments(new PerpanjanganJanjiBayar(), getString(R.string.perpanjangan));
        viewPagerAdapter.addFragments(new DendaJanjiBayar(), getString(R.string.denda));
        vPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(vPager);
        vPager.setOffscreenPageLimit(tabLayout.getTabCount());
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String message = "";
                switch (position) {
                    case 0:
                        message = "Pengajuan Janji Bayar";
                        break;
                    case 1:
                        message = "Pencairan Janji Bayar";
                        break;
                    case 2:
                        message = "Pembayaran Janji Bayar";
                        break;
                    case 3:
                        message = "Perpanjangan Janji Bayar";
                        break;
                    case 4:
                        message = "Denda Janji Bayar";
                        break;
                }
                Intent intent = new Intent("title");
                intent.putExtra("message", message);
                LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vPager.setCurrentItem(0);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onNotice);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(onNotice, new IntentFilter(getString(R.string.archive)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onNotice);
    }


    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            SetTitle();


        }
    };


    private void SetTitle() {

        SessionManager sess = new SessionManager(mActivity);


        if (sess.getTitle() != null) {
            txTitle.setText(sess.getTitle());

        } else {
            txTitle.setText(getString(R.string.daftar_pengajuan));
        }

        sess.setTitle(null);
        vPager.setCurrentItem(0);

    }

}
