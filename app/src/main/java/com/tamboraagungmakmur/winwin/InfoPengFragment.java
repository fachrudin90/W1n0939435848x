package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.tamboraagungmakmur.winwin.Model.PengajuanDetail;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/8/2017.
 */

public class InfoPengFragment extends Fragment {

    private View view;
    private Context context;

    private Button bt1, bt2, bt3, bt4, btReqJB, btInputJB, btCicilan;
    private String id, idKlien, nama, no_pinj, nm_klien, sisa;
    private int tahap;
    private TextView nopeng, tanggal, klien, email;

    private final static String TAG = "INFO_PENGAJUAN";
    private RequestQueue requestQueue;

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
            view = inflater.inflate(R.layout.fragment_infopeng, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();
        requestQueue = Volley.newRequestQueue(context);
        id = getArguments().getString("id");
        nama = getArguments().getString("nama");
        idKlien = getArguments().getString("id_klien");
        tahap = getArguments().getInt("tahap");
        no_pinj = "";
        nm_klien = "";
        sisa = "0";

        initView();
        getPengajuanDetail();

        Intent intent = new Intent("title");
        intent.putExtra("message", "Detail Pengajuan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (tahap == 1) {
            DetailPeng1Fragment fragment = new DetailPeng1Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("id_klien", idKlien);
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commit();
        } else if (tahap == 2) {
            DetailPeng2Fragment fragment = new DetailPeng2Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("nama", nama);
            bundle.putString("id_klien", idKlien);
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commit();
        } else if (tahap == 3) {
            DetailPeng3Fragment fragment = new DetailPeng3Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("id_klien", idKlien);
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commit();
        } else if (tahap == 4) {
            DetailPeng4Fragment fragment = new DetailPeng4Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("id_klien", idKlien);
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commit();
        } else if (tahap == 5) {
            DetailPeng5Fragment fragment = new DetailPeng5Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("id_klien", idKlien);
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commit();
        } else if (tahap == 6) {
            DetailPeng6Fragment fragment = new DetailPeng6Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("id_klien", idKlien);
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commit();
        }

        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("tahap2"));
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver1,
                new IntentFilter("tahap1"));
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver2,
                new IntentFilter("tahap3"));
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver3,
                new IntentFilter("tahap4"));
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver4,
                new IntentFilter("tahap5"));
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver5, new IntentFilter("req_jb"));
        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver6, new IntentFilter("req_cicilan"));


        initBt();

        return view;
    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {
        nopeng = (TextView) view.findViewById(R.id.nopeng);
        tanggal = (TextView) view.findViewById(R.id.tanggal);
        klien = (TextView) view.findViewById(R.id.klien);
        email = (TextView) view.findViewById(R.id.email);
    }

    private void initBt() {
        bt1 = (Button) view.findViewById(R.id.bt1);
        bt2 = (Button) view.findViewById(R.id.bt2);
        bt3 = (Button) view.findViewById(R.id.bt3);
        bt4 = (Button) view.findViewById(R.id.bt4);
        btReqJB = (Button) view.findViewById(R.id.btReqJb);
        btInputJB = (Button) view.findViewById(R.id.btInputJb);
        btCicilan = (Button) view.findViewById(R.id.btReqCicilan);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), TaskAddActivity.class);
                intent.putExtra("id", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), KlienDetailActivity.class);
                intent.putExtra("id", idKlien);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SmsAddActivity.class);
                intent.putExtra("id", idKlien);
                intent.putExtra("idpeng", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EmailAddActivity.class);
                intent.putExtra("id", idKlien);
                intent.putExtra("idPeng", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btReqJB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ReqJBActivity.class);
                intent.putExtra("id", idKlien);
                intent.putExtra("idPeng", id);
                intent.putExtra("dari", PengajuanDetActivity.dari);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btInputJB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), InputJBActivity.class);
                intent.putExtra("id", idKlien);
                intent.putExtra("idPeng", id);
                intent.putExtra("dari", PengajuanDetActivity.dari);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btCicilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ReqCicilanActivity.class);
                intent.putExtra("id", idKlien);
                intent.putExtra("idPeng", id);
                intent.putExtra("dari", PengajuanDetActivity.dari);
                intent.putExtra("nomor", no_pinj);
                intent.putExtra("nama", nm_klien);
                intent.putExtra("sisa", sisa);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        if (PengajuanDetActivity.dari != null) {

            if (PengajuanDetActivity.dari.equalsIgnoreCase(getString(R.string.janji_bayar))) {
                btReqJB.setVisibility(View.GONE);
                btCicilan.setVisibility(View.GONE);
            } else if (PengajuanDetActivity.dari.equalsIgnoreCase(getString(R.string.cicilan))) {
                btCicilan.setVisibility(View.GONE);
            }
        }

    }

    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = new Bundle();
            bundle.putString("id", intent.getStringExtra("id"));
            bundle.putString("id_klien", intent.getStringExtra("id_klien"));
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailPeng2Fragment fragment = new DetailPeng2Fragment();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    };

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = new Bundle();
            bundle.putString("id", intent.getStringExtra("id"));
            bundle.putString("id_klien", intent.getStringExtra("id_klien"));
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailPeng1Fragment fragment = new DetailPeng1Fragment();
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    };

    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailPeng3Fragment fragment = new DetailPeng3Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", intent.getStringExtra("id"));
            bundle.putString("id_klien", intent.getStringExtra("id_klien"));
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    };

    private BroadcastReceiver mMessageReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailPeng4Fragment fragment = new DetailPeng4Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", intent.getStringExtra("id"));
            bundle.putString("id_klien", intent.getStringExtra("id_klien"));
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    };

    private BroadcastReceiver mMessageReceiver5 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            btReqJB.setVisibility(View.GONE);
        }
    };

    private BroadcastReceiver mMessageReceiver6 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            btCicilan.setVisibility(View.GONE);
        }
    };

    private BroadcastReceiver mMessageReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailPeng5Fragment fragment = new DetailPeng5Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", intent.getStringExtra("id"));
            bundle.putString("id_klien", intent.getStringExtra("id_klien"));
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_peng, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver1);
        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver2);
        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver3);
        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver4);
        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver5);
        LocalBroadcastManager.getInstance(view.getContext()).unregisterReceiver(mMessageReceiver6);
        super.onDestroy();
    }

    private void getPengajuanDetail() {
        Log.d("klien_all", "ok");


        SessionManager sessionManager = new SessionManager(context);
        AndLog.ShowLog("PENGDETT", ";;"+AppConf.URL_PENGAJUAN_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanDetail pengajuanDetail = new Gson().fromJson(jsonObject1.toString(), PengajuanDetail.class);

                    nopeng.setText("No. " + pengajuanDetail.getData().getNomor_pengajuan());
                    tanggal.setText("Tanggal input: " + pengajuanDetail.getData().getCreated_at());
                    klien.setText("Nama klien: " + pengajuanDetail.getData().getNama_lengkap());
                    email.setText("Email klien: " + pengajuanDetail.getData().getEmail());

                    no_pinj = pengajuanDetail.getData().getNomor_pengajuan();
                    nm_klien = pengajuanDetail.getData().getNama_lengkap();
                    sisa = pengajuanDetail.getData().getBase_bill();


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    getActivity().finish();
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
//                    getActivity().finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                AndLog.ShowLog("PENGDETT", params.toString()+";;"+AppConf.URL_PENGAJUAN_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
