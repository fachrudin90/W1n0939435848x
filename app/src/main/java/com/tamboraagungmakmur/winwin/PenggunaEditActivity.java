package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.tamboraagungmakmur.winwin.Model.KarEditResponse;
import com.tamboraagungmakmur.winwin.Model.Pengguna;
import com.tamboraagungmakmur.winwin.Model.PenggunaResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 10/2/2017.
 */

public class PenggunaEditActivity extends FragmentActivity {

    private final static String TAG = "PENGGUNA_EDIT";
    private RequestQueue requestQueue;

    private TextInputEditText username;
    private Button save;
    private ProgressBar progressBar;
    private CheckBox item1, item2, item3, item4, item5, item6, item7;
    private boolean role1 = false, role2=false, role3=false, role4=false, role5=false, role6=false, role7 = false;

    private String id;
    private Context context;

    private Spinner spinner1, spinner2;

    private String aktif = "true";
    private boolean ro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penggedit);
        id = getIntent().getStringExtra("id");
        context = PenggunaEditActivity.this;
        requestQueue = Volley.newRequestQueue(context);

        initView();

        getKlien();
    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {
        username = (TextInputEditText) findViewById(R.id.username);
        save = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        item1 = (CheckBox) findViewById(R.id.item1);
        item2 = (CheckBox) findViewById(R.id.item2);
        item3 = (CheckBox) findViewById(R.id.item3);
        item4 = (CheckBox) findViewById(R.id.item4);
        item5 = (CheckBox) findViewById(R.id.item5);
        item6 = (CheckBox) findViewById(R.id.item6);
        item7 = (CheckBox) findViewById(R.id.item7);

        item1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    role1 = isChecked;
            }
        });

        item2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                role2 = isChecked;
            }
        });

        item3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                role3 = isChecked;
            }
        });

        item4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                role4 = isChecked;
            }
        });

        item5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                role5 = isChecked;
            }
        });

        item6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                role6 = isChecked;
            }
        });

        item7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                role7 = isChecked;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                updateKar();
            }
        });

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_pengguna, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    aktif = "false";
                } else {
                    aktif = "true";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.penugasan_pengguna, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ro = false;
                } else {
                    ro = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_USER_USER + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PenggunaResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), PenggunaResponse.class);
                    Pengguna karyawan = klienResponse.getData()[0];

                    username.setText(karyawan.getUsername());
                    if (!karyawan.is_disabled()) {
                        spinner1.setSelection(0);
                        aktif = "false";
                    } else {
                        spinner1.setSelection(1);
                        aktif = "true";
                    }

                    if (karyawan.is_ro()) {
                        spinner2.setSelection(1);
                        ro = true;
                    } else {
                        spinner2.setSelection(0);
                        ro = false;
                    }

                    if (karyawan.getRoles() != null) {
                        String roles[] = karyawan.getRoles().split(", ");
                        for (int i = 0; i < roles.length; i++) {
                            switch (roles[i]) {
                                case "Analis":
                                    item1.setChecked(true);
                                    break;
                                case "Pelunasan":
                                    item2.setChecked(true);
                                    break;
                                case "Perpanjangan":
                                    item3.setChecked(true);
                                    break;
                                case "Denda":
                                    item4.setChecked(true);
                                    break;
                                case "Collection":
                                    item5.setChecked(true);
                                    break;
                                case "Pencairan":
                                    item6.setChecked(true);
                                    break;
                                case "Persetujuan":
                                    item7.setChecked(true);
                                    break;
                            }
                        }
                    }

                } catch (JSONException e) {
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
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void updateKar() {
        Log.d("kar_edit", id);

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_USER_DATA + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KarEditResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KarEditResponse.class);

                    Toast.makeText(context, klienResponse.getData(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent("pengguna");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    onBackPressed();


                } catch (JSONException e) {
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
                params.put("username", username.getText().toString());
                params.put("aktif", aktif);
                params.put("ro", ro?"true":"false");
                params.put("role1", role1?"true":"false");
                params.put("role2", role2?"true":"false");
                params.put("role3", role3?"true":"false");
                params.put("role4", role4?"true":"false");
                params.put("role5", role5?"true":"false");
                params.put("role6", role6?"true":"false");
                params.put("role7", role7?"true":"false");
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }
    
}
