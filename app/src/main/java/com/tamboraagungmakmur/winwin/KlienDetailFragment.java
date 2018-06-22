package com.tamboraagungmakmur.winwin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamboraagungmakmur.winwin.Utils.ViewPagerAdapter;

/**
 * Created by innan on 10/9/2017.
 */

public class KlienDetailFragment extends Fragment {

    private View view;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    private String id;

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
            view = inflater.inflate(R.layout.fragment_kliendetail, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        id = getArguments().getString("id");
        initViewPager();

        return view;
    }

    private void initViewPager() {
        viewPager = (ViewPager) view.findViewById(R.id.vp_klien1);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout_klien1);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        KlienDataFragment klienDataFragment = new KlienDataFragment();
        KlienData1Fragment klienData1Fragment = new KlienData1Fragment();
        KlienData2Fragment klienData2Fragment = new KlienData2Fragment();
        KlienData3Fragment klienData3Fragment = new KlienData3Fragment();
        KlienData4Fragment klienData4Fragment = new KlienData4Fragment();
        klienDataFragment.setArguments(bundle);
        klienData1Fragment.setArguments(bundle);
        klienData2Fragment.setArguments(bundle);
        klienData3Fragment.setArguments(bundle);
        klienData4Fragment.setArguments(bundle);

        adapter.addFragments(klienDataFragment, "Data Klien");
        adapter.addFragments(klienData1Fragment, "Histori Aplikasi");
        adapter.addFragments(klienData2Fragment, "Dokumen Klien");
        adapter.addFragments(klienData3Fragment, "Linked Accounts");
        adapter.addFragments(klienData4Fragment, "Catatan");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
