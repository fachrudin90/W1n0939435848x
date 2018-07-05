package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.tamboraagungmakmur.winwin.Model.RefPerusahaan;
import com.tamboraagungmakmur.winwin.Model.RefRlc;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by innan on 11/28/2017.
 */

public class RefRlcEditActivity extends FragmentActivity {

    private static final Object TAG = "REF_RLC_EDIT";
    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.txNama)
    TextInputEditText txNama;
    @Bind(R.id.input1)
    TextInputLayout input1;
    @Bind(R.id.spinner1)
    Spinner spinner1;
    @Bind(R.id.input2)
    TextInputLayout input2;
    @Bind(R.id.spinner2)
    Spinner spinner2;
    @Bind(R.id.input3)
    TextInputLayout input3;
    @Bind(R.id.spinner3)
    Spinner spinner3;
    @Bind(R.id.input4)
    TextInputLayout input4;
    @Bind(R.id.spinner4)
    Spinner spinner4;
    @Bind(R.id.input5)
    TextInputLayout input5;
    @Bind(R.id.save)
    Button save;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    private String id;

    private Context context;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ref_rlc_edit);
        ButterKnife.bind(this);
        context = RefRlcEditActivity.this;
        requestQueue = Volley.newRequestQueue(context);
        id = getIntent().getStringExtra("id");

        initView();
    }

    private void initView() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.yes_no, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);
        spinner4.setAdapter(adapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                edit();
            }
        });

        getDetail();
    }

    private void getDetail() {
        Log.d("klien_all", id);
        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_REF_RLC_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    RefRlc klienResponse = new Gson().fromJson(jsonObject1.getString("data"), RefRlc.class);

                    if (klienResponse != null) {
                        txNama.setText(klienResponse.getShort_desc());

                        if (klienResponse.getAdmin().equalsIgnoreCase("Yes")) {
                            spinner1.setSelection(0);
                        } else {
                            spinner1.setSelection(1);
                        }

                        if (klienResponse.getSuperuser().equalsIgnoreCase("Yes")) {
                            spinner2.setSelection(0);
                        } else {
                            spinner2.setSelection(1);
                        }

                        if (klienResponse.getReferenced().equalsIgnoreCase("Yes")) {
                            spinner3.setSelection(0);
                        } else {
                            spinner3.setSelection(1);
                        }

                        if (klienResponse.getNotif().equalsIgnoreCase("Yes")) {
                            spinner4.setSelection(0);
                        } else {
                            spinner4.setSelection(1);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

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

    private void edit() {

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_REF_RLC_UPDATE + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                save.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("klien_edit_rlc", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                    Intent intent = new Intent("ref_rlc");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                save.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

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
                params.put("short_desc", txNama.getText().toString());
                params.put("viewed_by_admin", spinner1.getSelectedItemPosition() == 0 ? "true" : "false");
                params.put("viewed_by_superuser", spinner2.getSelectedItemPosition() == 0 ? "true" : "false");
                params.put("referenced", spinner3.getSelectedItemPosition() == 0 ? "true" : "false");
                params.put("send_notifications", spinner4.getSelectedItemPosition() == 0 ? "true" : "false");

                AndLog.ShowLog("RLCLOGS", params.toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
