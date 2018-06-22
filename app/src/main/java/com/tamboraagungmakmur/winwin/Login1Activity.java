package com.tamboraagungmakmur.winwin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tamboraagungmakmur.winwin.Model.LoginResponse;
import com.tamboraagungmakmur.winwin.Utils.AndLog;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;
import com.tamboraagungmakmur.winwin.Utils.UnsafeOkHttpClient;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by innan on 9/19/2017.
 */

public class Login1Activity extends AppCompatActivity {

    @Bind(R.id.imgLogo)
    ImageView imgLogo;
    @Bind(R.id.etUsername)
    EditText etUsername;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btLogin)
    Button btLogin;

    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    private Call<JsonObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(Login1Activity.this);

        progressDialog = new ProgressDialog(Login1Activity.this);
        progressDialog.setMessage(getString(R.string.app_name));
        progressDialog.setCancelable(false);


        if (sessionManager.checkLogin()) {
            Goto();
        }

    }

    @Override
    public void onStop(){
        if (call != null) {
            call.cancel();
        }
        super.onStop();
    }

    @OnClick(R.id.btLogin)
    public void onClick() {


        if (!etUsername.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
//            LoginProses();

            progressDialog.show();
            sendFcm1();
        }
    }


    private void LoginProses() {


        progressDialog.show();

        Log.d("url_login", AppConf.URL_LOGIN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Innani
                Log.d("login", response.toString());
                try {
                    JSONObject res = new JSONObject(response.toString());
                    LoginResponse loginResponse = new Gson().fromJson(res.toString(), LoginResponse.class);

                    if (!loginResponse.isError()) {
                        Toast.makeText(Login1Activity.this, loginResponse.getSession_expire(), Toast.LENGTH_SHORT).show();
                        String idadmin = loginResponse.getMember_id();
                        String sessionId = loginResponse.getSession_id();
                        String username = etUsername.getText().toString().trim();
                        sessionManager.createLoginSession(idadmin, username, sessionId);
                        Goto();
                    } else {
                        GlobalToast.ShowToast(Login1Activity.this, getString(R.string.loginerror));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalToast.ShowToast(Login1Activity.this, getString(R.string.disconnected));
                }


                //Rudi
//                try {
//
//                    JSONObject jo = new JSONObject(response);
//                    String hasil = jo.getString("result");
//
//
//                    if (hasil.toString().trim().equals("true")) {
//
//
//                        String idadmin = jo.getString("member_id");
//                        String username = jo.getString("member_user");
//
//
//                        sessionManager.createLoginSession(idadmin, username);
//                        Goto();
//
//
//                    } else {
//
//                        GlobalToast.ShowToast(LoginActivity.this, getString(R.string.loginerror));
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//                    GlobalToast.ShowToast(LoginActivity.this, getString(R.string.disconnected));
//
//                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                AndLog.ShowLog(Login1Activity.class.getSimpleName(), error.toString());
                progressDialog.dismiss();

            }

        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("username", etUsername.getText().toString().trim());
                params.put("password", etPassword.getText().toString().trim());
//                params.put("device", GetDeviceInfo.getInfo(LoginActivity.this));
                return params;
            }
        };

        stringRequest.setTag(AppConf.httpTag);
        VolleyHttp.getInstance(Login1Activity.this).addToRequestQueue(stringRequest);

    }

    public void Goto() {

        Intent i = new Intent(Login1Activity.this, HomeActivity.class);
        startActivity(i);
        finish();

    }

    public static class ServiceGenerator {

        public static final String API_BASE_URL = "http://192.168.1.74/";

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
        @FormUrlEncoded
        @POST("/winwin/api/session/masuk")
        Call<JsonObject> postRawJson(@Header("Content-Type") String content,
                                     @Field(value = "username") String username,
                                     @Field(value = "password") String password);
    }

    private void sendFcm1() {

        NotifReq service =
                ServiceGenerator.createService(NotifReq.class);

        call = service.postRawJson("application/x-www-form-urlencoded", etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                if (response != null) {
                    Log.d("response_fcm", response.body().toString());
                    try {
                        JSONObject res = new JSONObject(response.body().toString());
                        LoginResponse loginResponse = new Gson().fromJson(res.toString(), LoginResponse.class);

                        if (!loginResponse.isError()) {
                            Toast.makeText(Login1Activity.this, loginResponse.getSession_expire(), Toast.LENGTH_SHORT).show();
                            String idadmin = loginResponse.getMember_id();
                            String sessionId = loginResponse.getSession_id();
                            String username = etUsername.getText().toString().trim();
                            sessionManager.createLoginSession(idadmin, username, sessionId);
                            Goto();
                            progressDialog.dismiss();
                        } else {
                            GlobalToast.ShowToast(Login1Activity.this, getString(R.string.loginerror));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        GlobalToast.ShowToast(Login1Activity.this, getString(R.string.disconnected));
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("error fcm", t.getMessage());
            }
        });

    }

}