package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class TaskListModel {

    private String tglWaktu, tugas, pengajuan, klien, tipe, status;

    public TaskListModel() {
    }

    public TaskListModel(String tglWaktu, String tugas, String pengajuan, String klien, String tipe, String status) {
        this.tglWaktu = tglWaktu;
        this.tugas = tugas;
        this.pengajuan = pengajuan;
        this.klien = klien;
        this.tipe = tipe;
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

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
