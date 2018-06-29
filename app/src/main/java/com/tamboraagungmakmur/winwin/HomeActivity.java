package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.NotificationAdapter;
import com.tamboraagungmakmur.winwin.Adapter.ViewPagerAdapter;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Model.NotifiactionModel;
import com.tamboraagungmakmur.winwin.Model.SidebarModel;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.HttpsTrustManager;
import com.tamboraagungmakmur.winwin.Utils.NonSwipableViewPager;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.UniqID;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Fragment> listFragment = new ArrayList<>();
    private ArrayList<Fragment> listKomunikasiFragment = new ArrayList<>();
    private ArrayList<Fragment> listPinjamanFragment = new ArrayList<>();
    private ArrayList<Fragment> listTransaksiFragment = new ArrayList<>();
    private ArrayList<Fragment> listInvestasiFragment = new ArrayList<>();
    private ArrayList<Fragment> listLaporanFragment = new ArrayList<>();

    private FragmentManager fragmentManager;

    private RelativeLayout rlNavBar;
    private ImageButton btBack, btBackRight, btNotif;
    private TextView txNavBarTitle, txNavBarTitleRight, txProfile;
    private ArrayList<SidebarModel> dataSet = new ArrayList<>();
    private DrawerLayout drawer;
    private NonSwipableViewPager menuPager, menuPagerRight;
    private ViewPagerAdapter viewPagerAdapter, viewPagerAdapterRight;
    private NavigationView navigationView, navViewRight;
    private CircleImageView imgProfileRight;
    private ArrayList<String> titleNav;
    private NotificationAdapter adapter;
    private ArrayList<NotifiactionModel> dataNotif = new ArrayList<>();
    private ListView lvNotif;
    private LinearLayout lyMenu, lyNotif;

    private int index1 = 0;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HttpsTrustManager.allowAllSSL();

        SessionManager sess = new SessionManager(HomeActivity.this);
        sess.setIdMenu(0);
        sess.setPage(0);
        sess.setTitle(null);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_slide_menu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.openDrawer(GravityCompat.START);
            }
        });

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        title = (TextView) findViewById(R.id.title);

        titleNav = new ArrayList<>();
        titleNav.add(null);
        titleNav.add(null);
        titleNav.add(null);
        titleNav.add(null);

        imgProfileRight = (CircleImageView) findViewById(R.id.imgProfileRight);
        txProfile = (TextView) findViewById(R.id.txProfile);
        txNavBarTitleRight = (TextView) findViewById(R.id.txNavBarTitleRight);
        navViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        lvNotif = (ListView) findViewById(R.id.lvNotif);
        lyMenu = (LinearLayout) findViewById(R.id.lyMenu);
        lyNotif = (LinearLayout) findViewById(R.id.lyNotif);
        btNotif = (ImageButton) findViewById(R.id.btNotif);

        rlNavBar = (RelativeLayout) findViewById(R.id.rlNavBar);
        btBack = (ImageButton) findViewById(R.id.btBack);
        btBackRight = (ImageButton) findViewById(R.id.btBackRight);
        txNavBarTitle = (TextView) findViewById(R.id.txNavBarTile);

        menuPager = (NonSwipableViewPager) findViewById(R.id.menuPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SidebarMenu1(), "");
        viewPagerAdapter.addFragments(new SidebarMenu2(), "");
        viewPagerAdapter.addFragments(new SidebarMenu3(), "");
        menuPager.setAdapter(viewPagerAdapter);
        menuPager.setOffscreenPageLimit(2);
        menuPager.setCurrentItem(0);

        menuPagerRight = (NonSwipableViewPager) findViewById(R.id.menuPager_right);
        viewPagerAdapterRight = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapterRight.addFragments(new SidebarMenuRight1(), "");
        viewPagerAdapterRight.addFragments(new SidebarMenuRight2(), "");
        viewPagerAdapterRight.addFragments(new SidebarMenuRight3(), "");
        viewPagerAdapterRight.addFragments(new SidebarMenuRight4(), "");
        viewPagerAdapterRight.addFragments(new SidebarMenuRight5(), "");
        menuPagerRight.setAdapter(viewPagerAdapterRight);
        menuPagerRight.setOffscreenPageLimit(3);
        menuPagerRight.setCurrentItem(0);


        adapter = new NotificationAdapter(HomeActivity.this, dataNotif);
        lvNotif.setAdapter(adapter);
        initNotif();


        fragmentManager = getSupportFragmentManager();
        listFragment.add(new HomeParent());
        listFragment.add(new PinjamanParent());
        listFragment.add(new KomunikasiParent());
        listFragment.add(new KlienParent());
        listFragment.add(new LaporanParent());
        listFragment.add(new TasklistParent());
        listFragment.add(new TransaksiParent());
