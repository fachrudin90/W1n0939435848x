package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.ArtikelAdapter;
import com.tamboraagungmakmur.winwin.Model.Artikel;
import com.tamboraagungmakmur.winwin.Model.ArtikelModel;
import com.tamboraagungmakmur.winwin.Model.ArtikelResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarArtikel extends AppCompatActivity {

    @Bind(R.id.btBack)
    ImageButton btBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bar)
    AppBarLayout bar;
    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.txTgl1)
    EditText txTgl1;
    @Bind(R.id.txTgl2)
    EditText txTgl2;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.rvList)
    RecyclerView rvList;
    @Bind(R.id.btAdd)
    ImageButton btAdd;
    
    private final static String TAG = "DAFTAR_ARTIKEL";
    private RequestQueue requestQueue;

    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ArtikelModel> dataSet = new ArrayList<>();

    private ArrayList<Artikel> klienArrayList = new ArrayList<>();
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_artikel);
        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(this);

        layoutManager = new LinearLayoutManager(DaftarArtikel.this);
        rvList.setLayoutManager(layoutManager);
        adapter = new ArtikelAdapter(DaftarArtikel.this, klienArrayList);
        rvList.setAdapter(adapter);

        getKlien();

    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
//        VolleyHttp.getInstance(DaftarArtikel.this).getRequestQueue().cancelAll(TAG);
        super.onStop();
    }

    private void GetData() {

        dataSet.clear();
        for (int i = 0; i < 8; i++) {

            ArtikelModel artikelModel = new ArtikelModel("24-02-2017", "Jenis-Jenis Pendidikan di Indonesia", "Zulkarnain", "Indonesia", "Aktif");
            dataSet.add(artikelModel);
            ArtikelModel artikelModel2 = new ArtikelModel("24-02-2017", "Permasalahan Pendidikan di Indonesia", "Adihamsa", "Pendidikan", "Aktif");
            dataSet.add(artikelModel2);
        }

        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btBack, R.id.txTgl1, R.id.txTgl2, R.id.btCari, R.id.btAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.txTgl1:
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTgl1.setText(tgl);
                            }
                        });
                cdp.show(getSupportFragmentManager(), null);
                break;
            case R.id.txTgl2:
                CalendarDatePickerDialogFragment cdp2 = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTgl2.setText(tgl);
                            }
                        });
                cdp2.show(getSupportFragmentManager(), null);
                break;
            case R.id.btCari:
                break;
            case R.id.btAdd:
                Intent i = new Intent(DaftarArtikel.this, AddArtikel.class);
                startActivity(i);
                break;
        }
    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(DaftarArtikel.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_ARTICLE_ALL + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    ArtikelResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), ArtikelResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            klienArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(klienArrayList.size() - 1);
                        }
                        offset += 1;
                        Log.d("offset", Integer.toString(offset));
                        getKlien();

//                        loading = true;
//                        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                visibleItemCount = layoutManager.getChildCount();
//                                totalItemCount = layoutManager.getItemCount();
//                                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//
//                                if (loading) {
//                                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                                        loading = false;
//                                        offset += 1;
//                                        Log.d("offset", Integer.toString(offset));
//                                        getKlien();
//                                    }
//                                }
//                            }
//                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(DaftarArtikel.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(DaftarArtikel.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(DaftarArtikel.this, LoginActivity.class);
                    DaftarArtikel.this.startActivity(intent);
                    DaftarArtikel.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(DaftarArtikel.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(DaftarArtikel.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(DaftarArtikel.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(DaftarArtikel.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(DaftarArtikel.this, LoginActivity.class);
//                    DaftarArtikel.this.startActivity(intent);
//                    DaftarArtikel.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(DaftarArtikel.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(DaftarArtikel.this).addToRequestQueue(stringRequest);

    }
}
