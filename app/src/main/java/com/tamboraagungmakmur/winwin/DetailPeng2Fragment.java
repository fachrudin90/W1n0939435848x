package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tamboraagungmakmur.winwin.Adapter.ViewPagerAdapter;

/**
 * Created by innan on 9/11/2017.
 */

public class DetailPeng2Fragment extends Fragment {

    private View view;

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
            view = inflater.inflate(R.layout.fragment_infopeng2, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        Intent intent = new Intent("title");
        intent.putExtra("message", "Detail Pengajuan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailPeng2aFragment fragment = new DetailPeng2aFragment();
        Bundle bundle = new Bundle();
        Log.d("id_pengajuan1", getArguments().getString("id"));
        bundle.putString("id", getArguments().getString("id"));
        bundle.putString("nama", getArguments().getString("nama"));
        bundle.putString("id_klien", getArguments().getString("id_klien"));
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_peng2, fragment);
        fragmentTransaction.commitAllowingStateLoss();

        return view;
    }

}
