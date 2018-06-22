package com.tamboraagungmakmur.winwin;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.tamboraagungmakmur.winwin.Adapter.PengajuanAdapter;
import com.tamboraagungmakmur.winwin.Model.KlienResponse;
import com.tamboraagungmakmur.winwin.Model.PengajuanResponse;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskListFragment extends Fragment {


    @Bind(R.id.txTanggal)
    EditText txTanggal;
    @Bind(R.id.txWaktu)
    EditText txWaktu;
    @Bind(R.id.txTargetTgl)
    EditText txTargetTgl;
    @Bind(R.id.txTipeTugas)
    Spinner txTipeTugas;
    @Bind(R.id.txPengajuan)
    EditText txPengajuan;
    @Bind(R.id.txPelaksana)
    EditText txPelaksana;
    @Bind(R.id.txTugas)
    EditText txTugas;
    @Bind(R.id.save)
    Button btSave;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;

    private final static String TAG = "ADD_TASKLIST";
    private RequestQueue requestQueue;

    private List<String> listPengajuan = new ArrayList<>();
    private RecyclerView rv1;
    private LinearLayoutManager linearLayoutManager;
    private PengajuanAdapter pengajuanAdapter;

    private ArrayAdapter<String> adapter1;
    private int offset;
    private static final int LIMIT = 20;

    private FragmentActivity mActivity;

    private String idPengajuan, idUser;
    private int workItem = 1;

    private View view;
    public AddTaskListFragment() {
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
            view = inflater.inflate(R.layout.fragment_add_task_list, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        ButterKnife.bind(this, view);

        mActivity = getActivity();

        requestQueue = Volley.newRequestQueue(getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.task_params, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txTipeTugas.setAdapter(adapter);

        txTipeTugas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workItem = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        rv1 = (RecyclerView) view.findViewById(R.id.rv1);
//        linearLayoutManager = new LinearLayoutManager(getContext());
//        pengajuanAdapter = new PengajuanAdapter(listPengajuan);
//        rv1.setLayoutManager(linearLayoutManager);
//        rv1.setAdapter(pengajuanAdapter);
//        rv1.setNestedScrollingEnabled(false);

//        txPengajuan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rv1.setVisibility(View.VISIBLE);
//            }
//        });

//         adapter1 = new ArrayAdapter<>(mActivity,
//                android.R.layout.simple_dropdown_item_1line, listPengajuan);
//        txPengajuan.setAdapter(adapter);
//
//        getKlien();

        txPengajuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ListPengajuanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
            }
        });

        txPelaksana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ListPelaksanaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btSave.setVisibility(View.INVISIBLE);
                storeTask();
            }
        });

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mMessageReceiver,
                new IntentFilter("pengajuan_terkait"));

        LocalBroadcastManager.getInstance(mActivity).registerReceiver(mMessageReceiver1,
                new IntentFilter("user_terkait"));

        return view;
    }

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            idUser = intent.getStringExtra("id");
            txPelaksana.setText(intent.getStringExtra("nama"));
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            idPengajuan = intent.getStringExtra("id");
            txPengajuan.setText(intent.getStringExtra("nomor_pengajuan") + " | " + intent.getStringExtra("nomor_pelanggan") + " " + intent.getStringExtra("nama_lengkap"));
        }
    };

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mMessageReceiver1);
        super.onDestroy();
    }

    @OnClick({R.id.txTanggal, R.id.txWaktu, R.id.txTargetTgl})
    public void onClick(View view) {
        switch (view.getId()) {
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
                cdp2.show(getChildFragmentManager(), null);
                break;
            case R.id.txTargetTgl:
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTargetTgl.setText(tgl);
                            }
                        });
                cdp.show(getChildFragmentManager(), null);
                break;
            case R.id.txWaktu:
                break;
        }
    }

    private void storeTask() {
        Log.d("store_task", "id " + idPengajuan);

//        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_TASK_STORE + "/" + idUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                progressBar.setVisibility(View.INVISIBLE);
                btSave.setVisibility(View.VISIBLE);
//                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (!klienResponse.isError()) {

                        Fragment frag = new TasklistParent();

                        FragmentManager manager = ((AppCompatActivity) mActivity).getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.content_frame, frag);
                        ft.addToBackStack(null);
                        ft.commit();
                    } else {
                        Toast.makeText(mActivity, "gagal membuat task baru", Toast.LENGTH_SHORT).show();
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
//                    sessionManager.setPage(0);
//                    sessionManager.logoutUser();
//
//                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));
//
//
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
                params.put("deadline", txTargetTgl.getText().toString());
                params.put("task", txTugas.getText().toString());
                params.put("pengajuan_id", idPengajuan);
                params.put("workitem_id", ""+workItem);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(mActivity).addToRequestQueue(stringRequest);

    }
}
