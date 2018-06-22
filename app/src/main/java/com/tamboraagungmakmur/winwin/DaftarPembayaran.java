package com.tamboraagungmakmur.winwin;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaftarPembayaran extends AppCompatActivity {

    @Bind(R.id.btBack)
    ImageButton btBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bar)
    AppBarLayout bar;
    @Bind(R.id.txPencarian)
    EditText txPencarian;
    @Bind(R.id.txTgl1)
    EditText txTgl1;
    @Bind(R.id.txTgl2)
    EditText txTgl2;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.rvList)
    RecyclerView rvList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pembayaran);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btBack, R.id.txTgl1, R.id.txTgl2, R.id.btCari})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.txTgl1:
                break;
            case R.id.txTgl2:
                break;
            case R.id.btCari:
                break;
        }
    }
}