//        listFragment.add(new ArchiveParent());

        listTransaksiFragment.add(new TransaksiPembayaran());
        listTransaksiFragment.add(new TransaksiPencairan());
        listTransaksiFragment.add(new TransaksiDaftarTransfer());
        listTransaksiFragment.add(new UploadPembayaran());
        listTransaksiFragment.add(new UploadPencairan());

        listInvestasiFragment.add(new InvestasiInvestor());
        listInvestasiFragment.add(new InvestasiInvest());
        listInvestasiFragment.add(new InvestasiInvestarik());
        listInvestasiFragment.add(new InvestasiInvestcair());
        listInvestasiFragment.add(new InvestasiInvestlain());

        listKomunikasiFragment.add(new KomunikasiEmail());
        listKomunikasiFragment.add(new KomunikasiSms());

        listLaporanFragment.add(new LaporanAkunting());
        listLaporanFragment.add(new LaporanInternal());
        listLaporanFragment.add(new LaporanOperator());
        listLaporanFragment.add(new LaporanSimulasi());
        listLaporanFragment.add(new LaporanAktivitasRev());
//        listLaporanFragment.add(new LaporanAktivitas());
        listLaporanFragment.add(new LaporanNasabah());


        displaySelectedScreen(0);

        AndLog.ShowLog("UniqID", UniqID.getPsuedoUniqueID());

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SessionManager(HomeActivity.this).setPage(0);
                menuPager.setCurrentItem(0);
                rlNavBar.setVisibility(View.GONE);
            }
        });

        btBackRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SessionManager sess = new SessionManager(HomeActivity.this);
                int prevPage = sess.getPage() - 1;
                sess.setPage(prevPage);
                txNavBarTitleRight.setText(titleNav.get(prevPage));
                menuPagerRight.setCurrentItem(prevPage);

                if (prevPage == 0) {
                    btBackRight.setVisibility(View.GONE);
                    imgProfileRight.setVisibility(View.VISIBLE);
                    txNavBarTitleRight.setText(getString(R.string.hello));
                }
            }
        });

        txProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        btNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                    lyNotif.setVisibility(View.VISIBLE);
                }
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {


            }

            @Override
            public void onDrawerClosed(View drawerView) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        lyNotif.setVisibility(View.GONE);
                    }
                }, 1250);


            }

            @Override
            public void onDrawerStateChanged(int newState) {


            }
        });

        if (getIntent().hasExtra("baru")) {
            drawer.closeDrawer(GravityCompat.START);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, new ArchiveParent0())
                    .commitAllowingStateLoss();
            sess.setIdMenu(0);
            sess.setPage(0);
            rlNavBar.setVisibility(View.GONE);
            menuPager.setCurrentItem(0);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("title"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver1,
                new IntentFilter("exit"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            title.setText(message);
        }
    };

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            logoutProses();
        }
    };

    private void initNotif() {

        dataNotif.clear();
//        dataNotif.add(new NotifiactionModel("", "Ridwan sudah jatuh tempo", "23 menit yang lalu"));
//        dataNotif.add(new NotifiactionModel("", "Aditya Eka harap segera dihubungi untuk pengajuan", "27 menit yang lalu"));
//        dataNotif.add(new NotifiactionModel("", "Adihamsah sudah jatuh tempo", "31 menit yang lalu"));

        adapter.notifyDataSetChanged();

    }


    private void displaySelectedScreen(int itemId) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, listFragment.get(itemId))
                .commitAllowingStateLoss();
    }

    private void displayKomSelectedScreen(int itemId) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, listKomunikasiFragment.get(itemId))
                .commitAllowingStateLoss();
    }

    private void displayTransaksiSelectedScreen(int itemId) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, listTransaksiFragment.get(itemId))
                .commitAllowingStateLoss();
    }

    private void displayInvestasiSelectedScreen(int itemId) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, listInvestasiFragment.get(itemId))
                .commitAllowingStateLoss();
    }

    private void displayLaporanSelectedScreen(int itemId) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, listLaporanFragment.get(itemId))
                .commitAllowingStateLoss();
    }


    private void displayPinjSelectedScreen(int itemId) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, listPinjamanFragment.get(itemId))
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            Intent intent = new Intent(HomeActivity.this, ExitActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(HomeActivity.this).unregisterReceiver(onNotice);
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(onNotice, new IntentFilter(getString(R.string.sidebar)));
        LocalBroadcastManager.getInstance(HomeActivity.this).unregisterReceiver(onRightSidebar);
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(onRightSidebar, new IntentFilter(getString(R.string.sidebar_right)));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(HomeActivity.this).unregisterReceiver(onNotice);
        LocalBroadcastManager.getInstance(HomeActivity.this).unregisterReceiver(onRightSidebar);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver1);

    }

