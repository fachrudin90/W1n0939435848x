package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.ChecklistAdapter;
import com.tamboraagungmakmur.winwin.Model.Checklist1;
import com.tamboraagungmakmur.winwin.Model.Checklist1Response;
import com.tamboraagungmakmur.winwin.Model.Checklists;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Model.PengajuanDetail;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.UnsafeOkHttpClient;
import com.tamboraagungmakmur.winwin.voip.VoiceCallBridge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.PATCH;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by innan on 9/11/2017.
 */

public class DetailPeng2aFragment extends Fragment {

    private View view;
    private Context context;

    private ArrayList<Checklist1> checklists = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ChecklistAdapter adapter;

    private ArrayList<Checklist1> checklists1 = new ArrayList<>();
    private RecyclerView recyclerView1;
    private LinearLayoutManager linearLayoutManager1;
    private ChecklistAdapter adapter1;

    private ArrayList<Checklist1> checklists2 = new ArrayList<>();
    private RecyclerView recyclerView2;
    private LinearLayoutManager linearLayoutManager2;
    private ChecklistAdapter adapter2;

    private ArrayList<Checklist1> checklists3 = new ArrayList<>();
    private RecyclerView recyclerView3;
    private LinearLayoutManager linearLayoutManager3;
    private ChecklistAdapter adapter3;

    private FragmentActivity mActivity;

    private TextView menu1, menu2, text2;
    private Button prev, tolak, setuju, save_cl;
    private TextInputEditText input1a;
    private ProgressBar progressBar;

    private String id, idKlien, nama;

    private final static String TAG = "DETAIL_PENG2A";
    private RequestQueue requestQueue;

    private HashMap<String, String> checklistArrayList = new HashMap<>();
    private ArrayList<String> idTrue = new ArrayList<>();
    private ArrayList<String> idFalse = new ArrayList<>();
    private String ids, ids1;

