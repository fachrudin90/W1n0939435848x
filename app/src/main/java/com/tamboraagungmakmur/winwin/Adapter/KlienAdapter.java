package com.tamboraagungmakmur.winwin.Adapter;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tamboraagungmakmur.winwin.AddDataKlienFragment;
import com.tamboraagungmakmur.winwin.ArchiveActivity;
import com.tamboraagungmakmur.winwin.EditDataKlienFragment;
import com.tamboraagungmakmur.winwin.KlienAktiva1Activity;
import com.tamboraagungmakmur.winwin.KlienAktivaActivity;
import com.tamboraagungmakmur.winwin.KlienDetailActivity;
import com.tamboraagungmakmur.winwin.Model.Klien;
import com.tamboraagungmakmur.winwin.Model.KlienModel;
import com.tamboraagungmakmur.winwin.PengHistActivity;
import com.tamboraagungmakmur.winwin.PengNoteActivity;
import com.tamboraagungmakmur.winwin.R;
import com.tamboraagungmakmur.winwin.voip.VoiceCallBridge;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class KlienAdapter extends RecyclerView.Adapter<KlienAdapter.MyViewHolder> {



    private ArrayList<Klien> dataSet;
    private Context context;
    private FragmentActivity fragmentActivity;
    private ProgressBar progressBar;

    private boolean isLast = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txNo)
        TextView txNo;
        @Bind(R.id.txNopel)
        TextView txNopel;
        @Bind(R.id.txNama)
        TextView txNama;
        @Bind(R.id.txNotelp)
        TextView txNotelp;
        @Bind(R.id.txAlamat)
        TextView txAlamat;
        @Bind(R.id.txJenisKel)
        TextView txJenisKel;
        @Bind(R.id.txStatus)
        TextView txStatus;
        @Bind(R.id.txSudahAktiva)
        TextView txSudahAktiva;
        @Bind(R.id.txSudahAktiva1)
        TextView txSudahAktiva1;
        @Bind(R.id.btView)
        ImageButton btView;
        @Bind(R.id.btNote)
        ImageButton btNote;
        @Bind(R.id.btCall)
        ImageButton btCall;
        @Bind(R.id.btArchive)
        ImageButton btArchive;
        @Bind(R.id.pg)
        ProgressBar pg;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public KlienAdapter(Context context, ArrayList<Klien> data) {
        this.dataSet = data;
        this.context = context;
        fragmentActivity = (FragmentActivity) context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_klien, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Klien value = dataSet.get(listPosition);

        progressBar = holder.pg;
        if (listPosition == dataSet.size()-1) {
            if (isLast) {
                holder.pg.setVisibility(View.GONE);
            } else {
                holder.pg.setVisibility(View.VISIBLE);
            }
        } else {
            holder.pg.setVisibility(View.GONE);
        }

        holder.txNo.setText("" + (listPosition+1));
        holder.txNopel.setText(value.getNomor_pelanggan());
        holder.txNama.setText(value.getNama_lengkap() + "\n" + value.getNo_ktp() + "\n" + value.getEmail());
        holder.txNotelp.setText(value.getHandphone());
        holder.txAlamat.setText(value.getAlamat());
        holder.txJenisKel.setText(value.getJenis_kelamin());
        holder.txStatus.setText(value.getStatus());
        if (value.getStatus().compareTo("Klien Aktif") == 0) {
            holder.txSudahAktiva.setText("Sudah");
            holder.txSudahAktiva1.setVisibility(View.GONE);
            holder.txSudahAktiva.setTextColor(Color.BLACK);
        } else {
            holder.txSudahAktiva.setText("Aktivasi Manual");
            holder.txSudahAktiva1.setVisibility(View.VISIBLE);
            holder.txSudahAktiva1.setText("Kirim ulang email dan sms aktivasi");
            holder.txSudahAktiva.setTextColor(Color.RED);
            holder.txSudahAktiva1.setTextColor(context.getResources().getColor(R.color.greendark));

            holder.txSudahAktiva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, KlienAktivaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", value.getId());
                    context.startActivity(intent);
                }
            });
            holder.txSudahAktiva1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, KlienAktiva1Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", value.getId());
                    context.startActivity(intent);
                }
            });
        }



        holder.btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KlienDetailActivity.class);
                intent.putExtra("id", value.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        holder.btNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PengHistActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", value.getId());
                context.startActivity(intent);
            }
        });

        holder.btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, VoiceCallBridge.class);
                i.putExtra("iduser", value.getId());
                i.putExtra("nama", value.getNama_lengkap());
                i.putExtra("foto", "");
                context.startActivity(i);
            }
        });

        holder.btArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArchiveActivity.class);
                intent.putExtra("id", value.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });


//        holder.btView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("id", value.getId());
//                bundle.putString("namaklien", value.getNama_lengkap());
//                bundle.putString("email", value.getEmail());
//                bundle.putString("noktp", value.getNo_ktp());
//                bundle.putString("alamat", value.getAlamat());
//                bundle.putString("kota", value.getKota());
//                bundle.putString("kec", value.getKecamatan());
//                bundle.putString("statusrumah", value.getStatus_rumah());
//                bundle.putString("statusrumahid", value.getStatus_rumah_id());
//                bundle.putString("kodepos", value.getKodepos());
//                bundle.putString("lama0", value.getLama_tinggal_tahun());
//                bundle.putString("lama1", value.getLama_tinggal_bulan());
//                bundle.putString("nohp", value.getTelepon());
//                bundle.putString("jenisper", value.getJenis_pekerjaan());
//                bundle.putString("namaper", value.getNama_perusahaan());
//                bundle.putString("hrdper", value.getHrd_perusahaan());
//                bundle.putString("jabatan", value.getJabatan());
//                bundle.putString("lamabekerja", value.getLama_bekerja());
//                bundle.putString("alamatper", value.getAlamat_perusahaan());
//                bundle.putString("teleponper", value.getTelepon_perusahaan());
//                bundle.putString("gaji", value.getBesar_gaji());
//                bundle.putString("tglgaji", value.getTanggal_gajian());
//                bundle.putString("namaangg", value.getNama_keluarga_serumah());
//                bundle.putString("teleponangg", value.getTelepon_keluarga_serumah());
//                bundle.putString("namaangg1", value.getNama_keluarga_tidak_serumah());
//                bundle.putString("jenishub", value.getHubungan_keluarga_tidak_serumah());
//                bundle.putString("alamatangg1", value.getAlamat_keluarga_tidak_serumah());
//                bundle.putString("bank", value.getNama_bank());
//                bundle.putString("cabangbank", value.getNama_cabang_bank());
//                bundle.putString("namarek", value.getNama_pemilik_rekening());
//                bundle.putString("norek", value.getNomor_rekening());
//
//                Fragment frag = new EditDataKlienFragment();
//                frag.setArguments(bundle);
//
//                FragmentManager manager = fragmentActivity.getSupportFragmentManager();
//                FragmentTransaction ft = manager.beginTransaction();
//                ft.replace(R.id.content_frame, frag);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });


        if ((listPosition >= getItemCount() - 1))
            load();

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("loading"));

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isLast = true;
            progressBar.setVisibility(View.GONE);
        }
    };

    public void load() {

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


}


