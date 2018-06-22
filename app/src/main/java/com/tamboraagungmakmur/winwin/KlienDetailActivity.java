package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.HttpsTrustManager;
import com.tamboraagungmakmur.winwin.Utils.NonSwipableViewPager;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.ViewPagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innani on 9/26/2017.
 */

public class KlienDetailActivity extends FragmentActivity {


    private static final Object TAG = "KLIEN_DETAIL";
    private NonSwipableViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    private String id;

    private Context context;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HttpsTrustManager.allowAllSSL();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kliendetail);
        context = KlienDetailActivity.this;
        requestQueue = Volley.newRequestQueue(context);

        id = getIntent().getStringExtra("id");
        initViewPager();

        getHistori();
    }

    @Override
    public void onStop() {
        super.onStop();
        requestQueue.cancelAll(TAG);
    }

    private void initViewPager() {
        viewPager = (NonSwipableViewPager) findViewById(R.id.vp_klien);
        tabLayout = (TabLayout) findViewById(R.id.tablayout_klien);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

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

    private void getHistori() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_KLIEN_LIHAT + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "no connection", Toast.LENGTH_SHORT).show();
                } else {


//                    AndLog.ShowLog(KlienDetailActivity.class.getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent);
//                    finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(KlienDetailActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