    private static PengajuanDetail pengdet;

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
            view = inflater.inflate(R.layout.fragment_infopeng2a, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();

        requestQueue = Volley.newRequestQueue(context);

        id = getArguments().getString("id");
        nama = getArguments().getString("nama");
        idKlien = getArguments().getString("id_klien");
        Log.d("id_pengajuan2", id);

        getPengajuanDetail();

        Intent intent = new Intent("title");
        intent.putExtra("message", "Detail Pengajuan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        input1a = (TextInputEditText) view.findViewById(R.id.input1a);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        KlienDetailFragment fragment = new KlienDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString("id", idKlien);

        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_peng2a, fragment);
        fragmentTransaction.commitAllowingStateLoss();


//        recyclerView = (RecyclerView) view.findViewById(R.id.rv1);
//        linearLayoutManager = new LinearLayoutManager(view.getContext());
//        adapter = new ChecklistAdapter(checklists);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(adapter);

        recyclerView1 = (RecyclerView) view.findViewById(R.id.rv2);
        linearLayoutManager1 = new LinearLayoutManager(view.getContext());
        adapter1 = new ChecklistAdapter(checklists1);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        recyclerView1.setAdapter(adapter1);

        recyclerView2 = (RecyclerView) view.findViewById(R.id.rv3);
        linearLayoutManager2 = new LinearLayoutManager(view.getContext());
        adapter2 = new ChecklistAdapter(checklists2);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView2.setAdapter(adapter2);

        recyclerView3 = (RecyclerView) view.findViewById(R.id.rv4);
        linearLayoutManager3 = new LinearLayoutManager(view.getContext());
        adapter3 = new ChecklistAdapter(checklists3);
        recyclerView3.setLayoutManager(linearLayoutManager3);
        recyclerView3.setAdapter(adapter3);

//        initFab();

        menu2 = (TextView) view.findViewById(R.id.menu2);
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DetailPeng2bFragment fragment = new DetailPeng2bFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("id_klien", idKlien);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_peng2, fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        prev = (Button) view.findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap1Activity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tolak = (Button) view.findViewById(R.id.tolak);
        setuju = (Button) view.findViewById(R.id.setuju);
        save_cl = (Button) view.findViewById(R.id.save_cl);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        text2 = (TextView) view.findViewById(R.id.text2);
        tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap3Activity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.putExtra("note", input1a.getText().toString());
                intent.putExtra("rekomendasi", "tolak");
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        setuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap3Activity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.putExtra("note", input1a.getText().toString());
                intent.putExtra("rekomendasi", "setujui");
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        save_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ids = "0";
                for (int i = 0; i < idTrue.size(); i++) {
//                    if (i == 0) {
//                        ids += idTrue.get(i);
//                    } else {
                    ids += "," + idTrue.get(i);
//                    }
                }
                ids1 = "0";
                for (int i = 0; i < idFalse.size(); i++) {
//                    if (i == 0) {
//                        ids1 += idFalse.get(i);
//                    } else {
                    ids1 += "," + idFalse.get(i);
//                    }
                }

                Log.d("hash_checklist", ids.toString() + " " + ids1.toString());
                progressBar.setVisibility(View.VISIBLE);
                save_cl.setVisibility(View.INVISIBLE);
                updateCheckl();
            }
        });

        mActivity = getActivity();
        getKlien();

        LocalBroadcastManager.getInstance(view.getContext()).registerReceiver(mMessageReceiver1,
                new IntentFilter("checklist_pilih"));

        return view;
    }

    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String idChecklist = intent.getStringExtra("id");
            String answer = intent.getStringExtra("answer");

            if (checklistArrayList.containsKey(idChecklist)) {
                checklistArrayList.put(idChecklist, answer);
            } else {
                checklistArrayList.put(idChecklist, answer);
            }

            Log.d("hash_checklist", checklistArrayList.toString());

            if (answer.compareTo("true") == 0) {
                idTrue.add(idChecklist);
                idFalse.remove(idChecklist);
            } else {
                idTrue.remove(idChecklist);
                idFalse.add(idChecklist);
            }


        }
    };

    private void getKlien() {
        Log.d("pengajuan_detail", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KLIEN_CHECKLIST + "/" + idKlien + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan_detail", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    Checklist1Response klienResponse = new Gson().fromJson(jsonObject1.toString(), Checklist1Response.class);

                    if (klienResponse.getData() != null) {

//                        for (int i = 0; i < klienResponse.getData().getPribadi().length; i++) {
//                            checklists.add(klienResponse.getData().getPribadi()[i]);
//                            adapter.notifyItemChanged(checklists.size() - 1);
//                        }

                        for (int i = 0; i < klienResponse.getData().getPerusahaan().length; i++) {
                            checklists1.add(klienResponse.getData().getPerusahaan()[i]);
                            adapter1.notifyItemChanged(checklists1.size() - 1);
                        }

                        for (int i = 0; i < klienResponse.getData().getTelfon().length; i++) {
                            checklists2.add(klienResponse.getData().getTelfon()[i]);
                            adapter2.notifyItemChanged(checklists2.size() - 1);
                        }

                        for (int i = 0; i < klienResponse.getData().getSurvei().length; i++) {
                            checklists3.add(klienResponse.getData().getSurvei()[i]);
                            adapter3.notifyItemChanged(checklists3.size() - 1);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
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
                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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

    private void updateCheckl() {
        Log.d("pengajuan_detail", "ok");

        SessionManager sessionManager = new SessionManager(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, AppConf.URL_KLIEN_CHECKLIST2 + "/" + idKlien + "/" + ids + "/" + ids1 + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                save_cl.setVisibility(View.VISIBLE);
                Log.d("pengajuan_detail", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    Toast.makeText(context, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(mActivity);
                    sess.setPage(0);
                    GlobalToast.ShowToast(mActivity, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.logoutUser();
                    sessionManager.setPage(0);

                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
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
                    SessionManager sessionManager = new SessionManager(mActivity);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser();
//                    sessionManager.setPage(0);
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

    private void initFab() {

        final FloatingActionMenu materialDesignFAM = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        FloatingActionButton floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item1);
        FloatingActionButton floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item2);

        materialDesignFAM.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {

                } else {

                }
            }
        });

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Toast.makeText(mActivity, idKlien, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), VoiceCallBridge.class);
                i.putExtra("iduser", idKlien);
                i.putExtra("nama", nama);
                i.putExtra("foto", "");
                startActivity(i);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    private void getPengajuanDetail() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_PENGAJUAN_DETAIL + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    PengajuanDetail pengajuanDetail = new Gson().fromJson(jsonObject1.toString(), PengajuanDetail.class);
                    pengdet = pengajuanDetail;

                    if (pengajuanDetail.getData().is_batal()) {
                        tolak.setVisibility(View.GONE);
                        setuju.setVisibility(View.GONE);
                        prev.setVisibility(View.GONE);
                        text2.setVisibility(View.GONE);
                        save_cl.setVisibility(View.GONE);
                    }


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
                return params;
            }

        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    public static class ServiceGenerator1 {

        public static final String API_BASE_URL = AppConf.BASE_URL;

        static OkHttpClient.Builder okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        public static <S> S createService(Class<S> serviceClass) {
            Retrofit retrofit = builder.client(okHttpClient.build()).build();
            return retrofit.create(serviceClass);
        }
    }

    interface NotifReq {

        @PATCH
        Call<LogoutResponse> postRawJson(@Url String url,
                                         @Query("_session") String session,
                                         @Query("checklist") HashMap checklist);
    }

    private void updateChecklist() {

        NotifReq service =
                ServiceGenerator1.createService(NotifReq.class);

        SessionManager sessionManager = new SessionManager(context);

        Checklists notifRequest = new Checklists();
//        notifRequest.set_session(sessionManager.getSessionId());
//        Checklist2 checklist2 = new Checklist2();
//        checklist2.setId("1");
//        checklist2.setVal("true");
//        ArrayList<Checklist2> checklist2ArrayList = new ArrayList<>();
//        checklist2ArrayList.add(checklist2);
//        Checklist2[] checklist2s = checklist2ArrayList.toArray(new Checklist2[checklist2ArrayList.size()]);
//        notifRequest.setChecklist(checklist2s);


//        String jsonReq = new Gson().toJson(notifRequest, Checklists.class);
//        Log.d("req fcm", jsonReq);
//        JsonElement jsonReq1 = new Gson().fromJson(jsonReq, JsonElement.class);
//        JsonObject jsonReq2 = jsonReq1.getAsJsonObject();


//        Call<LogoutResponse> call = service.postRawJson("https://hq.ppgwinwin.com:445/klien/checklist/"+idKlien, sessionManager.getSessionId(), checklistArrayList);
        Call<LogoutResponse> call = service.postRawJson(AppConf.URL_KLIEN_CHECKLIST + "/" + idKlien, sessionManager.getSessionId(), checklistArrayList);
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, retrofit2.Response<LogoutResponse> response) {
                if (response != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    save_cl.setVisibility(View.VISIBLE);

                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    Log.d("response_fcm", response.toString());
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Log.d("error_fcm", t.getMessage());
            }
        });

    }
}
