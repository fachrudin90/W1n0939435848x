package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.tamboraagungmakmur.winwin.Adapter.ViewPagerAdapter;
import com.tamboraagungmakmur.winwin.voip.VoiceCallBridge;

/**
 * Created by innan on 9/8/2017.
 */

public class PengajuanDetActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ImageButton btBack;

    private int tahap;
    private String idKlien, nama;
    public static String dari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuandet);
        initViewPager();

        dari = "";
        btBack = (ImageButton) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        idKlien = getIntent().getStringExtra("id_klien");
        nama = getIntent().getStringExtra("nama");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            if (bundle.containsKey("dari")) {
                dari = bundle.getString("dari");
            }
        }
//        REDLYST
        initFab();

    }

    private void initFab() {

        final FloatingActionMenu materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        FloatingActionButton floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        FloatingActionButton floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        FloatingActionButton floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);


//        FloatingActionButton floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item_data);
//        FloatingActionButton floatingActionButton5 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_input);
//        FloatingActionButton floatingActionButton6 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_email);
//        FloatingActionButton floatingActionButton7 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_sms);

        materialDesignFAM.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {

                } else {

                }
            }
        });

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Toast.makeText(mActivity, idKlien, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PengajuanDetActivity.this, VoiceCallBridge.class);
                i.putExtra("iduser", idKlien);
                i.putExtra("nama", nama);
                i.putExtra("foto", "");
                startActivity(i);
            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PengajuanDetActivity.this, PengNote1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", idKlien);
                startActivity(intent);
            }
        });

        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PengajuanDetActivity.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", idKlien);
                intent.putExtra("nama", nama);
                startActivity(intent);
            }
        });

//        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(PengajuanDetActivity.this, KlienDetailActivity.class);
//                intent.putExtra("id", idKlien);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(PengajuanDetActivity.this, TaskAddActivity.class);
//                intent.putExtra("id", idKlien);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//        floatingActionButton6.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(PengajuanDetActivity.this, EmailAddActivity.class);
//                intent.putExtra("id", idKlien);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
//
//        floatingActionButton7.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(PengajuanDetActivity.this, SmsAddActivity.class);
//                intent.putExtra("id", idKlien);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        tabLayout = (TabLayout) findViewById(R.id.tablayout1);

        tahap = getIntent().getIntExtra("tahap", 0);

        Bundle bundle = new Bundle();
        bundle.putString("id", getIntent().getStringExtra("id"));
        bundle.putString("nama", getIntent().getStringExtra("nama"));
        bundle.putString("id_klien", getIntent().getStringExtra("id_klien"));
        bundle.putInt("tahap", tahap);

        InfoPengFragment infoPengFragment = new InfoPengFragment();
        infoPengFragment.setArguments(bundle);

        HistTahFragment histTahFragment = new HistTahFragment();
        histTahFragment.setArguments(bundle);

        HistNoteFragment histNoteFragment = new HistNoteFragment();
        histNoteFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(infoPengFragment, "Info Pengajuan");
        adapter.addFragments(histTahFragment, "Histori Tahapan");
        adapter.addFragments(histNoteFragment, "Notes");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }




}