//    private BroadcastReceiver onNotice = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            Bundle bundle = intent.getExtras();
//
//            if (bundle != null) {
//
//                int index = bundle.getInt(getString(R.string.index));
//                int menu = bundle.getInt(getString(R.string.menu));
//
//                if (index == 1) {
//
//                    if (menu >= 0) {
//                        new SessionManager(HomeActivity.this).setPage(0);
//                        drawer.closeDrawer(GravityCompat.START);
//                        menuPager.setCurrentItem(0);
//                        rlNavBar.setVisibility(View.GONE);
//                        displaySelectedScreen(listFragment.size() - 1);
//
//
//                    } else {
//                        menuPager.setCurrentItem(1);
//                        rlNavBar.setVisibility(View.VISIBLE);
//                    }
//
//                } else if (index == 4) {
//
//                    if (menu >= 0) {
//
//                        drawer.closeDrawer(GravityCompat.START);
//                        menuPager.setCurrentItem(0);
//                        rlNavBar.setVisibility(View.GONE);
//                        displayKomSelectedScreen(menu);
//
//
//                    } else {
//                        menuPager.setCurrentItem(1);
//                        rlNavBar.setVisibility(View.VISIBLE);
//                    }
//
//                } else {
//
//                    drawer.closeDrawer(GravityCompat.START);
//                    displaySelectedScreen(index);
//                }
//
//            }
//
//        }
//    };

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                SessionManager sess = new SessionManager(HomeActivity.this);
                int index = bundle.getInt(getString(R.string.index));

                switch (sess.getPage()) {
                    case 0:

                        if (index == 6) {
                            sess.setIdMenu(index);
                            sess.setPage(1);
                            rlNavBar.setVisibility(View.VISIBLE);
                            txNavBarTitle.setText(getString(R.string.transaksi));
                            menuPager.setCurrentItem(1);

                        } else if (index == 7) {
                            sess.setIdMenu(index);
                            sess.setPage(1);
                            rlNavBar.setVisibility(View.VISIBLE);
                            txNavBarTitle.setText(getString(R.string.investasi));
                            menuPager.setCurrentItem(1);

                        }else if (index == 8) {
                            drawer.closeDrawer(GravityCompat.START);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, new PinjamanReqJanjiBayar()).commit();

                            sess.setIdMenu(0);
                            sess.setPage(0);
                            rlNavBar.setVisibility(View.GONE);
                            menuPager.setCurrentItem(0);

                        } else if (index == 2) {
                            sess.setIdMenu(index);
                            sess.setPage(1);
                            rlNavBar.setVisibility(View.VISIBLE);
                            txNavBarTitle.setText(getString(R.string.komunikasi));
                            menuPager.setCurrentItem(1);


                        } else if (index == 4) {
                            sess.setIdMenu(index);
                            sess.setPage(1);
                            rlNavBar.setVisibility(View.VISIBLE);
                            txNavBarTitle.setText(getString(R.string.laporan));
                            menuPager.setCurrentItem(1);


                        } else if (index == 1) {
                            sess.setIdMenu(index);
                            sess.setPage(1);
                            rlNavBar.setVisibility(View.VISIBLE);
                            txNavBarTitle.setText(getString(R.string.status_pinj));
                            menuPager.setCurrentItem(1);

                        } else {
                            drawer.closeDrawer(GravityCompat.START);
                            sess.setIdMenu(0);
                            sess.setPage(0);
                            rlNavBar.setVisibility(View.GONE);
                            menuPager.setCurrentItem(0);
                            displaySelectedScreen(index);

                            Intent in = new Intent(getString(R.string.archive));
                            LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(in);
                        }

                        break;
                    case 1:



                        if (new SessionManager(HomeActivity.this).getIdMenu() == 6) {
                            drawer.closeDrawer(GravityCompat.START);
                            sess.setIdMenu(0);
                            sess.setPage(0);
                            rlNavBar.setVisibility(View.GONE);
                            menuPager.setCurrentItem(0);
                            displayTransaksiSelectedScreen(index);

//                            if (index == 0) {
//                                drawer.closeDrawer(GravityCompat.START);
//
//                                sess.setPage(0);
//                                txNavBarTitleRight.setText(getString(R.string.hello));
//                                btBackRight.setVisibility(View.GONE);
//                                imgProfileRight.setVisibility(View.VISIBLE);
//                                menuPagerRight.setCurrentItem(0);
//
//                                Intent i = new Intent(HomeActivity.this, DaftarArtikel.class);
//                                startActivity(i);
//
//                            } else if (index == 3) {
//                                sess.setPage(2);
//                                btBackRight.setVisibility(View.VISIBLE);
//                                imgProfileRight.setVisibility(View.GONE);
//                                txNavBarTitleRight.setText(getString(R.string.daftar_ref));
//                                titleNav.set(2, getString(R.string.daftar_ref));
//                                menuPagerRight.setCurrentItem(2);
//                            } else {
//                                drawer.closeDrawer(GravityCompat.END);
//
//                                sess.setPage(0);
//                                txNavBarTitleRight.setText(getString(R.string.hello));
//                                btBackRight.setVisibility(View.GONE);
//                                imgProfileRight.setVisibility(View.VISIBLE);
//                                menuPagerRight.setCurrentItem(0);
//                            }
                        } else if (new SessionManager(HomeActivity.this).getIdMenu() == 7) {
                            drawer.closeDrawer(GravityCompat.START);
                            sess.setIdMenu(0);
                            sess.setPage(0);
                            rlNavBar.setVisibility(View.GONE);
                            menuPager.setCurrentItem(0);
                            displayInvestasiSelectedScreen(index);

                        } else if (new SessionManager(HomeActivity.this).getIdMenu() == 2) {
                            drawer.closeDrawer(GravityCompat.START);
                            sess.setIdMenu(0);
                            sess.setPage(0);
                            rlNavBar.setVisibility(View.GONE);
                            menuPager.setCurrentItem(0);
                            displayKomSelectedScreen(index);
                        } else if (new SessionManager(HomeActivity.this).getIdMenu() == 4) {
                            drawer.closeDrawer(GravityCompat.START);
                            sess.setIdMenu(0);
                            sess.setPage(0);
                            rlNavBar.setVisibility(View.GONE);
                            menuPager.setCurrentItem(0);
                            displayLaporanSelectedScreen(index);

                        } else if (new SessionManager(HomeActivity.this).getIdMenu() == 1) {

                            if (index == 0) {
                                sess.setIdMenu(index);
                                sess.setPage(2);
                                rlNavBar.setVisibility(View.VISIBLE);
                                txNavBarTitle.setText(getString(R.string.status_pinj));
                                menuPager.setCurrentItem(2);
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, new ArchiveParent0())
//                                        .commit();
//
//                                sess.setIdMenu(0);
//                                sess.setPage(0);
//                                rlNavBar.setVisibility(View.GONE);
//                                menuPager.setCurrentItem(0);

                            } else if (index == 1) {
                                drawer.closeDrawer(GravityCompat.START);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParent1())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);

                            } else if (index == 2) {
                                drawer.closeDrawer(GravityCompat.START);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParent2())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);

                            } else if (index == 3) {
                                drawer.closeDrawer(GravityCompat.START);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParent3())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);

                            } else if (index == 4) {
                                drawer.closeDrawer(GravityCompat.START);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParent4())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);

                            } else if (index == 5) {
                                drawer.closeDrawer(GravityCompat.START);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParent5())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);

                            } else if (index == 6) {
                                drawer.closeDrawer(GravityCompat.START);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParent6())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);

                            } else if (index == 7) {
                                drawer.closeDrawer(GravityCompat.START);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParent7())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);

                            } else if (index == 8) {
                                drawer.closeDrawer(GravityCompat.START);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParent8())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);

                            }else if (index == 9) {
                                drawer.closeDrawer(GravityCompat.START);
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, new JanjiBayarParent())
//                                        .commit();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParentJanjiBayar())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);
                            }else if (index == 10) {
                                drawer.closeDrawer(GravityCompat.START);
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, new JanjiBayarParent())
//                                        .commit();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParentLenyap())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);
                            }else if (index == 11) {
                                drawer.closeDrawer(GravityCompat.START);
//                                getSupportFragmentManager()
//                                        .beginTransaction()
//                                        .replace(R.id.content_frame, new JanjiBayarParent())
//                                        .commit();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, new ArchiveParentCicilan())
                                        .commit();

                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);
                            }else {
                                drawer.closeDrawer(GravityCompat.START);
                                sess.setIdMenu(0);
                                sess.setPage(0);
                                rlNavBar.setVisibility(View.GONE);
                                menuPager.setCurrentItem(0);
                                displaySelectedScreen(listFragment.size() - 1);
                                Intent i = new Intent(getString(R.string.archive));
                                LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(i);
                            }
                        }


                        break;
                    case 2:
                        drawer.closeDrawer(GravityCompat.START);
                        Log.d("okee", "okee");
                        if (index == 0) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, new ArchiveParent0())
                                    .commit();
                        } else if (index == 1) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, new ArchiveParent01())
                                    .commit();
                        } else if (index == 2) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, new ArchiveParent02())
                                    .commit();
                        } else if (index == 3) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, new ArchiveParent03())
                                    .commit();
                        }
                        sess.setIdMenu(0);
                        sess.setPage(0);
                        rlNavBar.setVisibility(View.GONE);
                        menuPager.setCurrentItem(0);
                        break;
                }


            }
        }
    };


    private BroadcastReceiver  onRightSidebar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                SessionManager sess = new SessionManager(HomeActivity.this);
                int index = bundle.getInt(getString(R.string.index));


                switch (sess.getPage()) {
                    case 0:
                        if (index == 0) {
                            Intent i = new Intent(HomeActivity.this, EditMyProfile.class);
                            startActivity(i);
                            drawer.closeDrawer(GravityCompat.END);

                        }
                        if (index == 1) {

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_frame, new DaftarKlienReward())
                                    .commit();

                            drawer.closeDrawer(GravityCompat.END);

                        } else if (index == 3) {
                            if (sess.getJABATAN().compareTo("IT Admin") == 0 ||sess.getJABATAN().compareTo("Management") == 0) {
                                sess.setPage(1);
                                btBackRight.setVisibility(View.VISIBLE);
                                imgProfileRight.setVisibility(View.GONE);
                                txNavBarTitleRight.setText(getString(R.string.setting));
                                titleNav.set(1, getString(R.string.setting));
                                menuPagerRight.setCurrentItem(1);
                            }
                        }
