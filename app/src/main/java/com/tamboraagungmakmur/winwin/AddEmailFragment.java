package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.kyleduo.switchbutton.SwitchButton;
import com.tamboraagungmakmur.winwin.Model.EmailTemplate;
import com.tamboraagungmakmur.winwin.Model.EmailTemplateResponse;
import com.tamboraagungmakmur.winwin.Model.Klien;
import com.tamboraagungmakmur.winwin.Model.KlienResponse;
import com.tamboraagungmakmur.winwin.Model.LoginResponse;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Model.TaskResponse;
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
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

import butterknife.ButterKnife;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by innan on 8/22/2017.
 */

public class AddEmailFragment extends Fragment {

    private final static String TAG = "ADD_EMAIL";
    private RequestQueue requestQueue;

    private View view;
    private Context context;

    private TextInputEditText klien;
    private TextInputEditText subjek, isi1;
    private Button submit;
    private ProgressBar progressBar;
    private Switch swAllKlien;
    private Switch swAllKlien1;

    private ArrayList<Klien> klienArrayList = new ArrayList<>();
    private List<String> klienArrayList1 = new ArrayList<>();
    private List<Integer> klienArrayList2 = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private int pos = 0;
    private String idPengajuan;

    private SessionManager sessionManager;
    private String sessionId;

    private int offset;
    private static final int LIMIT = 20;

    private List<CharSequence> emailTemplateArrayList = new ArrayList<>();
    private ArrayList<EmailTemplate> emailTemplates = new ArrayList<>();
    private ArrayAdapter<CharSequence> adapterSpinner;

