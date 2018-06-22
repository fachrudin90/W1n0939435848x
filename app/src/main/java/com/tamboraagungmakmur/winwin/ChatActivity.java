package com.tamboraagungmakmur.winwin;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.tamboraagungmakmur.winwin.Adapter.ChatAdapter;
import com.tamboraagungmakmur.winwin.Model.Chat;
import com.tamboraagungmakmur.winwin.Model.ChatResponse;
import com.tamboraagungmakmur.winwin.Model.ChatSendResponse;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 10/21/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private static final Object TAG = "CHAT";
    private RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ChatAdapter adapter;
    private ArrayList<Chat> chatArrayList = new ArrayList<>();

    private TextView title;
    private EditText pesan;
    private ImageButton send;
    private ImageButton btBack;

    private LinearLayout lin1;

    private Context context;

    private int offset = 0;
    private String idKlien, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = ChatActivity.this;

        idKlien = getIntent().getStringExtra("id");
        nama = getIntent().getStringExtra("nama");

        requestQueue = Volley.newRequestQueue(context);
        initView();

        getChat();

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(1);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("chat"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.cancel(1);
            String iduser = intent.getStringExtra("id_user");
            if (iduser.compareTo(idKlien) == 0) {
                if (chatArrayList != null) {
                    chatArrayList.clear();
                } else {
                    chatArrayList = new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
                offset = 0;
                getChat();
            }
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        requestQueue.cancelAll(TAG);
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        title.setText(nama);

        btBack = (ImageButton) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_chat);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(chatArrayList);
        recyclerView.setAdapter(adapter);

        pesan = (EditText) findViewById(R.id.pesan);
        send = (ImageButton) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat(pesan.getText().toString());
                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                pesan.setText("");
            }
        });

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = lin1.getRootView().getHeight() - lin1.getHeight();
                if (heightDiff > dpToPx(ChatActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    recyclerView.scrollToPosition(0);
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void getChat() {
        Log.d("chat_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_USER_GETCHAT , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("chat_all", response);
                Log.d("chat_id_klien", idKlien);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    ChatResponse chatResponse = new Gson().fromJson(jsonObject1.toString(), ChatResponse.class);

                    if (chatResponse.getData().length > 0) {
                        for (int i=0; i<chatResponse.getData().length; i++) {
                            chatArrayList.add(chatResponse.getData()[i]);
                            adapter.notifyItemChanged(chatArrayList.size()-1);
                        }
                        offset += 1;
                        recyclerView.scrollToPosition(0);
                        getChat();
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
                params.put("page", "" + offset);
                params.put("id_user", idKlien);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void sendChat(final String pesan) {
        Log.d("chat_all", "ok");

        SessionManager sessionManager = new SessionManager(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_USER_SENDCHAT , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("chat_all", response);
                Log.d("chat_id_klien", idKlien);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    ChatSendResponse chatResponse = new Gson().fromJson(jsonObject1.toString(), ChatSendResponse.class);

                    if (!chatResponse.isError()) {
                        if (chatArrayList != null) {
                            chatArrayList.clear();
                        } else {
                            chatArrayList = new ArrayList<>();
                        }
                        adapter.notifyDataSetChanged();
                        offset = 0;
                        getChat();
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
                params.put("id_user", idKlien);
                params.put("user", "admin");
                params.put("message", pesan);
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }

}