//                        else if (index == 4) {
//
//                            Intent i = new Intent(HomeActivity.this, LogAktivitas.class);
//                            startActivity(i);
//
//                            drawer.closeDrawer(GravityCompat.END);
//
//                        }
                        else if (index == 4) {
                            drawer.closeDrawer(GravityCompat.END);

                            logoutProses();

                            SessionManager sessionManager = new SessionManager(HomeActivity.this);
                            sessionManager.logoutUser(); sessionManager.setPage(0);

                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();

                            sess.setPage(0);
                            txNavBarTitleRight.setText(getString(R.string.hello));
                            btBackRight.setVisibility(View.GONE);
                            imgProfileRight.setVisibility(View.VISIBLE);
                            menuPagerRight.setCurrentItem(0);
                        } else {
                            drawer.closeDrawer(GravityCompat.END);

                            sess.setPage(0);
                            txNavBarTitleRight.setText(getString(R.string.hello));
                            btBackRight.setVisibility(View.GONE);
                            imgProfileRight.setVisibility(View.VISIBLE);
                            menuPagerRight.setCurrentItem(0);
                        }

                        break;
                    case 1:

                        if (index == 0) {
                            drawer.closeDrawer(GravityCompat.END);

                            sess.setPage(0);
                            txNavBarTitleRight.setText(getString(R.string.hello));
                            btBackRight.setVisibility(View.GONE);
                            imgProfileRight.setVisibility(View.VISIBLE);
                            menuPagerRight.setCurrentItem(0);

                            Intent i = new Intent(HomeActivity.this, DaftarArtikel.class);
                            startActivity(i);

                        } else if (index == 1) {
                            drawer.closeDrawer(GravityCompat.END);

                            sess.setPage(0);
                            txNavBarTitleRight.setText(getString(R.string.hello));
                            btBackRight.setVisibility(View.GONE);
                            imgProfileRight.setVisibility(View.VISIBLE);
                            menuPagerRight.setCurrentItem(0);

                            Intent i = new Intent(HomeActivity.this, DaftarTestimoni.class);
                            startActivity(i);

                        } else if (index == 2) {
                            sess.setPage(2);
                            btBackRight.setVisibility(View.VISIBLE);
                            imgProfileRight.setVisibility(View.GONE);
                            txNavBarTitleRight.setText(getString(R.string.karyawan_pengguna));
                            titleNav.set(0, getString(R.string.karyawan_pengguna));
                            index1 = 0;
                            menuPagerRight.setCurrentItem(4);
                        } else if (index == 3) {
                            sess.setPage(2);
                            btBackRight.setVisibility(View.VISIBLE);
                            imgProfileRight.setVisibility(View.GONE);
                            txNavBarTitleRight.setText(getString(R.string.daftar_ref));
                            titleNav.set(2, getString(R.string.daftar_ref));
                            index1 = 1;
                            menuPagerRight.setCurrentItem(2);
                        } else {
                            drawer.closeDrawer(GravityCompat.END);

                            sess.setPage(0);
                            txNavBarTitleRight.setText(getString(R.string.hello));
                            btBackRight.setVisibility(View.GONE);
                            imgProfileRight.setVisibility(View.VISIBLE);
                            menuPagerRight.setCurrentItem(0);
                        }
                        break;
                    case 2:
                        if (index1 == 1) {
                            String ref = getString(R.string.referensi_a);
                            titleNav.set(3, getString(R.string.referensi_a));
                            if (index == 1) {
                                ref = getString(R.string.referensi_b);
                                titleNav.set(3, getString(R.string.referensi_b));
                            }

                            if (index == 2) {
                                ref = getString(R.string.referensi_c);
                                titleNav.set(3, getString(R.string.referensi_c));
                            }

                            if (index == 3) {
                                ref = getString(R.string.referensi_d);
                                titleNav.set(3, getString(R.string.referensi_d));
                            }

                            sess.setPage(3);
                            sess.setIdMenu(index);
                            btBackRight.setVisibility(View.VISIBLE);
                            imgProfileRight.setVisibility(View.GONE);
                            txNavBarTitleRight.setText(ref);
                            menuPagerRight.setCurrentItem(3);
                        }
                        else {
                            drawer.closeDrawer(GravityCompat.END);

                            sess.setPage(0);
                            txNavBarTitleRight.setText(getString(R.string.hello));
                            btBackRight.setVisibility(View.GONE);
                            imgProfileRight.setVisibility(View.VISIBLE);
                            menuPagerRight.setCurrentItem(0);

                            if (index == 0) {
                                Intent intent1 = new Intent(HomeActivity.this, DaftarKaryawanActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                            } else {
                                Intent intent1 = new Intent(HomeActivity.this, DaftarPenggunaActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                            }
                        }
                        break;
                    case 3:
                        drawer.closeDrawer(GravityCompat.END);

                        sess.setPage(0);
                        txNavBarTitleRight.setText(getString(R.string.hello));
                        btBackRight.setVisibility(View.GONE);
                        imgProfileRight.setVisibility(View.VISIBLE);
                        menuPagerRight.setCurrentItem(0);

                        if(sess.getIdMenu() == 0) {
                            if (index == 0) {
                                Intent intent1 = new Intent(HomeActivity.this, EmailTemplateRefActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                            } else if (index == 3) {
                                Intent intent1 = new Intent(HomeActivity.this, RefKotaActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                            } else if (index == 4) {
                                Intent intent1 = new Intent(HomeActivity.this, RefBankActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                            }
                        }else if(sess.getIdMenu() == 1) {

                            if (index == 5) {
                                Intent intent1 = new Intent(HomeActivity.this, RefPekerjaanActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent1);
                            }
                        }
                        break;

                    case 4:
                        drawer.closeDrawer(GravityCompat.END);

                        sess.setPage(0);
                        txNavBarTitleRight.setText(getString(R.string.hello));
                        btBackRight.setVisibility(View.GONE);
                        imgProfileRight.setVisibility(View.VISIBLE);
                        menuPagerRight.setCurrentItem(0);
                        break;
//                    case 5:
//                        drawer.closeDrawer(GravityCompat.END);
//
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.content_frame, new LaporanParent())
//                                .commit();
//
//                        drawer.closeDrawer(GravityCompat.END);
//                        break;


                }

//                if (index == 3) {
//
//                    if (menu >= 0) {
//
//                        drawer.closeDrawer(GravityCompat.END);
//                        menuPagerRight.setCurrentItem(0);
//                        rlNavBar.setVisibility(View.GONE);
//                        imgProfileRight.setVisibility(View.VISIBLE);
//
//
//                    } else {
//                        imgProfileRight.setVisibility(View.GONE);
//                        menuPagerRight.setCurrentItem(1);
//                        rlNavBar.setVisibility(View.VISIBLE);
//                    }
//
//                } else {
//
//                    drawer.closeDrawer(GravityCompat.END);
//                    displaySelectedScreen(index);
//                }

            }

        }
    };

    private void logoutProses() {
        Log.d("logout", "ok");

        SessionManager sessionManager = new SessionManager(HomeActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_LOGOUT + "/" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Innani
                Log.d("logout", response.toString());
                try {
                    JSONObject res = new JSONObject(response.toString());
                    LogoutResponse loginResponse = new Gson().fromJson(res.toString(), LogoutResponse.class);


                    if (!loginResponse.isError()) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
                        SessionManager sessionManager = new SessionManager(HomeActivity.this);
                        sessionManager.logoutUser(); sessionManager.setPage(0);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(HomeActivity.this, getString(R.string.disconnected));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AndLog.ShowLog(HomeActivity.class.getSimpleName(), error.toString());
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Basic " + AppConf.APIKEY);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);

    }
}
