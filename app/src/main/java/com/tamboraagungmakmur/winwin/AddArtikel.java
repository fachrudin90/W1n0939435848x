package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddArtikel extends AppCompatActivity {

    private static final Object TAG = "ADD_ARTIKEL";
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

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artikel);
        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(this);

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
    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_ARTICLE_STORE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (!klienResponse.isError()) {
                        Toast.makeText(AddArtikel.this, "artikel telah ditambahkan", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddArtikel.this, "error", Toast.LENGTH_SHORT).show();
                    }


                    Intent intent = new Intent(AddArtikel.this, DaftarArtikel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(AddArtikel.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(AddArtikel.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0); sessionManager.setPage(0);

                    Intent intent = new Intent(AddArtikel.this, LoginActivity.class);
                    AddArtikel.this.startActivity(intent);
                    AddArtikel.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(AddArtikel.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(AddArtikel.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(AddArtikel.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(AddArtikel.this, getString(R.string.session_expired));
//
//                    Intent intent = new Intent(AddArtikel.this, LoginActivity.class);
//                    AddArtikel.this.startActivity(intent);
//                    AddArtikel.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(AddArtikel.this);
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
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(AddArtikel.this).addToRequestQueue(stringRequest);

    }
}
