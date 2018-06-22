package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.tamboraagungmakmur.winwin.Adapter.ViewPagerAdapter;

/**
 * Created by innan on 11/7/2017.
 */

public class ArchiveActivity extends AppCompatActivity {

    private ImageButton btBack;

    private ViewPager vPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        id = getIntent().getStringExtra("id");
        initView();

    }

    private void initView() {
        btBack = (ImageButton) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        vPager = (ViewPager) findViewById(R.id.vPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        ArchivePengajuan archivePengajuan = new ArchivePengajuan();
        archivePengajuan.setArguments(bundle);
        ArchivePencarian archivePencarian = new ArchivePencarian();
        archivePencarian.setArguments(bundle);
        ArchivePembayaran archivePembayaran = new ArchivePembayaran();
        archivePembayaran.setArguments(bundle);
        ArchivePerpanjangan archivePerpanjangan = new ArchivePerpanjangan();
        archivePerpanjangan.setArguments(bundle);
        ArchiveDenda archiveDenda = new ArchiveDenda();
        archiveDenda.setArguments(bundle);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(archivePengajuan, getString(R.string.pengajuan));
        viewPagerAdapter.addFragments(archivePencarian, getString(R.string.pencarian));
        viewPagerAdapter.addFragments(archivePembayaran, getString(R.string.pembayaran));
        viewPagerAdapter.addFragments(archivePerpanjangan, getString(R.string.perpanjangan));
        viewPagerAdapter.addFragments(archiveDenda, getString(R.string.denda));
        vPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(vPager);
        vPager.setOffscreenPageLimit(tabLayout.getTabCount());

        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String message = "";
                switch (position) {
                    case 0:
                        message = "Archive Pengajuan";
                        break;
                    case 1:
                        message = "Archive Pencairan";
                        break;
                    case 2:
                        message = "Archive Pembayaran";
                        break;
                    case 3:
                        message = "Archive Perpanjangan";
                        break;
                    case 4:
                        message = "Archive Denda";
                        break;
                }
                Intent intent = new Intent("title");
                intent.putExtra("message", message);
                LocalBroadcastManager.getInstance(ArchiveActivity.this).sendBroadcast(intent);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        vPager.setCurrentItem(0);
    }

}
