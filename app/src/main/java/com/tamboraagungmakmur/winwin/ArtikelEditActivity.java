package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by innan on 9/8/2017.
 */

public class ArtikelEditActivity extends AppCompatActivity {

    @Bind(R.id.btBack)
    ImageButton btBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.txTanggal)
    EditText txTanggal;
    @Bind(R.id.status1)
    Spinner txStatus;
    @Bind(R.id.txPenulis)
    EditText txPenulis;
    @Bind(R.id.txKataKunci)
    EditText txKataKunci;
    @Bind(R.id.txJudul)
    EditText txJudul;
    @Bind(R.id.txIsi)
    EditText txIsi;
    @Bind(R.id.btSave)
    Button btSave;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    private int pos = 0;
    private String id;

    
    private final static String TAG = "ARTIKEL_EDIT";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artikel);
        ButterKnife.bind(this);

        id = this.getIntent().getStringExtra("id");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_artikel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txStatus.setAdapter(adapter);

        txStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String status = getIntent().getStringExtra("status");
        String[] myResArray = getResources().getStringArray(R.array.status_artikel);
        List<String> myResArrayList = Arrays.asList(myResArray);

        for(int i=0; i<myResArrayList.size(); i++) {
            if (status.compareTo(myResArrayList.get(i)) == 0) {
                pos = i;
            }
        }

        txTanggal.setText(getIntent().getStringExtra("tanggal"));
        txPenulis.setText(getIntent().getStringExtra("penulis"));
        txKataKunci.setText(getIntent().getStringExtra("tags"));
        txJudul.setText(getIntent().getStringExtra("judul"));
        txIsi.setText(getIntent().getStringExtra("konten"));
    }

    @Override
    public void onStop() {
        VolleyHttp.getInstance(ArtikelEditActivity.this).getRequestQueue().cancelAll(TAG);
        super.onStop();
    }

    @OnClick({R.id.btBack, R.id.txTanggal, R.id.btSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.txTanggal:
                CalendarDatePickerDialogFragment cdp2 = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTanggal.setText(tgl);
                            }
                        });
                cdp2.show(getSupportFragmentManager(), null);
                break;
            case R.id.btSave:
                btSave.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                storeArtikel();
                break;
        }
    }

    private void storeArtikel() {
        Log.d("klien_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_ARTICLE_UPDATE + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    if (!klienResponse.isError()) {
                        Toast.makeText(ArtikelEditActivity.this, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ArtikelEditActivity.this, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    Intent intent = new Intent(ArtikelEditActivity.this, DaftarArtikel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(ArtikelEditActivity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(ArtikelEditActivity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(ArtikelEditActivity.this, LoginActivity.class);
                    ArtikelEditActivity.this.startActivity(intent);
                    ArtikelEditActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(ArtikelEditActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ArtikelEditActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(ArtikelEditActivity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//                    GlobalToast.ShowToast(ArtikelEditActivity.this, getString(R.string.session_expired));
//
//                    Intent intent = new Intent(ArtikelEditActivity.this, LoginActivity.class);
//                    ArtikelEditActivity.this.startActivity(intent);
//                    ArtikelEditActivity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(ArtikelEditActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("date", txTanggal.getText().toString());
                params.put("status", "" + (pos+1));
                params.put("author", txPenulis.getText().toString());
                params.put("tags", txKataKunci.getText().toString());
                params.put("judul", txJudul.getText().toString());
                params.put("konten", txIsi.getText().toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        VolleyHttp.getInstance(ArtikelEditActivity.this).addToRequestQueue(stringRequest);

    }

}
