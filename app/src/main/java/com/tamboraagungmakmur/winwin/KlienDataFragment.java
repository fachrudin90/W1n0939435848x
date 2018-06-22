package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.LinkAdapter;
import com.tamboraagungmakmur.winwin.Model.Klien1;
import com.tamboraagungmakmur.winwin.Model.KlienDetail;
import com.tamboraagungmakmur.winwin.Model.KlienDetailResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/26/2017.
 */

public class KlienDataFragment extends Fragment {

    private final static String TAG = "KLIEN_DATA";
    private RequestQueue requestQueue;

    private String id;

    private View view;
    private Context context;

    private TextView nopeng, email, email1;
    private TextView nama, ktp, ttl;
    private TextView alamat1, lamatinggal, alamat, kec, kota, kodepos;
    private TextView kontak, telepon, hp;
    private TextView info1, infonama, infotel;
    private TextView info2, infonama1, infoalamat, infotel1;
    private TextView staf, staf1, staf2, staf3;
    private TextView gaji1, gaji, gaji2;
    private TextView bank, bank1, bank2;

    private Button edit;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Klien1> listLink = new ArrayList<>();
    private LinkAdapter adapter;

    private RecyclerView recyclerView1;
    private LinearLayoutManager linearLayoutManager1;
    private ArrayList<Klien1> listLink1 = new ArrayList<>();
    private LinkAdapter adapter1;

    private RecyclerView recyclerView2;
    private LinearLayoutManager linearLayoutManager2;
    private ArrayList<Klien1> listLink2 = new ArrayList<>();
    private LinkAdapter adapter2;

    private RecyclerView recyclerView3;
    private LinearLayoutManager linearLayoutManager3;
    private ArrayList<Klien1> listLink3 = new ArrayList<>();
    private LinkAdapter adapter3;

    private RecyclerView recyclerView4;
    private LinearLayoutManager linearLayoutManager4;
    private ArrayList<Klien1> listLink4 = new ArrayList<>();
    private LinkAdapter adapter4;

    private TextView pinjaman, disetujui, lunas, ditolak, bunga, rating, maxpinjam;

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
            view = inflater.inflate(R.layout.fragment_klien_data, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
//        view = inflater.inflate(R.layout.fragment_klien_data, container, false);
        context = view.getContext();
        requestQueue = Volley.newRequestQueue(context);

        id = getArguments().getString("id");

        initView();
        getKlien();

        Log.d("kliendetail", "ok");

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("klien"));

        return view;
    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            getKlien();
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void initView() {

        pinjaman = (TextView) view.findViewById(R.id.pinjaman);
        disetujui = (TextView) view.findViewById(R.id.disetujui);
        lunas = (TextView) view.findViewById(R.id.lunas);
        ditolak = (TextView) view.findViewById(R.id.ditolak);
        bunga = (TextView) view.findViewById(R.id.bunga);
        rating = (TextView) view.findViewById(R.id.rating);
        maxpinjam = (TextView) view.findViewById(R.id.maxpinjam);

        nopeng = (TextView) view.findViewById(R.id.nopeng);
        email = (TextView) view.findViewById(R.id.email);
        email1 = (TextView) view.findViewById(R.id.email1);

        nama = (TextView) view.findViewById(R.id.nama);
        ktp = (TextView) view.findViewById(R.id.ktp);
        ttl = (TextView) view.findViewById(R.id.ttl);

        alamat1 = (TextView) view.findViewById(R.id.alamat1);
        lamatinggal = (TextView) view.findViewById(R.id.lamatinggal);
        alamat = (TextView) view.findViewById(R.id.alamat);
        kec = (TextView) view.findViewById(R.id.kec);
        kota = (TextView) view.findViewById(R.id.kota);
        kodepos = (TextView) view.findViewById(R.id.kodepos);

        kontak = (TextView) view.findViewById(R.id.kontak);
        telepon = (TextView) view.findViewById(R.id.telepon);
        hp = (TextView) view.findViewById(R.id.hp);

        info1 = (TextView) view.findViewById(R.id.info1);
        infonama = (TextView) view.findViewById(R.id.infonama);
        infotel = (TextView) view.findViewById(R.id.infotel);

        info2 = (TextView) view.findViewById(R.id.info2);
        infonama1 = (TextView) view.findViewById(R.id.infonama1);
        infoalamat = (TextView) view.findViewById(R.id.infoalamat);
        infotel1 = (TextView) view.findViewById(R.id.infotel1);

        staf = (TextView) view.findViewById(R.id.staf);
        staf1 = (TextView) view.findViewById(R.id.staf1);
        staf2 = (TextView) view.findViewById(R.id.staf2);
        staf3 = (TextView) view.findViewById(R.id.staf3);

        gaji1 = (TextView) view.findViewById(R.id.gaji1);
        gaji = (TextView) view.findViewById(R.id.gaji);
        gaji2 = (TextView) view.findViewById(R.id.gaji2);

        bank = (TextView) view.findViewById(R.id.bank);
        bank1 = (TextView) view.findViewById(R.id.bank1);
        bank2 = (TextView) view.findViewById(R.id.bank2);

        edit = (Button) view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KlienEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                context.startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_kec);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LinkAdapter(listLink);
        recyclerView.setAdapter(adapter);

