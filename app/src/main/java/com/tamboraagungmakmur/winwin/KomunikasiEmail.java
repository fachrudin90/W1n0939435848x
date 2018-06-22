package com.tamboraagungmakmur.winwin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.KomunikasiAdapter;
import com.tamboraagungmakmur.winwin.Model.Email;
import com.tamboraagungmakmur.winwin.Model.EmailResponse;
import com.tamboraagungmakmur.winwin.Model.Klien;
import com.tamboraagungmakmur.winwin.Model.KomunikasiModel;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

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
public class KomunikasiEmail extends Fragment {


    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.txTgl1)
    EditText txTgl1;
    @Bind(R.id.txTgl2)
    EditText txTgl2;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.btCari1)
    Button btCari1;
    @Bind(R.id.rvList)
    RecyclerView rvList;
    @Bind(R.id.btAdd)
    ImageButton btAdd;
    @Bind(R.id.total)
    TextView total;

    private final static String TAG = "KOMUNIKASI_EMAIL";
    private RequestQueue requestQueue;

    private FragmentActivity mActivity;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<KomunikasiModel> dataSet = new ArrayList<>();

    private ArrayList<Klien> klienArrayList = new ArrayList<>();
    private ArrayList<Email> emailArrayList = new ArrayList<>();
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int offset;
    private static final int LIMIT = 20;

    private View view;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private String sessionId;

    public KomunikasiEmail() {
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
            view = inflater.inflate(R.layout.fragment_komunikasi_email, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();
        requestQueue = Volley.newRequestQueue(mActivity);

        Intent intent = new Intent("title");
        intent.putExtra("message", "Email");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        total.setText("Total Email: 0");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh1);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                if (emailArrayList != null) {
                    emailArrayList.clear();
                } else {
                    emailArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getKlien();
            }
        });

        layoutManager = new LinearLayoutManager(mActivity);
        rvList.setLayoutManager(layoutManager);
        adapter = new KomunikasiAdapter(mActivity, emailArrayList);
        rvList.setAdapter(adapter);

//        GetData();
        sessionManager = new SessionManager(mActivity);
        sessionId = sessionManager.getSessionId();
        getKlien();

        return view;
    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void GetData() {

        dataSet.clear();
        for (int i = 0; i < 15; i++) {

            KomunikasiModel komunikasiModel = new KomunikasiModel("2017-02-01\nOleh:\nridwan@pinjamwinwin.com", "0821015158\n1184218 - Teguh Susanto", "Yth. Kami coba hubungi hp anda namun belum terhubung. Mohon hub kami- win-win", "Terkirim tapi tanpa report");
            dataSet.add(komunikasiModel);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.txTgl1, R.id.txTgl2, R.id.btCari, R.id.btCari1, R.id.btAdd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txTgl1:
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTgl1.setText(tgl);
                            }
                        });
                cdp.show(getChildFragmentManager(), null);
                break;
            case R.id.txTgl2:
                CalendarDatePickerDialogFragment cdp2 = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTgl2.setText(tgl);
                            }
                        });
                cdp2.show(getChildFragmentManager(), null);
                break;
            case R.id.btCari:
                offset = 0;
                if (emailArrayList != null) {
                    emailArrayList.clear();
                } else {
                    emailArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getKlien1(txTgl1.getText().toString(), txTgl2.getText().toString());
                break;
            case R.id.btCari1:
                offset = 0;
                if (emailArrayList != null) {
                    emailArrayList.clear();
                } else {
                    emailArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                getKlien2(txPencarian.getText().toString().replace(" ", "+"));
                break;
            case R.id.btAdd:
                Fragment frag = new AddEmailFragment();

                FragmentManager manager = ((AppCompatActivity) mActivity).getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frame, frag);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }

    private void getKlien() {
        Log.d("klien_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_EMAIL_ALL + "/" + LIMIT + "/" + offset + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    EmailResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), EmailResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Email: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Email klien = new Gson().fromJson(jsonObject.toString(), Email.class);
                            emailArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(emailArrayList.size() - 1);
//                            getEmail(klienResponse.getData()[i].getId());
                        }
//                        adapter.notifyDataSetChanged();
                        offset += 1;
                        Log.d("offset", Integer.toString(offset));
                        getKlien();

//                        offset += 1;
//                        getKlien();

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
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
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

    private void getKlien1(final String tgl1, final String tgl2) {
        Log.d("klien_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_EMAIL_ALL + "/" + LIMIT + "/" + offset + "/" + tgl1 + "/" + tgl2 + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    EmailResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), EmailResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        if (total != null) {
                            total.setText("Total Email: " + klienResponse.getCount());
                        }
                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Email klien = new Gson().fromJson(jsonObject.toString(), Email.class);
                            emailArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(emailArrayList.size() - 1);
//                            getEmail(klienResponse.getData()[i].getId());
                        }
//                        adapter.notifyDataSetChanged();
                        offset += 1;
                        Log.d("offset", Integer.toString(offset));
                        getKlien1(tgl1, tgl2);

//                        offset += 1;
//                        getKlien();

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
//                                        getKlien1(tgl1, tgl2);
//                                    }
//                                }
//                            }
//                        });
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
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

    private void getKlien2(final String cari) {
        Log.d("klien_all", "ok");

        String query = "";
        if (cari.compareTo("") != 0) {
            query = "/" + cari;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_EMAIL_FIND + query + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    EmailResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), EmailResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Email klien = new Gson().fromJson(jsonObject.toString(), Email.class);
                            emailArrayList.add(klienResponse.getData()[i]);
//                            adapter.notifyItemChanged(emailArrayList.size() - 1);
//                            getEmail(klienResponse.getData()[i].getId());
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
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

    private void getEmail(String klien) {
        Log.d("email_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KOMUNIKASI_EMAIL_BY_CLIENT + "/" + klien + "?_session=" + sessionId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("email_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    EmailResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), EmailResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//                        JSONObject jsonObject = new JSONObject(klienResponse.getData()[i].toString());
//                        Klien klien = new Gson().fromJson(jsonObject.toString(), Klien.class);
                            emailArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(emailArrayList.size() - 1);
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

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }
}
