package com.tamboraagungmakmur.winwin;


import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.Adapter.ViewPagerAdapter;
import com.tamboraagungmakmur.winwin.Model.TabModel;
import com.tamboraagungmakmur.winwin.Model.TabsModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeParent extends Fragment {


    @Bind(R.id.vPager)
    ViewPager vPager;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    private ViewPagerAdapter viewPagerAdapter;
    private FragmentActivity mActivity;

    private ArrayList<TabsModel> dataTab;
    private ArrayList<TabModel> tabModels;

    private View view;

    public HomeParent() {
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
            view = inflater.inflate(R.layout.fragment_home_parent, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);


        dataTab = new ArrayList<>();
        tabModels = new ArrayList<>();
        dataTab.add(new TabsModel(R.drawable.ic_analisa_white, R.drawable.ic_analisa_grey, Color.parseColor("#5d9bfb")));
        dataTab.add(new TabsModel(R.drawable.ic_verifikasi_white, R.drawable.ic_verifikasi_grey, Color.parseColor("#6fd64b")));
        dataTab.add(new TabsModel(R.drawable.homejatuhhempo2, R.drawable.homejatuhtempo, Color.parseColor("#7751c8")));
        dataTab.add(new TabsModel(R.drawable.ic_colection_white, R.drawable.ic_colection_grey, Color.parseColor("#fb5d5d")));
        dataTab.add(new TabsModel(R.drawable.ic_others_white, R.drawable.ic_others_grey, Color.parseColor("#f7941d")));


        mActivity = getActivity();
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragments(new HomeAnalisa(), getString(R.string.analisa));
        viewPagerAdapter.addFragments(new HomeVerifikasi(), getString(R.string.verifikasi));
        viewPagerAdapter.addFragments(new HomeJatuhTempo(), getString(R.string.jatuh_tempo));
        viewPagerAdapter.addFragments(new HomeCollection(), getString(R.string.collection));
        viewPagerAdapter.addFragments(new HomeOther(), getString(R.string.other));
        vPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(vPager);
        vPager.setOffscreenPageLimit(tabLayout.getTabCount());

        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String message = "";
                switch (position) {
                    case 0:
                        message = "Task Analisa";
                        break;
                    case 1:
                        message = "Task Verifikasi";
                        break;
                    case 2:
                        message = "Task Jatuh Tempo";
                        break;
                    case 3:
                        message = "Task Collection";
                        break;
                    case 4:
                        message = "Task Other";
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

        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(mActivity).inflate(R.layout.tabs_custom_divider, tabLayout, false);

            LinearLayout tabLiner = (LinearLayout) relativeLayout.findViewById(R.id.lyTab);
            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            ImageView tabImage = (ImageView) relativeLayout.findViewById(R.id.tab_image);
            tabModels.add(new TabModel(tabImage, tabTextView, tabLiner));

            tabImage.setImageResource(dataTab.get(i).getImgUnselected());
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            tab.select();

        }

        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {


                TabsModel tabDetail = dataTab.get(position);
                TabModel tabLy = tabModels.get(position);

                for (int i = 0; i < tabModels.size(); i++) {

                    TabModel value = tabModels.get(i);
                    TabsModel value2 = dataTab.get(i);
                    value.getLyTab().setBackgroundColor(Color.WHITE);
                    value.getImgIcon().setImageResource(value2.getImgUnselected());
                    value.getTxTitle().setTextColor(Color.GRAY);

                }

                tabLy.getLyTab().setBackgroundColor(tabDetail.getBgColor());
                tabLy.getImgIcon().setImageResource(tabDetail.getImgSelected());
                tabLy.getTxTitle().setTextColor(Color.WHITE);


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
}
