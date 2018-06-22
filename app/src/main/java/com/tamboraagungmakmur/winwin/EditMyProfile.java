package com.tamboraagungmakmur.winwin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.tamboraagungmakmur.winwin.Model.UserResponse1;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditMyProfile extends AppCompatActivity {

    @Bind(R.id.btBack)
    ImageButton btBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.txNama)
    EditText txNama;
    @Bind(R.id.txUsername)
    EditText txUsername;
    @Bind(R.id.txHakAkses)
    EditText txHakAkses;
    @Bind(R.id.btSave)
    Button btSave;
    @Bind(R.id.progressbar)
    ProgressBar progressBar;
    @Bind(R.id.gantipass)
    Button gantiPass;
    @Bind(R.id.gantipass1)
    Button gantiPass1;

    private final static String TAG = "EDIT_MYPROFILE";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);
        ButterKnife.bind(this);

        requestQueue = Volley.newRequestQueue(this);

        SessionManager sessionManager = new SessionManager(this);
        txUsername.setText(sessionManager.getUsername());

        getUser();

        gantiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditMyProfile.this, GantiPassActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        gantiPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditMyProfile.this, GantiPass1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    @OnClick({R.id.btBack, R.id.btSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btSave:
                break;
        }
    }

    private void getUser() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(EditMyProfile.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConf.URL_USER_INFO + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    UserResponse1 klienResponse = new Gson().fromJson(jsonObject1.toString(), UserResponse1.class);

                    txNama.setText(klienResponse.getNama_karyawan());

                    String roles = "";
                    for (int i=0; i<klienResponse.getRoles().length; i++) {
                        if (i == 0) {
                            roles += klienResponse.getRoles()[i].getRole_label();
                        } else {
                            roles += ", " + klienResponse.getRoles()[i].getRole_label();
                        }
                    }
                    txHakAkses.setText(roles);

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(EditMyProfile.this, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(EditMyProfile.this);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(EditMyProfile.this, LoginActivity.class);
                    EditMyProfile.this.startActivity(intent);
                    EditMyProfile.this.finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(EditMyProfile.this, "timeout", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(EditMyProfile.this, "no connection", Toast.LENGTH_SHORT).show();
                } else {
//                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
                    SessionManager sessionManager = new SessionManager(EditMyProfile.this);
                    sessionManager.errorHandling(error);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    GlobalToast.ShowToast(EditMyProfile.this, getString(R.string.session_expired));
//                    Intent intent = new Intent(EditMyProfile.this, LoginActivity.class);
//                    EditMyProfile.this.startActivity(intent);
//                    EditMyProfile.this.finish();
                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(EditMyProfile.this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(EditMyProfile.this).addToRequestQueue(stringRequest);

    }
}
