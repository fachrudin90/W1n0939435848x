package com.tamboraagungmakmur.winwin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.tamboraagungmakmur.winwin.Adapter.InvestorNotesAdapter;
import com.tamboraagungmakmur.winwin.Model.InvestorNotes;
import com.tamboraagungmakmur.winwin.Model.InvestorNotesResponse;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
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
 * Created by innan on 12/9/2017.
 */

public class InvestorNotesActivity extends FragmentActivity {

    private static final Object TAG = "INVESTASI_NOTES";
    @Bind(R.id.btInput)
    Button btInput;
    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.txKeterangan)
    TextView txKeterangan;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.rvList)
    RecyclerView rvList;
    @Bind(R.id.refresh1)
    SwipeRefreshLayout refresh1;
    @Bind(R.id.btClose)
    Button btClose;
    private RequestQueue requestQueue;
    private Context context;

    private LinearLayoutManager linearLayoutManager;
    private InvestorNotesAdapter adapter;
    private ArrayList<InvestorNotes> investasiInvestorArrayList = new ArrayList<>();


    private static final int LIMIT = 20;
    private int offset = 0;
    private String id;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_notes);
        ButterKnife.bind(this);
        context = InvestorNotesActivity.this;
        progressDialog = new ProgressDialog(InvestorNotesActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        id = getIntent().getStringExtra("id");
        String keterangan = getIntent().getStringExtra("keterangan");
        txKeterangan.setText(keterangan);

        requestQueue = Volley.newRequestQueue(context);


        linearLayoutManager = new LinearLayoutManager(context);
        rvList.setLayoutManager(linearLayoutManager);
        adapter = new InvestorNotesAdapter(investasiInvestorArrayList) {
            @Override
            public void deletes(String id) {
                dialogDelete(id);
            }

        };
        rvList.setAdapter(adapter);


        getCatatan();


        refresh1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                getCatatan();
            }
        });


    }

    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }


    private void cari(final String cari) {

        Log.d("investor_cari", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVESTOR_NOTES + "/" + cari + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                refresh1.setRefreshing(false);
                Log.d("investor_cari", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    InvestorNotesResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), InvestorNotesResponse.class);

                    if (klienResponse.getData().length > 0) {
                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            investasiInvestorArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(investasiInvestorArrayList.size() - 1);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.gagal));

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
//                    getActivity().finish();
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

    }

    private void getCatatan() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_INVESTASI_INVESTOR_NOTES + "/" + id + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                refresh1.setRefreshing(false);
                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    InvestorNotesResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), InvestorNotesResponse.class);

                    if (klienResponse.getData().length > 0) {
                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            investasiInvestorArrayList.add(klienResponse.getData()[i]);
                            adapter.notifyItemChanged(investasiInvestorArrayList.size() - 1);
                        }
                        offset += 1;
                        getCatatan();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.gagal));

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
//                    getActivity().finish();
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

    private void input(final String catatan, final Dialog dialog) {

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_INVESTASI_INVESTOR_NOTES + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse logoutResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    if (!logoutResponse.isError()) {
                        dialog.dismiss();
                        refresh();
                        getCatatan();
                    } else {
                        GlobalToast.ShowToast(context, getString(R.string.gagal));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.gagal));

                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

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
                params.put("note", catatan);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);

    }

    private void detele(String id, final Dialog dialog) {

        SessionManager sessionManager = new SessionManager(InvestorNotesActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, AppConf.URL_INVESTASI_INVESTOR_NOTES + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse logoutResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    if (!logoutResponse.isError()) {
                        dialog.dismiss();
                        refresh();
                        getCatatan();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.gagal));

                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

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

    }

    private void refresh() {
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
        if (investasiInvestorArrayList != null) {
            investasiInvestorArrayList.clear();
        } else {
            investasiInvestorArrayList = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();
        offset = 0;
    }


    public void dialogInput() {

        final Dialog dialog = new Dialog(context, R.style.ThemeDialogHalf);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_investor_notes, null);

        final EditText etCatatan = (EditText) view.findViewById(R.id.etCatatan);
        Button btOk = (Button) view.findViewById(R.id.ok);
        Button btCancel = (Button) view.findViewById(R.id.cancel);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                input(etCatatan.getText().toString(), dialog);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        dialog.show();

    }

    public void dialogDelete(final String id) {

        final Dialog dialog = new Dialog(context, R.style.ThemeDialogHalf);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_investor_notes_delete, null);

        Button btOk = (Button) view.findViewById(R.id.ok);
        Button btCancel = (Button) view.findViewById(R.id.cancel);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detele(id, dialog);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);

        dialog.show();

    }

    @OnClick({R.id.btInput, R.id.btCari, R.id.btClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btInput:
                dialogInput();
                break;
            case R.id.btClose:
                finish();
                break;
            case R.id.btCari:
                if (txPencarian.getText().toString().compareTo("") == 0) {
                    refresh();
                    getCatatan();
                } else {
                    refresh();
                    getCatatan();

//                    cari(txPencarian.getText().toString().replace(" ", "+"));
                }
                break;
        }
    }
}
