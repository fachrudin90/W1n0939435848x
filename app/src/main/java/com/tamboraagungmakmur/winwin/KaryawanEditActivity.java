package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.tamboraagungmakmur.winwin.Model.KarEditResponse;
import com.tamboraagungmakmur.winwin.Model.Karyawan;
import com.tamboraagungmakmur.winwin.Model.KaryawanResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 10/2/2017.
 */

public class KaryawanEditActivity extends FragmentActivity {

    private final static String TAG = "KARYAWAN_EDIT";
    private RequestQueue requestQueue;

    private TextInputEditText nama, email, masuk, keluar;
    private Spinner spinner1;
    private Button save;
    private ProgressBar progressBar;

    private String id;
    private String sudahKeluar = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karedit);
        id = getIntent().getStringExtra("id");
        requestQueue = Volley.newRequestQueue(this);

        initView();

        getKlien();
    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {
        nama = (TextInputEditText) findViewById(R.id.nama);
        email = (TextInputEditText) findViewById(R.id.email);
        masuk = (TextInputEditText) findViewById(R.id.masuk);
        keluar = (TextInputEditText) findViewById(R.id.keluar);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        save = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                updateKar();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_karyawan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sudahKeluar = "false";
                } else {
                    sudahKeluar = "true";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
                                masuk.setText(tgl);
                            }
                        });
                cdp.show(getSupportFragmentManager(), null);
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "yyyy-MM-dd");
                                keluar.setText(tgl);
                            }
                        });
                cdp.show(getSupportFragmentManager(), null);
            }
        });
    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(KaryawanEditActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_USER_KARYAWAN + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KaryawanResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KaryawanResponse.class);
                    Karyawan karyawan = klienResponse.getData()[0];

                    nama.setText(karyawan.getNama_karyawan());
                    email.setText(karyawan.getKar_email_winwin());
                    masuk.setText(karyawan.getKar_tgl_masuk());
                    keluar.setText(karyawan.getKar_tgl_keluar());

                    boolean isKeluar = karyawan.isKar_sudah_keluar();
                    if (isKeluar) {
                        spinner1.setSelection(1);
                    } else {
                        spinner1.setSelection(0);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(KaryawanEditActivity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(KaryawanEditActivity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(KaryawanEditActivity.this, LoginActivity.class);
                    KaryawanEditActivity.this.startActivity(intent);
                    KaryawanEditActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(KaryawanEditActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(KaryawanEditActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(KaryawanEditActivity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(KaryawanEditActivity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(KaryawanEditActivity.this, LoginActivity.class);
//                    KaryawanEditActivity.this.startActivity(intent);
//                    KaryawanEditActivity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(KaryawanEditActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(KaryawanEditActivity.this).addToRequestQueue(stringRequest);

    }

    private void updateKar() {
        Log.d("kar_edit", "ok");

        SessionManager sessionManager = new SessionManager(KaryawanEditActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_USER_KARDATA + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                Log.d("kar_edit", id + " --> " + response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KarEditResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KarEditResponse.class);

                    Toast.makeText(KaryawanEditActivity.this, klienResponse.getData(), Toast.LENGTH_SHORT).show();
                    onBackPressed();


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(KaryawanEditActivity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(KaryawanEditActivity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(KaryawanEditActivity.this, LoginActivity.class);
                    KaryawanEditActivity.this.startActivity(intent);
                    KaryawanEditActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(KaryawanEditActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(KaryawanEditActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(KaryawanEditActivity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(KaryawanEditActivity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(KaryawanEditActivity.this, LoginActivity.class);
//                    KaryawanEditActivity.this.startActivity(intent);
//                    KaryawanEditActivity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(KaryawanEditActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("nama_lengkap", nama.getText().toString());
                params.put("email_winwin", email.getText().toString());
                params.put("tgl_masuk", masuk.getText().toString());
                params.put("tgl_keluar", keluar.getText().toString());
                params.put("sudah_keluar", sudahKeluar);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(KaryawanEditActivity.this).addToRequestQueue(stringRequest);

    }

}
