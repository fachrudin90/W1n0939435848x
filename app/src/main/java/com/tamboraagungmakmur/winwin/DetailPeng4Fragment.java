package com.tamboraagungmakmur.winwin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
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
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.google.gson.Gson;
import com.tamboraagungmakmur.winwin.Model.PengajuanDetail;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;
import com.tamboraagungmakmur.winwin.Utils.FormatPrice;
import com.tamboraagungmakmur.winwin.Utils.GlobalToast;
import com.tamboraagungmakmur.winwin.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by innan on 9/12/2017.
 */

public class DetailPeng4Fragment extends Fragment {

    private View view;
    private Context context;

    private Button sudah, batal;
    private TextView text2, text3, text4, text5, text15;
    private TextInputEditText input1a, input3a, input2a;

    private String id, idKlien, idPenc;
    private int duration = 0;

    private final static String TAG = "DETAIL_PENG4";
    private RequestQueue requestQueue;

    private static PengajuanDetail pengdet;
    private String jumlahTransfer;
    private ProgressDialog progressDialog;

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
            view = inflater.inflate(R.layout.fragment_infopeng4, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        context = view.getContext();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(context);

        id = getArguments().getString("id");
        idKlien = getArguments().getString("id_klien");

        Intent intent = new Intent("title");
        intent.putExtra("message", "Detail Pengajuan");
        LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

        jumlahTransfer = "0";
        sudah = (Button) view.findViewById(R.id.sudah);
        batal = (Button) view.findViewById(R.id.batal);
        text2 = (TextView) view.findViewById(R.id.text2);
        text3 = (TextView) view.findViewById(R.id.text3);
        text4 = (TextView) view.findViewById(R.id.text4);
        text5 = (TextView) view.findViewById(R.id.text5);
        text15 = (TextView) view.findViewById(R.id.text15);
        input1a = (TextInputEditText) view.findViewById(R.id.input1a);
        input3a = (TextInputEditText) view.findViewById(R.id.input3a);
        input2a = (TextInputEditText) view.findViewById(R.id.input2a);

        SessionManager sessionManager = new SessionManager(context);
        if (!sessionManager.isPENCAIRAN() && !sessionManager.isPELUNASAN()) {
            sudah.setVisibility(View.INVISIBLE);
            batal.setVisibility(View.INVISIBLE);
        } else {
            sudah.setVisibility(View.VISIBLE);
            batal.setVisibility(View.VISIBLE);
        }

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateandTime = sdf.format(currentTime);

        input1a.setText(currentDateandTime);

        input1a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                input1a.setText(tgl);

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                Calendar calendar = Calendar.getInstance();
                                try {
                                    calendar.setTime(sdf.parse(tgl));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                calendar.add(Calendar.DATE, duration);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                                String output = sdf1.format(calendar.getTime());

                                input3a.setText(output);
                            }
                        });
                cdp.show(getChildFragmentManager(), "tanggal");
            }
        });

        sudah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap5Activity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.putExtra("note", input2a.getText().toString());
                intent.putExtra("date", input1a.getText().toString());
                intent.putExtra("id_penc", idPenc);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.putExtra("jumlah", jumlahTransfer);
                intent.putExtra("nama_bank", text3.getText().toString());
                intent.putExtra("no_rek", text4.getText().toString());
                intent.putExtra("nama_rekening", text5.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

//                sudahTransfer();
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PindahTahap5bActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("id_klien", idKlien);
                intent.putExtra("nopeng", pengdet.getData().getNomor_pengajuan());
                intent.putExtra("nama", pengdet.getData().getNama_lengkap());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        getPengajuanDetail();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("tahap"));

        return view;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getPengajuanDetail();
        }
    };

    public void onStop() {
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
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
                        text15.setVisibility(View.GONE);
                        sudah.setVisibility(View.GONE);
                        batal.setVisibility(View.GONE);
                    }


                    jumlahTransfer = pengajuanDetail.getData().getAmount();
                    text2.setText("Jumlah transfer: " + new FormatPrice().format(pengajuanDetail.getData().getAmount()));
                    text3.setText("Nama Bank: " + pengajuanDetail.getData().getNama_bank());
                    text4.setText("Nomor rekening: " + pengajuanDetail.getData().getNomor_rekening());
                    text5.setText("Nama rekening: " + pengajuanDetail.getData().getNama_rekening());

                    input3a.setText(pengajuanDetail.getData().getDue_date());
                    duration = Integer.parseInt(pengajuanDetail.getData().getDuration());

                    idPenc = pengajuanDetail.getData().getPencairan_id();

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

    private void sudahTransfer() {
        Log.d("transfer_data", "ok");

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConf.URL_GET_API_BCA_TRF, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("transfer_data", response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);

                    if (jsonObject1.has("Status")) {

                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                    } else if (jsonObject1.has("ErrorMessage")) {

                        JSONObject a = new JSONObject(jsonObject1.getString("ErrorMessage"));
                        Toast.makeText(context, a.getString("Indonesian"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "timeout", Toast.LENGTH_SHORT).show();
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
                }
//                } else {
//                    GlobalToast.ShowToast(context, getString(R.string.session_expired));
////                AndLog.ShowLog(getContext().getSimpleName(), error.toString());
//                    SessionManager sessionManager = new SessionManager(context);
//                    sessionManager.logoutUser(); sessionManager.setPage(0);
//
//                    Intent intent = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent);
//                    getActivity().finish();
//                }

            }

        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("_session", sessionManager.getSessionId());
                params.put("user", "apibca");
                params.put("pass", "tr4nsf3rbc4.");
                params.put("jumlah", jumlahTransfer);
                params.put("nama_bank", text3.getText().toString());
                params.put("no_rek", text4.getText().toString());
                params.put("nama_rekening", text5.getText().toString());
                return params;
            }
        };

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
//        VolleyHttp.getInstance(context).addToRequestQueue(stringRequest);

    }


}
