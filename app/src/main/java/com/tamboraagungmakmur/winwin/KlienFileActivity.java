package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Adapter.ChecklistAdapter;
import com.tamboraagungmakmur.winwin.Model.Checklist1;
import com.tamboraagungmakmur.winwin.Model.Checklist1Response;
import com.tamboraagungmakmur.winwin.Model.LogoutResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.HttpsTrustManager;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/28/2017.
 */

public class KlienFileActivity extends AppCompatActivity {

    private static final Object TAG = "KLIEN_FILE";
    private RequestQueue requestQueue;

    private ImageButton btBack;
    private Button save_cl;
    private LinearLayout lin1;
    private String img, type, id;
    
    private Context context;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ChecklistAdapter adapter;
    private ArrayList<Checklist1> checklist1ArrayList = new ArrayList<>();

    private HashMap<String, String> checklistArrayList = new HashMap<>();
    private ArrayList<String> idTrue = new ArrayList<>();
    private ArrayList<String> idFalse = new ArrayList<>();
    private String ids, ids1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HttpsTrustManager.allowAllSSL();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klienfile);
        context = KlienFileActivity.this;
        requestQueue = Volley.newRequestQueue(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btBack = (ImageButton) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin1.setVisibility(View.GONE);

        PhotoView image1 = (PhotoView) findViewById(R.id.image1);
        img = getIntent().getStringExtra("img");
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        Glide
            .with(KlienFileActivity.this)
            .load(img)
            .placeholder(R.drawable.placeholder)
            .into(image1);

        initRecycler();

        save_cl = (Button) findViewById(R.id.save); 
        save_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ids = "0";
                for (int i=0; i<idTrue.size(); i++) {
//                    if (i == 0) {
//                        ids += idTrue.get(i);
//                    } else {
                    ids += "," + idTrue.get(i);
//                    }
                }
                ids1 = "0";
                for (int i=0; i<idFalse.size(); i++) {
//                    if (i == 0) {
//                        ids1 += idFalse.get(i);
//                    } else {
                    ids1 += "," + idFalse.get(i);
//                    }
                }

                Log.d("hash_checklist", ids.toString() + " " + ids1.toString());
//                progressBar.setVisibility(View.VISIBLE);
                save_cl.setVisibility(View.INVISIBLE);
                updateCheckl();
            }
        });

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver1,
                new IntentFilter("checklist_pilih"));
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
    
    @Override
    public void onStop() {
        super.onStop();
        requestQueue.cancelAll(TAG);
    }

    private void initRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_checklist);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChecklistAdapter(checklist1ArrayList);
        recyclerView.setAdapter(adapter);

        if (type.compareTo("Kartu Tanda Penduduk") == 0) {
            lin1.setVisibility(View.VISIBLE);
            getKlien("ktp");
        } else if (type.compareTo("Buku Rekening Bank") == 0) {
            lin1.setVisibility(View.VISIBLE);
            getKlien("rek");
        } else if (type.compareTo("Slip Gaji") == 0) {
            lin1.setVisibility(View.VISIBLE);
            getKlien("gaji");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savefile, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.savefile:
//                Picasso
//                        .with(KlienFileActivity.this)
//                        .load(img)
//                        .into(new com.squareup.picasso.Target() {
//                            @Override
//                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                Log.d("picasso", "loaded");
//                                Toast.makeText(KlienFileActivity.this, "Loaded", Toast.LENGTH_SHORT).show();
//                                try {
//                                    String root = Environment.getExternalStoragePublicDirectory(
//                                            Environment.DIRECTORY_PICTURES).getAbsolutePath();
//                                    File myDir = new File(root + "/Winwin");
//
//                                    if (!myDir.exists()) {
//                                        myDir.mkdirs();
//                                    }
//
//                                    String name = new Date().toString() + ".jpg";
//                                    myDir = new File(myDir, name);
//                                    FileOutputStream out = new FileOutputStream(myDir);
//                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//
//                                    out.flush();
//                                    out.close();
//                                } catch(Exception e){
//                                    // some action
//                                }
//                            }
//
//                            @Override
//                            public void onBitmapFailed(Drawable errorDrawable) {
//                                Log.d("picasso", "failed");
//                                Toast.makeText(KlienFileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                                Log.d("picasso", "dowloading");
//                                Toast.makeText(KlienFileActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                Glide
                    .with(KlienFileActivity.this)
                    .load(img)
                     .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                Log.d("glide", "downloading");
                                Toast.makeText(KlienFileActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                Log.d("glide", "failed");
                                Toast.makeText(KlienFileActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Toast.makeText(KlienFileActivity.this, "ok", Toast.LENGTH_SHORT).show();

//                                Log.d("glide", "ok");
                                try {
                                    String root = Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES).getAbsolutePath();
                                    File myDir = new File(root + "/Winwin");

                                    if (!myDir.exists()) {
                                        myDir.mkdirs();
                                    }

                                    String name = new Date().toString() + ".jpg";
                                    myDir = new File(myDir, name);
                                    FileOutputStream out = new FileOutputStream(myDir);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, out);

                                    out.flush();
                                    out.close();
                                    Toast.makeText(KlienFileActivity.this, "image saved at /Pictures/Winwin/" + name, Toast.LENGTH_SHORT).show();
                                } catch(Exception e){
                                    // some action
                                }

                            }

                            @Override
                            public void onLoadCleared(Drawable placeholder) {
                                Log.d("glide", "cleared");
//                                Toast.makeText(KlienFileActivity.this, "cleared", Toast.LENGTH_SHORT).show();
                            }
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getKlien(final String cari) {
        Log.d("pengajuan_detail", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, AppConf.URL_KLIEN_CHECKLIST + "/" + id + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("pengajuan_detail", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    Checklist1Response klienResponse = new Gson().fromJson(jsonObject1.toString(), Checklist1Response.class);

                    if (klienResponse.getData() != null) {

                        for (int i = 0; i < klienResponse.getData().getPribadi().length; i++) {
                            if (klienResponse.getData().getPribadi()[i].getQuestion().toLowerCase().contains(cari)) {
                                checklist1ArrayList.add(klienResponse.getData().getPribadi()[i]);
                                adapter.notifyItemChanged(checklist1ArrayList.size() - 1);
                            }

                        }

                        for (int i = 0; i < klienResponse.getData().getPerusahaan().length; i++) {
                            if (klienResponse.getData().getPerusahaan()[i].getQuestion().toLowerCase().contains(cari)) {
                                checklist1ArrayList.add(klienResponse.getData().getPerusahaan()[i]);
                                adapter.notifyItemChanged(checklist1ArrayList.size() - 1);
                            }

                        }

                        for (int i = 0; i < klienResponse.getData().getTelfon().length; i++) {
                            if (klienResponse.getData().getTelfon()[i].getQuestion().toLowerCase().contains(cari)) {
                                checklist1ArrayList.add(klienResponse.getData().getTelfon()[i]);
                                adapter.notifyItemChanged(checklist1ArrayList.size() - 1);
                            }

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(context);
                    sess.setPage(0);
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
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
//                    startActivity(intent);
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

    private void updateCheckl() {
        Log.d("pengajuan_detail", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.PATCH, AppConf.URL_KLIEN_CHECKLIST2 + "/" + id + "/" + ids + "/" + ids1 + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                progressBar.setVisibility(View.INVISIBLE);
                save_cl.setVisibility(View.VISIBLE);
                Log.d("pengajuan_detail", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    LogoutResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), LogoutResponse.class);

                    Toast.makeText(context, klienResponse.getMessage(), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    SessionManager sess = new SessionManager(context);
                    sess.setPage(0);
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
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
//                    startActivity(intent);
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