        if (listLink != null) {
            listLink.clear();
        } else {
            listLink = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();

        recyclerView1 = (RecyclerView) view.findViewById(R.id.rv_kel);
        linearLayoutManager1 = new LinearLayoutManager(context);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        adapter1 = new LinkAdapter(listLink1);
        recyclerView1.setAdapter(adapter1);

        if (listLink1 != null) {
            listLink1.clear();
        } else {
            listLink1 = new ArrayList<>();
        }
        adapter1.notifyDataSetChanged();

        recyclerView2 = (RecyclerView) view.findViewById(R.id.rv_per);
        linearLayoutManager2 = new LinearLayoutManager(context);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        adapter2 = new LinkAdapter(listLink2);
        recyclerView2.setAdapter(adapter2);

        if (listLink2 != null) {
            listLink2.clear();
        } else {
            listLink2 = new ArrayList<>();
        }
        adapter2.notifyDataSetChanged();

        recyclerView3 = (RecyclerView) view.findViewById(R.id.rv_lain);
        linearLayoutManager3 = new LinearLayoutManager(context);
        recyclerView3.setLayoutManager(linearLayoutManager3);
        adapter3 = new LinkAdapter(listLink3);
        recyclerView3.setAdapter(adapter3);

        if (listLink3 != null) {
            listLink3.clear();
        } else {
            listLink3 = new ArrayList<>();
        }
        adapter3.notifyDataSetChanged();

        recyclerView4 = (RecyclerView) view.findViewById(R.id.rv_ip);
        linearLayoutManager4 = new LinearLayoutManager(context);
        recyclerView4.setLayoutManager(linearLayoutManager4);
        adapter4 = new LinkAdapter(listLink4);
        recyclerView4.setAdapter(adapter4);

        if (listLink4 != null) {
            listLink4.clear();
        } else {
            listLink4 = new ArrayList<>();
        }
        adapter4.notifyDataSetChanged();

    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KLIEN_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KlienDetailResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KlienDetailResponse.class);
                    KlienDetail klienDetail = klienResponse.getData();

                    rating.setText(klienDetail.getRating()); //d
//                    if (klienDetail.getMax_pinjam() != null) {
                        maxpinjam.setText(new FormatPrice().format(klienDetail.getMax_pinjam()));
//                    }
                    pinjaman.setText("" + klienDetail.getPengajuan().length); //a
                    disetujui.setText(klienDetail.getSetujui());
                    lunas.setText(klienDetail.getLunas()); //b
                    ditolak.setText(klienDetail.getTolak()); //c
                    bunga.setText(klienDetail.getBunga() + "%");


                    nopeng.setText("No. " + klienDetail.getNomor_pelanggan());
                    email.setText("Email: " + klienDetail.getEmail());
                    email1.setText("Email Alternatif: " + klienDetail.getEmail_alternatif());

                    nama.setText(klienDetail.getNama_lengkap());
                    ktp.setText("No. KTP: " + klienDetail.getNo_ktp());
                    ttl.setText("Tanggal Lahir: " + klienDetail.getTgl_lahir());

                    alamat1.setText("Alamat Pelanggan: (" + klienDetail.getStatus_rumah() + ")");
                    lamatinggal.setText("Lama Tinggal: " + klienDetail.getLama_tinggal_tahun() + " Tahun " + klienDetail.getLama_tinggal_bulan() + " Bulan");
                    alamat.setText(klienDetail.getAlamat());
                    kec.setText("Kelurahan: " + klienDetail.getKelurahan() + ", Kecamatan: " + klienDetail.getKecamatan());
                    kota.setText("Kota: " + klienDetail.getKota());
                    kodepos.setText("Kodepos: " + klienDetail.getKodepos());

