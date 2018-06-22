package com.tamboraagungmakmur.winwin;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.tamboraagungmakmur.winwin.Adapter.LogAktivitasAdapter;
import com.tamboraagungmakmur.winwin.Model.LogAktivitasModel;
import com.tamboraagungmakmur.winwin.Utils.FormatDate;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogAktivitas extends AppCompatActivity {

    @Bind(R.id.btBack)
    ImageButton btBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bar)
    AppBarLayout bar;
    @Bind(R.id.txTgl1)
    EditText txTgl1;
    @Bind(R.id.txTgl2)
    EditText txTgl2;
    @Bind(R.id.btCari)
    Button btCari;
    @Bind(R.id.rvList)
    RecyclerView rvList;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<LogAktivitasModel> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_aktivitas);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(LogAktivitas.this);
        rvList.setLayoutManager(layoutManager);
        adapter = new LogAktivitasAdapter(LogAktivitas.this, dataSet);
        rvList.setAdapter(adapter);

        GetData();

    }


    private void GetData() {

        dataSet.clear();
        for (int i = 0; i < 15; i++) {

            LogAktivitasModel logAktivitasModel = new LogAktivitasModel("2017-02-01", "10:59:07", "DEBISERT", "log_statistik_operator", "110.210.215.15", "Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko");
            dataSet.add(logAktivitasModel);
        }

        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btBack, R.id.txTgl1, R.id.txTgl2, R.id.btCari})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.txTgl1:
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTgl1.setText(tgl);
                            }
                        });
                cdp.show(getSupportFragmentManager(), null);
                break;
            case R.id.txTgl2:
                CalendarDatePickerDialogFragment cdp2 = new CalendarDatePickerDialogFragment().setThemeCustom(R.style.BetterPickersDialogCustom)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String dateString = year + "-" + month + "-" + dayOfMonth;
                                String tgl = FormatDate.format(dateString, "yyyy-M-dd", "dd-MM-yyyy");
                                txTgl2.setText(tgl);
                            }
                        });
                cdp2.show(getSupportFragmentManager(), null);
                break;
            case R.id.btCari:
                break;
        }
    }
}
