package com.tamboraagungmakmur.winwin;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddDataKlienFragment extends FragmentActivity {

    private static final String TAG = "ADD_DATA_KLIEN";
    private View view;
    private Context context;
    private Button btSave;
    private ProgressBar progressBar;
    private Spinner spinner1;
    private int pos = 0;

    private RequestQueue requestQueue;

    private EditText namaKlien, email, jenisKel, noKtp, alamat, kota, kecamatan, statusRumah, kodePos, lamaTinggalTahun, lamaTinggalBulan, noTelpHp,
                jenisPerusahaan, namaPekerjaan, hrdPerusahaan, posisiDiPerusahaan, lamaBekerja, alamatPerusahaan, teleponPerusahaan, gaji, tanggalGaji,
                namaAnggKelSerumah, telAnggKelSerumah, namaAnggKelTidakSerumah, jenisHubAnggKelTidakSerumah, alamatAnggKelTidakSerumah, bankRekKlien,
                lokasiCabangBank, namaPemilikRek, noRek, tglLahir, noTeleponRumah, noTelpKts;


    public AddDataKlienFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_data_klien);
        
        context = AddDataKlienFragment.this;
        requestQueue = Volley.newRequestQueue(context);
        initView();

    }

    private void initView() {
        btSave = (Button) findViewById(R.id.btSave);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        namaKlien = (EditText) findViewById(R.id.txNamaKlien);
        email = (EditText) findViewById(R.id.txEmail);
        jenisKel = (EditText) findViewById(R.id.txJenis_Kel);
        noKtp = (EditText) findViewById(R.id.txNoKtp);
        alamat = (EditText) findViewById(R.id.txAlamat);
        kota = (EditText) findViewById(R.id.txKota);
        kecamatan = (EditText) findViewById(R.id.txKecamatan);
//        statusRumah = (EditText) findViewById(R.id.txStatusRumah);
        kodePos = (EditText) findViewById(R.id.txKodePos);
        lamaTinggalTahun = (EditText) findViewById(R.id.txLamaTinggalTahun);
        lamaTinggalBulan = (EditText) findViewById(R.id.txLamaTinggalBulan);
        noTelpHp = (EditText) findViewById(R.id.txNoTelpHP);
        noTelpKts = (EditText) findViewById(R.id.txNoTelpKts);
        noTeleponRumah = (EditText) findViewById(R.id.txNoTelp);
        jenisPerusahaan = (EditText) findViewById(R.id.txJenisPerusahaan);
        namaPekerjaan = (EditText) findViewById(R.id.txNamaPekerjaan);
        hrdPerusahaan = (EditText) findViewById(R.id.txHRDPerusahaan);
        posisiDiPerusahaan = (EditText) findViewById(R.id.txPosisidiPerusahaan);
        lamaBekerja = (EditText) findViewById(R.id.txLamaBekerja);
        alamatPerusahaan = (EditText) findViewById(R.id.txAlamatPerusahaan);
        teleponPerusahaan = (EditText) findViewById(R.id.txTeleponPerusahaan);
        gaji = (EditText) findViewById(R.id.txGaji);
        tanggalGaji = (EditText) findViewById(R.id.txTanggalGaji);
        namaAnggKelSerumah = (EditText) findViewById(R.id.txNamaAnggotaKeluargaSerumah);
        telAnggKelSerumah = (EditText) findViewById(R.id.txTeleponAnggotaKeluargaSerumah);
        namaAnggKelTidakSerumah =(EditText) findViewById(R.id.txNamaAnggotaKeluargaTidakSerumah);
        jenisHubAnggKelTidakSerumah = (EditText) findViewById(R.id.txJenisHubunganAnggotaKeluargaTidakSerumah);
        alamatAnggKelTidakSerumah = (EditText) findViewById(R.id.txAlamatAnggotaKeluargaTidakSerumah);
        bankRekKlien = (EditText) findViewById(R.id.txBankRekeningKlien);
        lokasiCabangBank = (EditText) findViewById(R.id.txLokasiCabangBank);
        namaPemilikRek = (EditText) findViewById(R.id.txNamaPemilikRekening);
        noRek = (EditText) findViewById(R.id.txNomerRekening);
        tglLahir = (EditText) findViewById(R.id.txTglLahir);

        namaKlien.setError(null);
        email.setError(null);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.status_rumah, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        final ArrayList<String> statRum = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.status_rumah_id)));
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
                                tglLahir.setText(tgl);
                            }
                        });
                cdp.show(getSupportFragmentManager(), null);
            }
        });


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btSave.setVisibility(View.INVISIBLE);
                addKlien();
            }
        });

    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void addKlien() {
        Log.d("klien_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_KLIEN_STORE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_find", response);
                progressBar.setVisibility(View.INVISIBLE);
                btSave.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse response1 = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (response1.isError())
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    }
                    Fragment frag = new KlienParent();

                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.content_frame, frag);
                    ft.addToBackStack(null);
                    ft.commit();

                } catch (JSONException e) {

                    try {
                        JSONObject jsonObject1 = new JSONObject(response);
                        LogoutResponse response1 = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                        Toast.makeText(context, response1.getMessage(), Toast.LENGTH_SHORT).show();

                        Fragment frag = new KlienParent();

                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.content_frame, frag);
                        ft.addToBackStack(null);
                        ft.commit();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    finish();
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
//
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent);
//                    finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("email", email.getText().toString());
                params.put("nama_lengkap", namaKlien.getText().toString());
                params.put("tgl_lahir", tglLahir.getText().toString());
                params.put("telepon", noTeleponRumah.getText().toString());
                params.put("handphone", noTelpHp.getText().toString());
                params.put("telepon_keluarga_tidak_serumah", noTelpKts.getText().toString());
                params.put("nomor_rekening", noRek.getText().toString());
                params.put("no_ktp", noKtp.getText().toString());
                params.put("kode_pos", kodePos.getText().toString());
                params.put("besar_gaji", gaji.getText().toString());
                params.put("kota", kota.getText().toString());
                params.put("posisi", posisiDiPerusahaan.getText().toString());
                params.put("nama_keluarga_serumah", namaAnggKelSerumah.getText().toString());
                params.put("alamat", alamat.getText().toString());
                params.put("nama_keluarga_tidak_serumah", namaAnggKelTidakSerumah.getText().toString());
                params.put("nama_hrd", hrdPerusahaan.getText().toString());
                params.put("alamat_keluarga_tidak_serumah", alamatAnggKelTidakSerumah.getText().toString());
                params.put("telepon_keluarga_serumah", telAnggKelSerumah.getText().toString());
                params.put("nama_bank", bankRekKlien.getText().toString());
                params.put("kecamatan", kecamatan.getText().toString());
                params.put("hubungan_keluarga_tidak_serumah", jenisHubAnggKelTidakSerumah.getText().toString());
                params.put("jenis_kelamin", jenisKel.getText().toString());
                params.put("lokasi_cabang_bank", lokasiCabangBank.getText().toString());
                params.put("tanggal_gajian", tanggalGaji.getText().toString());
                params.put("lama_bekerja", lamaBekerja.getText().toString());
                params.put("status_rumah", "" + (pos+1));
                params.put("nama_pemilik_rekening", namaPemilikRek.getText().toString());
                params.put("jenis_pekerjaan", jenisPerusahaan.getText().toString());
                params.put("alamat_perusahaan", alamatPerusahaan.getText().toString());
                params.put("telepon_perusahaan", teleponPerusahaan.getText().toString());
                params.put("lama_tinggal_bulan", lamaTinggalBulan.getText().toString());
                params.put("lama_tinggal_tahun", lamaTinggalTahun.getText().toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
