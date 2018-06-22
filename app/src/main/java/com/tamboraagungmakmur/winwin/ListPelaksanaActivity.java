package com.tamboraagungmakmur.winwin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
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
import com.tamboraagungmakmur.winwin.Adapter.UserAdapter;
import com.tamboraagungmakmur.winwin.Model.User;
import com.tamboraagungmakmur.winwin.Model.UserResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/6/2017.
 */

public class ListPelaksanaActivity extends Activity {

    private final static String TAG = "LIST_PELAKSANA";
    private RequestQueue requestQueue;

    private ArrayList<User> pengajuanArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserAdapter pengajuanAdapter;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpelaksana);
        requestQueue = Volley.newRequestQueue(this);
        initView();

        getPengajuan();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("user_terkait"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBackPressed();
        }
    };

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rvlistpeng);
        linearLayoutManager = new LinearLayoutManager(this);
        pengajuanAdapter = new UserAdapter(pengajuanArrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pengajuanAdapter);

    }

    private void getPengajuan() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(ListPelaksanaActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_USER_ALL + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
//                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    UserResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), UserResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            pengajuanArrayList.add(klienResponse.getData()[i]);
                            pengajuanAdapter.notifyItemChanged(pengajuanArrayList.size() - 1);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(ListPelaksanaActivity.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(ListPelaksanaActivity.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(ListPelaksanaActivity.this, LoginActivity.class);
                    ListPelaksanaActivity.this.startActivity(intent);
                    ListPelaksanaActivity.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(ListPelaksanaActivity.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(ListPelaksanaActivity.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(ListPelaksanaActivity.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(ListPelaksanaActivity.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(ListPelaksanaActivity.this, LoginActivity.class);
//                    ListPelaksanaActivity.this.startActivity(intent);
//                    ListPelaksanaActivity.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(ListPelaksanaActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(ListPelaksanaActivity.this).addToRequestQueue(stringRequest);

    }

}
