package com.tamboraagungmakmur.winwin;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.tamboraagungmakmur.winwin.Adapter.KlienAdapter;
import com.tamboraagungmakmur.winwin.Model.Klien;
import com.tamboraagungmakmur.winwin.Model.KlienModel;
import com.tamboraagungmakmur.winwin.Model.KlienResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class KlienParent extends Fragment {


    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.rvList)
    RecyclerView rvList;
    @Bind(R.id.btAdd)
    ImageButton btAdd;
    @Bind(R.id.total)
    TextView total;

    private final static String TAG = "KLIEN_PARENT";
    private RequestQueue requestQueue;

    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<KlienModel> dataSet = new ArrayList<>();
    private ArrayList<Klien> klienArrayList = new ArrayList<>();


    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    private View view;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    public KlienParent() {
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
            view = inflater.inflate(R.layout.fragment_klien_parent, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
        requestQueue = Volley.newRequestQueue(mActivity);

        Intent intent = new Intent("title");
        intent.putExtra("message", "Klien");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        total.setText("Total Klien: 0");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent("loading");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                requestQueue.cancelAll(TAG);
                offset = 0;
                if (klienArrayList != null) {
                    klienArrayList.clear();
                } else {
                    klienArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getKlien();
            }
        });

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new KlienAdapter(mActivity, klienArrayList);
        rvList.setAdapter(adapter);


        GetData();
        getKlien();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("klien"));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent("loading");
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
        requestQueue.cancelAll(TAG);
        offset = 0;
        if (klienArrayList != null) {
            klienArrayList.clear();
        } else {
            klienArrayList = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();
        getKlien();
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
            Intent intent1 = new Intent("loading");
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent1);
            requestQueue.cancelAll(TAG);
            offset = 0;
            if (rvList == null) {
                rvList = (RecyclerView) view.findViewById(R.id.rvList);
                layoutManager = new LinearLayoutManager(mActivity);
                rvList.setLayoutManager(layoutManager);
                adapter = new KlienAdapter(mActivity, klienArrayList);
                rvList.setAdapter(adapter);
            }
            if (klienArrayList != null) {
                klienArrayList.clear();
            } else {
                klienArrayList = new ArrayList<>();
            }
            adapter.notifyDataSetChanged();
            getKlien();
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void GetData() {

        dataSet.clear();
        for (int i = 0; i < 15; i++) {

            KlienModel klienModel = new KlienModel("1601-22", "Yulia Rachmawati\nNo. KTP: 212121000112211\nEmail:rachmawati@gmail.com", "08131313131", "Jl. Angsa No.43", "Laki-laki", "Klien Aktif", "sudah", "");
            dataSet.add(klienModel);
        }

        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btCari, R.id.btAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btCari:
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent("loading");
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                requestQueue.cancelAll(TAG);
                offset = 0;
                if (klienArrayList != null) {
                    klienArrayList.clear();
                } else {
                    klienArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getKlien1(txPencarian.getText().toString().replace(" ", "+"));
                break;
            case R.id.btAdd:

//                Fragment frag = new AddDataKlienFragment();
//
//                FragmentManager manager = ((AppCompatActivity) mActivity).getSupportFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                ft.replace(R.id.content_frame, frag);
//                ft.addToBackStack(null);
//                ft.commit();

                Intent intent1 = new Intent(mActivity, AddDataKlienFragment.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);

                break;
        }
    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KLIEN_ALL + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KlienResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KlienResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Klien: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            klienArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(klienArrayList.size() - 1);
                        }
//                        adapter.notifyDataSetChanged();
                        offset += 1;
                        Log.d("offset", Integer.toString(offset));
                        getKlien();

//                        loading = true;
//                        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                visibleItemCount = layoutManager.getChildCount();
//                                totalItemCount = layoutManager.getItemCount();
//                                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//
//                                if (loading) {
//                                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                                        loading = false;
//                                        offset += 1;
//                                        Log.d("offset", Integer.toString(offset));
//                                        getKlien();
//                                    }
//                                }
//                            }
//                        });
                    } else {
                        Intent intent = new Intent("loading");
                        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(mActivity, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mActivity, "no connection", Toast.LENGTH_SHORT).show();
                } else {

//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
//                    Intent intent = new Intent(mActivity, LoginActivity.class);
//                    mActivity.startActivity(intent);
//                    mActivity.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(mActivity);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

    private void getKlien1(final String cari) {
        Log.d("klien_all", "ok");

        String query = "";
        if (cari.compareTo("") != 0) {
            query = "/" + cari;
        }
        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KLIEN_FIND + query + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_find", response);
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KlienResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KlienResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            klienArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(klienArrayList.size() - 1);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(mActivity, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mActivity, "no connection", Toast.LENGTH_SHORT).show();
                } else {

//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
//                    Intent intent = new Intent(mActivity, LoginActivity.class);
//                    mActivity.startActivity(intent);
//                    mActivity.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(mActivity);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }

}