                    telepon.setText("Telepon Rumah: " + klienDetail.getTelepon());
                    hp.setText("Handphone: " + klienDetail.getHandphone());

                    infonama.setText("Nama: " + klienDetail.getNama_keluarga_serumah());
                    infotel.setText("Telepon: " + klienDetail.getTelepon_keluarga_serumah());

                    infonama1.setText("Nama: " + klienDetail.getNama_keluarga_tidak_serumah());
                    infoalamat.setText("Alamat: " + klienDetail.getAlamat_keluarga_tidak_serumah());
                    infotel1.setText("Telepon: " + klienDetail.getTelepon_keluarga_tidak_serumah());

                    staf.setText(klienDetail.getJabatan() + " di " + klienDetail.getNama_perusahaan());
                    staf1.setText("Jenis Pekerjaan: " + klienDetail.getJenis_pekerjaan() + ", Lama Kerja: " + klienDetail.getLama_bekerja());
                    staf2.setText(klienDetail.getAlamat_perusahaan());
                    staf3.setText("Nama HRD: " + klienDetail.getHrd_perusahaan() + ", Kontak Perusahaan: " + klienDetail.getTelepon_perusahaan());

                    gaji.setText("Rp. " + klienDetail.getBesar_gaji());
                    gaji2.setText("Tanggal Gajian: " + klienDetail.getTanggal_gajian());

                    bank.setText("Bank: " + klienDetail.getNama_bank());
                    bank1.setText("No. Rekening: " + klienDetail.getNomor_rekening());
                    bank2.setText("Nama Pemilik Rekening: " + klienDetail.getNama_pemilik_rekening());

                    for (int i=0; i<klienDetail.getMirip_kecamatan_kelurahan().length; i++) {
                        Klien1 klien1 = new Klien1();
                        klien1.setId(id);
                        klien1.setType("3");
                        klien1.setId1(klienDetail.getMirip_kecamatan_kelurahan()[i].getId());
                        klien1.setKlien(klienDetail.getMirip_kecamatan_kelurahan()[i].getLabel());
                        listLink.add(klien1);
                        adapter.notifyItemChanged(listLink.size()-1);
                    }

                    for (int i=0; i<klienDetail.getMirip_keluarga().length; i++) {
                        Klien1 klien1 = new Klien1();
                        klien1.setId(id);
                        klien1.setType("1");
                        klien1.setId1(klienDetail.getMirip_keluarga()[i].getId());
                        klien1.setKlien(klienDetail.getMirip_keluarga()[i].getLabel());
                        listLink1.add(klien1);
                        adapter1.notifyItemChanged(listLink1.size()-1);
                    }

                    for (int i=0; i<klienDetail.getMirip_perusahaan().length; i++) {
                        Klien1 klien1 = new Klien1();
                        klien1.setId(id);
                        klien1.setType("2");
                        klien1.setId1(klienDetail.getMirip_perusahaan()[i].getId());
                        klien1.setKlien(klienDetail.getMirip_perusahaan()[i].getLabel());
                        listLink2.add(klien1);
                        adapter2.notifyItemChanged(listLink2.size()-1);
                    }

                    for (int i=0; i<klienDetail.getMirip_lain_lain().length; i++) {
                        Klien1 klien1 = new Klien1();
                        klien1.setId(id);
                        klien1.setType("3");
                        klien1.setId1(klienDetail.getMirip_lain_lain()[i].getId());
                        klien1.setKlien(klienDetail.getMirip_lain_lain()[i].getLabel());
                        listLink3.add(klien1);
                        adapter3.notifyItemChanged(listLink3.size()-1);
                    }

                    for (int i=0; i<klienDetail.getMirip_ip().length; i++) {
                        Klien1 klien1 = new Klien1();
                        klien1.setId(id);
                        klien1.setType("3");
                        klien1.setId1(klienDetail.getMirip_ip()[i].getId());
                        klien1.setKlien(klienDetail.getMirip_ip()[i].getLabel());
                        listLink4.add(klien1);
                        adapter4.notifyItemChanged(listLink4.size()-1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    getActivity().finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent);
//                    getActivity().finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(context);
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
