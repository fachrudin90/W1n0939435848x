package com.tamboraagungmakmur.winwin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.tamboraagungmakmur.winwin.Adapter.HistoriTahapan1Adapter;
import com.tamboraagungmakmur.winwin.Model.Notes;
import com.tamboraagungmakmur.winwin.Model.NotesResponse;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 10/19/2017.
 */

public class PengNoteActivity extends FragmentActivity {

    private final static String TAG = "PENG_NOTE";
    private RequestQueue requestQueue;

    private View view;
    private Context context;

    private String id, idPengajuan;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Notes> historiTahapanArrayList = new ArrayList<>();
    private HistoriTahapan1Adapter adapter;

    private TextInputEditText note;
    private Button save;
    private ProgressBar progressBar;

    private int pos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_klien_data4);

        context = PengNoteActivity.this;
        requestQueue = Volley.newRequestQueue(context);

        id = getIntent().getStringExtra("id");

        initView();
        getHistori();

    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_note);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HistoriTahapan1Adapter(historiTahapanArrayList);
        recyclerView.setAdapter(adapter);

        if (historiTahapanArrayList != null) {
            historiTahapanArrayList.clear();
        } else {
            historiTahapanArrayList = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();

        note = (TextInputEditText) findViewById(R.id.klien);
        save = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                addNote();
            }
        });

    }

    private void addNote() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_KLIEN_NOTE + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                save.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse pengajuanDetail = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (!pengajuanDetail.isError()) {
                        if (historiTahapanArrayList != null) {
                            historiTahapanArrayList.clear();
                        } else {
                            historiTahapanArrayList = new ArrayList<>();
                        }
                        adapter.notifyDataSetChanged();
                        getHistori();
                        note.setText("");
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
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("note", note.getText().toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void getHistori() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KLIEN_NOTES + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    NotesResponse pengajuanDetail = new Gson().fromJson(jsonObject1.toString(), NotesResponse.class);

                    if (pengajuanDetail.getData().length > 0) {
                        for (int i = 0; i < pengajuanDetail.getData().length; i++) {
                            historiTahapanArrayList.add(pengajuanDetail.getData()[i]);
                            adapter.notifyItemChanged(historiTahapanArrayList.size() - 1);
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
