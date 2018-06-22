package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 8/3/2017.
 */

public class EditDataKlienFragment extends Fragment {

    private final static String TAG = "EDIT_DATA_KLIEN";
    private RequestQueue requestQueue;

    private View view;
    private Context context;
    private Button btSave;
    private Spinner spinner1;
    private int pos = 0;
    private String pos1;

    private ProgressBar progressBar;
    private EditText namaKlien, email, jenisKel, noKtp, alamat, kota, kecamatan, statusRumah, kodePos, lamaTinggalTahun, lamaTinggalBulan, noTelpHp,
            jenisPerusahaan, namaPekerjaan, hrdPerusahaan, posisiDiPerusahaan, lamaBekerja, alamatPerusahaan, teleponPerusahaan, gaji, tanggalGaji,
            namaAnggKelSerumah, telAnggKelSerumah, namaAnggKelTidakSerumah, jenisHubAnggKelTidakSerumah, alamatAnggKelTidakSerumah, bankRekKlien,
            lokasiCabangBank, namaPemilikRek, noRek, noTeleponRumah;



    public EditDataKlienFragment() {
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
            view = inflater.inflate(R.layout.fragment_add_data_klien, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();
        requestQueue = Volley.newRequestQueue(context);

        Intent intent = new Intent("title");
        intent.putExtra("message", "Klien");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        initView();
        initData();

        return view;
    }

    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initData() {

        namaKlien.setText(getArguments().getString("namaklien"));
        email.setText(getArguments().getString("email"));
        noKtp.setText(getArguments().getString("noktp"));
        alamat.setText(getArguments().getString("alamat"));
        kota.setText(getArguments().getString("kota"));
        kecamatan.setText(getArguments().getString("kec"));
//        if (getArguments().getString("statusrumah") != null) {
//            statusRumah.setText(getArguments().getString("statusrumah"));
//        }
        kodePos.setText(getArguments().getString("kodepos"));
        lamaTinggalTahun.setText(getArguments().getString("lama0"));
        lamaTinggalBulan.setText(getArguments().getString("lama1"));
        noTelpHp.setText(getArguments().getString("nohp"));
        jenisPerusahaan.setText(getArguments().getString("jenisper"));
        namaPekerjaan.setText(getArguments().getString("namaper"));
        hrdPerusahaan.setText(getArguments().getString("hrdper"));
        posisiDiPerusahaan.setText(getArguments().getString("jabatan"));
        lamaBekerja.setText(getArguments().getString("lamabekerja"));
        alamatPerusahaan.setText(getArguments().getString("alamatper"));
        teleponPerusahaan.setText(getArguments().getString("teleponper"));
        gaji.setText(getArguments().getString("gaji"));
        tanggalGaji.setText(getArguments().getString("tglgaji"));
        namaAnggKelSerumah.setText(getArguments().getString("namaangg"));
        telAnggKelSerumah.setText(getArguments().getString("teleponangg"));
        namaAnggKelTidakSerumah.setText(getArguments().getString("namaangg1"));
        jenisHubAnggKelTidakSerumah.setText(getArguments().getString("jenishub"));
        alamatAnggKelTidakSerumah.setText(getArguments().getString("alamatangg1"));
        bankRekKlien.setText(getArguments().getString("bank"));
        lokasiCabangBank.setText(getArguments().getString("cabangbank"));
        namaPemilikRek.setText(getArguments().getString("namarek"));
        noRek.setText(getArguments().getString("norek"));
        noTeleponRumah.setText(getArguments().getString("notelprumah"));


    }

    private void initView() {
        btSave = (Button) view.findViewById(R.id.btSave);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        namaKlien = (EditText) view.findViewById(R.id.txNamaKlien);
        email = (EditText) view.findViewById(R.id.txEmail);
        jenisKel = (EditText) view.findViewById(R.id.txJenis_Kel);
        noKtp = (EditText) view.findViewById(R.id.txNoKtp);
        alamat = (EditText) view.findViewById(R.id.txAlamat);
        kota = (EditText) view.findViewById(R.id.txKota);
        kecamatan = (EditText) view.findViewById(R.id.txKecamatan);
//        statusRumah = (EditText) view.findViewById(R.id.txStatusRumah);
        kodePos = (EditText) view.findViewById(R.id.txKodePos);
        lamaTinggalTahun = (EditText) view.findViewById(R.id.txLamaTinggalTahun);
        lamaTinggalBulan = (EditText) view.findViewById(R.id.txLamaTinggalBulan);
        noTelpHp = (EditText) view.findViewById(R.id.txNoTelpHP);
        jenisPerusahaan = (EditText) view.findViewById(R.id.txJenisPerusahaan);
        namaPekerjaan = (EditText) view.findViewById(R.id.txNamaPekerjaan);
        hrdPerusahaan = (EditText) view.findViewById(R.id.txHRDPerusahaan);
        posisiDiPerusahaan = (EditText) view.findViewById(R.id.txPosisidiPerusahaan);
        lamaBekerja = (EditText) view.findViewById(R.id.txLamaBekerja);
        alamatPerusahaan = (EditText) view.findViewById(R.id.txAlamatPerusahaan);
        teleponPerusahaan = (EditText) view.findViewById(R.id.txTeleponPerusahaan);
        gaji = (EditText) view.findViewById(R.id.txGaji);
        tanggalGaji = (EditText) view.findViewById(R.id.txTanggalGaji);
        namaAnggKelSerumah = (EditText) view.findViewById(R.id.txNamaAnggotaKeluargaSerumah);
        telAnggKelSerumah = (EditText) view.findViewById(R.id.txTeleponAnggotaKeluargaSerumah);
        namaAnggKelTidakSerumah =(EditText) view.findViewById(R.id.txNamaAnggotaKeluargaTidakSerumah);
        jenisHubAnggKelTidakSerumah = (EditText) view.findViewById(R.id.txJenisHubunganAnggotaKeluargaTidakSerumah);
        alamatAnggKelTidakSerumah = (EditText) view.findViewById(R.id.txAlamatAnggotaKeluargaTidakSerumah);
        bankRekKlien = (EditText) view.findViewById(R.id.txBankRekeningKlien);
        lokasiCabangBank = (EditText) view.findViewById(R.id.txLokasiCabangBank);
        namaPemilikRek = (EditText) view.findViewById(R.id.txNamaPemilikRekening);
        noRek = (EditText) view.findViewById(R.id.txNomerRekening);
        noTeleponRumah = (EditText) view.findViewById(R.id.txNoTelp);

        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.status_rumah, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        final ArrayList<String> statRum = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.status_rumah_id)));
        pos1 = getArguments().getString("statusrumahid");
        if (pos1 != null) {
            for (int i = 0; i < statRum.size(); i++) {
                if (pos1.compareTo(statRum.get(i)) == 0) {
                    pos = i;
                    break;
                }
            }
        }

        spinner1.setSelection(pos);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                pos1 = statRum.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btSave.setVisibility(View.INVISIBLE);
                updateKlien(getArguments().getString("id"));
            }
        });
    }

    private void updateKlien(final String cari) {
        Log.d("klien_update", cari);

        String query = "";
        if (cari.compareTo("") != 0) {
            query = "/" + cari;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_KLIEN_UPDATE + query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                btSave.setVisibility(View.VISIBLE);

                Log.d("klien_update", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    Intent intent = new Intent("klien");
                    // You can also include some extra data.
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    GlobalToast.ShowToast(context, klienResponse.getMessage());

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
                params.put("email", email.getText().toString());
                params.put("kodepos", kodePos.getText().toString());
                params.put("besar_gaji", gaji.getText().toString());
                params.put("kota", kota.getText().toString());
                params.put("posisi", posisiDiPerusahaan.getText().toString());
                params.put("nama_keluarga_serumah", namaAnggKelSerumah.getText().toString());
                params.put("alamat", alamat.getText().toString());
                params.put("no_ktp", noKtp.getText().toString());
                params.put("nama_keluarga_tidak_serumah", namaAnggKelTidakSerumah.getText().toString());
                params.put("handphone", noTelpHp.getText().toString());
                params.put("telepon", noTeleponRumah.getText().toString());
                params.put("nama_hrd", hrdPerusahaan.getText().toString());
                params.put("alamat_keluarga_tidak_serumah", alamatAnggKelTidakSerumah.getText().toString());
                params.put("telepon_keluarga_serumah", telAnggKelSerumah.getText().toString());
                params.put("nama_bank", bankRekKlien.getText().toString());
                params.put("kecamatan", kecamatan.getText().toString());
                params.put("perusahaan", namaPekerjaan.getText().toString());
                params.put("hubungan_keluarga_tidak_serumah", jenisHubAnggKelTidakSerumah.getText().toString());
                params.put("jenis_kelamin", "");
                if (jenisKel.getText().toString() != null) {
                    params.put("jenis_kelamin", jenisKel.getText().toString());
                }
                params.put("lokasi_cabang_bank", "");
                if (lokasiCabangBank.getText().toString() != null) {
                    params.put("lokasi_cabang_bank", lokasiCabangBank.getText().toString());
                }
                params.put("tanggal_gajian", tanggalGaji.getText().toString());
                params.put("nama_lengkap", namaKlien.getText().toString());
                params.put("lama_bekerja", lamaBekerja.getText().toString());
                params.put("status_rumah", "");
                if (pos1 != null) {
                    params.put("status_rumah", pos1);
                }
                params.put("nama_pemilik_rekening", namaPemilikRek.getText().toString());
                params.put("nomor_rekening", noRek.getText().toString());
                params.put("jenis_pekerjaan", "");
                if (jenisPerusahaan.getText().toString() != null) {
                    params.put("jenis_pekerjaan", jenisPerusahaan.getText().toString());
                }
                params.put("alamat_perusahaan", alamatPerusahaan.getText().toString());
                params.put("telepon_perusahaan", teleponPerusahaan.getText().toString());
                params.put("lama_tinggal_bulan", lamaTinggalBulan.getText().toString());
                params.put("lama_tinggal_tahun", lamaTinggalTahun.getText().toString());

                Log.d("klien_update", jenisKel.getText().toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
