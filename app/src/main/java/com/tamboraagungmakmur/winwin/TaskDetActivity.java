package com.tamboraagungmakmur.winwin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tamboraagungmakmur.winwin.Utils.AppConf;
import com.tamboraagungmakmur.winwin.Utils.VolleyHttp;

/**
 * Created by innan on 8/28/2017.
 */

public class TaskDetActivity extends Activity {

    private final static String TAG = "TASK_DET";
    private RequestQueue requestQueue;

    private TextView txNoPeng, txNoPel, txNamaKlien, txTanggalPeng, txTanggalJatuhTempo, txTugas, txDeadline, txStatus, txCatatan;
    private Button finish, cancel;

    private String id, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdet);
        requestQueue = Volley.newRequestQueue(this);
        initView();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("task_update"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            onBackPressed();
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onStop(){
        requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void initView() {

        txNoPeng = (TextView) findViewById(R.id.txNoPeng);
        txNoPel = (TextView) findViewById(R.id.txNopel);
        txNamaKlien = (TextView) findViewById(R.id.txNamaKlien);
        txTanggalPeng = (TextView) findViewById(R.id.txTanggalPeng);
        txTanggalJatuhTempo = (TextView) findViewById(R.id.txTanggalJatuhTempo);
        txTugas = (TextView) findViewById(R.id.txTugas);
        txDeadline = (TextView) findViewById(R.id.txDeadline);
        txStatus = (TextView) findViewById(R.id.txStatus);
        txCatatan = (TextView) findViewById(R.id.txCatatan);
        finish = (Button) findViewById(R.id.finish);
        cancel = (Button) findViewById(R.id.cancel);

        txNoPeng.setText("Nomor Pengajuan: " + getIntent().getStringExtra("nomor_pengajuan"));
        txNoPel.setText("Nomor Pelanggan: " + getIntent().getStringExtra("nomor_pelanggan"));
        txNamaKlien.setText("Nama Pelanggan: " + getIntent().getStringExtra("nama_lengkap"));
        txTanggalPeng.setText("Tanggal Pengajuan: " + getIntent().getStringExtra("tanggal_pengajuan"));
        txTanggalJatuhTempo.setText("Tanggal Jatuh Tempo: " + getIntent().getStringExtra("jatuh_tempo"));
        txTugas.setText(getIntent().getStringExtra("task"));
        txDeadline.setText("Deadline: " + getIntent().getStringExtra("deadline"));
        txStatus.setText("Status: " + getIntent().getStringExtra("status"));
        txCatatan.setText("Catatan Pelaksana: \n" + getIntent().getStringExtra("note"));

        boolean finished = getIntent().getBooleanExtra("finished", false);
        if (finished) {
            finish.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finish.setBackground(getResources().getDrawable(R.drawable.bg_green1, getTheme()));
            }
        }

        id = getIntent().getStringExtra("id");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetActivity.this, TaskDet1Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

}