    private RichEditor isi;
    private Button bold, italic, h1, h2, h3, h4, h5, red, green, blue, yellow, link;

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
            view = inflater.inflate(R.layout.fragment_add_email, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();

        requestQueue = Volley.newRequestQueue(context);

        sessionManager = new SessionManager(context);
        sessionId = sessionManager.getSessionId();

        subjek = (TextInputEditText) view.findViewById(R.id.emailsubjek);
        isi1 = (TextInputEditText) view.findViewById(R.id.emailisi);
        submit = (Button) view.findViewById(R.id.submit);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        swAllKlien = (Switch) view.findViewById(R.id.swAllKlien);
        swAllKlien1 = (Switch) view.findViewById(R.id.swAllKlien1);

        swAllKlien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swAllKlien1.setChecked(false);
                }
            }
        });

        swAllKlien1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swAllKlien.setChecked(false);
                }
            }
        });

        klien = (TextInputEditText) view.findViewById(R.id.klien);

        klien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListKlienActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                if (swAllKlien.isChecked()) {
                    getKlien(subjek.getText().toString(), isi.getHtml());
                } else if (swAllKlien1.isChecked()) {
                    getKlien1(subjek.getText().toString(), isi.getHtml());
                } else {
                    addEmail(subjek.getText().toString(), isi.getHtml());
                }
            }
        });

        emailTemplateArrayList.add("-");

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
        adapterSpinner = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, emailTemplateArrayList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (emailTemplates.size() > 1) {
                    isi.setHtml(emailTemplates.get(i - 1).getKonten());
                }
                if (i == 0) {
                    isi.setHtml("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getTemplate();

        isi = (RichEditor) view.findViewById(R.id.isi);
        bold = (Button) view.findViewById(R.id.bold);
        italic = (Button) view.findViewById(R.id.italic);
        h1 = (Button) view.findViewById(R.id.h1);
        h2 = (Button) view.findViewById(R.id.h2);
        h3 = (Button) view.findViewById(R.id.h3);
        h4 = (Button) view.findViewById(R.id.h4);
        h5 = (Button) view.findViewById(R.id.h5);
        red = (Button) view.findViewById(R.id.red);
        green = (Button) view.findViewById(R.id.green);
        blue = (Button) view.findViewById(R.id.blue);
        yellow = (Button) view.findViewById(R.id.yellow);
        link = (Button) view.findViewById(R.id.link);

        isi.setEditorHeight(200);
        isi.setPadding(20, 20, 20, 20);
        isi.setPlaceholder("Isi Template");

        isi.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                isi.setFocusable(true);
            }
        });

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isi.setBold();
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isi.setItalic();
            }
        });

        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isi.setHeading(1);
            }
        });

        h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isi.setHeading(2);
            }
        });

        h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isi.setHeading(3);
            }
        });

        h4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isi.setHeading(4);
            }
        });

        h5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isi.setHeading(5);
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View view) {
                isi.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View view) {
                isi.setTextColor(isChanged ? Color.BLACK : Color.GREEN);
                isChanged = !isChanged;
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View view) {
                isi.setTextColor(isChanged ? Color.BLACK : Color.BLUE);
                isChanged = !isChanged;
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override
            public void onClick(View view) {
                isi.setTextColor(isChanged ? Color.BLACK : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EmailTempLinkActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver1,
                new IntentFilter("link"));

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("klien_terkait"));

        return view;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            idPengajuan = intent.getStringExtra("id");
            klien.setText(intent.getStringExtra("nomor_pelanggan") + " " + intent.getStringExtra("nama_lengkap") + " [" + intent.getStringExtra("email") + "]");
        }
    };

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String link = intent.getStringExtra("link");
            String alias = intent.getStringExtra("alias");

            isi.insertLink(link, alias);

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver1);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {
    }

    private void addEmail(final String subjek, final String isi) {
        Log.d("klien_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_KOMUNIKASI_EMAIL + "/" + idPengajuan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_find", response);
                progressBar.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse response1 = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (response1.isError())
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    }
                    Fragment frag = new KomunikasiEmail();

                    FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.content_frame, frag);
                    ft.addToBackStack(null);
                    ft.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

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
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
//
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
                params.put("subject", subjek);
                params.put("message", isi);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void getKlien(final String subjek, final String isi) {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KLIEN_ALL + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KlienResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KlienResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//
                            addEmail1(klienResponse.getData()[i].getId(), subjek, isi);
                        }

                        offset += 1;
                        getKlien(subjek, isi);

                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        submit.setVisibility(View.VISIBLE);

                        Fragment frag = new KomunikasiEmail();

                        FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.content_frame, frag);
                        ft.addToBackStack(null);
                        ft.commit();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

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
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
//
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

    private void getKlien1(final String subjek, final String isi) {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_KLIEN_ALL + "/" + LIMIT + "/" + offset + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    KlienResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), KlienResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
                            if (klienResponse.getData()[i].getStatus().compareTo("Klien Aktif") == 0) {
                                addEmail1(klienResponse.getData()[i].getId(), subjek, isi);
                            }
                        }

                        offset += 1;
                        getKlien1(subjek, isi);

                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        submit.setVisibility(View.VISIBLE);

                        Fragment frag = new KomunikasiEmail();

                        FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.replace(R.id.content_frame, frag);
                        ft.addToBackStack(null);
                        ft.commit();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

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
                    if (error instanceof TimeoutError) {
                        Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(context, "no connection", Toast.LENGTH_SHORT).show();
                    } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                        SessionManager sessionManager = new SessionManager(context);
                        sessionManager.errorHandling(error);
//                        sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                        GlobalToast.ShowToast(context, getString(R.string.session_expired));
//
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        context.startActivity(intent);
//                        getActivity().finish();
                    }
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

    private void addEmail1(final String idKlien, final String subjek, final String isi) {
        Log.d("klien_all", "ok");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_KOMUNIKASI_EMAIL + "/" + idKlien, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_find", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse response1 = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

//                    if (response1.isError())
//                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//                    else {
//                        Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

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
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
//
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
                params.put("subject", subjek);
                params.put("message", isi);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void getTemplate() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_EMAIL_TEMPLATE + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    EmailTemplateResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), EmailTemplateResponse.class);

//                    JSONArray jsonArray = new JSONArray(klienResponse.getData().toString());
                    if (klienResponse.getData().length != 0) {

                        for (int i = 0; i < klienResponse.getData().length; i++) {
//
                            emailTemplateArrayList.add(klienResponse.getData()[i].getLabel());
                            emailTemplates.add(klienResponse.getData()[i]);
                        }
                        adapterSpinner.notifyDataSetChanged();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    getActivity().finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                SessionManager sessionManager = new SessionManager(context);
                sessionManager.errorHandling(error);
//                sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                GlobalToast.ShowToast(context, getString(R.string.session_expired));
//
//                Intent intent = new Intent(context, LoginActivity.class);
//                context.startActivity(intent);
//                getActivity().finish();

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
