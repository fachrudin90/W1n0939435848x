package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class HomeModel {

    private String tglWaktu, tugas, pengajuan, klien, status;

    public HomeModel() {
    }

    public HomeModel(String tglWaktu, String tugas, String pengajuan, String klien, String status) {
        this.tglWaktu = tglWaktu;
        this.tugas = tugas;
        this.pengajuan = pengajuan;
        this.klien = klien;
        this.status = status;
    }

    public String getTglWaktu() {
        return tglWaktu;
    }

    public void setTglWaktu(String tglWaktu) {
        this.tglWaktu = tglWaktu;
    }

    public String getTugas() {
        return tugas;
    }

    public void setTugas(String tugas) {
        this.tugas = tugas;
    }

    public String getPengajuan() {
        return pengajuan;
    }

    public void setPengajuan(String pengajuan) {
        this.pengajuan = pengajuan;
    }

    public String getKlien() {
        return klien;
    }

    public void setKlien(String klien) {
        this.klien = klien;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
