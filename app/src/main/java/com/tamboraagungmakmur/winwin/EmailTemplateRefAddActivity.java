package com.tamboraagungmakmur.winwin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.tamboraagungmakmur.winwin.Model.TaskStoreResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by innan on 11/10/2017.
 */

public class EmailTemplateRefAddActivity extends FragmentActivity{

    private static final String TAG = "EMAIL_TEMPLATE_ADD";
    private TextInputEditText subjek, message;
    private Button save;
    private ProgressBar progressBar;

    private RequestQueue requestQueue;
    private Context context;

    private RichEditor isi;
    private Button bold, italic, h1, h2, h3, h4, h5, red, green, blue, yellow, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_template_ref_add);
        context = EmailTemplateRefAddActivity.this;
        requestQueue = Volley.newRequestQueue(context);
        initView();
    }

    private void initView() {
        subjek = (TextInputEditText) findViewById(R.id.subjek);
        message = (TextInputEditText) findViewById(R.id.message);
        save = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                addTemplate();
            }
        });

        isi = (RichEditor) findViewById(R.id.isi);
        bold = (Button) findViewById(R.id.bold);
        italic = (Button) findViewById(R.id.italic);
        h1 = (Button) findViewById(R.id.h1);
        h2 = (Button) findViewById(R.id.h2);
        h3 = (Button) findViewById(R.id.h3);
        h4 = (Button) findViewById(R.id.h4);
        h5 = (Button) findViewById(R.id.h5);
        red = (Button) findViewById(R.id.red);
        green = (Button) findViewById(R.id.green);
        blue = (Button) findViewById(R.id.blue);
        yellow = (Button) findViewById(R.id.yellow);
        link = (Button) findViewById(R.id.link);

        isi.setEditorHeight(200);
        isi.setPadding(10, 10, 10, 10);
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

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("link"));
    }

    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String link = intent.getStringExtra("link");
            String alias = intent.getStringExtra("alias");

            isi.insertLink(link, alias);

        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void addTemplate() {
        Log.d("klien_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_EMAIL_TEMPLATE_STORE + "?_session=" + sessionManager.getSessionId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("klien_all", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    TaskStoreResponse klienResponse = new Gson().fromJson(jsonObject1.toString(), TaskStoreResponse.class);

                    if (!klienResponse.isError()) {
                        Toast.makeText(context, "Template telah ditambahkan", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent("email_template");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                        onBackPressed();
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(context, getString(R.string.session_expired));

                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.logoutUser(); sessionManager.setPage(0);

                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
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
                params.put("emailtpt_name", subjek.getText().toString());
                params.put("emailtpt_content", isi.getHtml());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
