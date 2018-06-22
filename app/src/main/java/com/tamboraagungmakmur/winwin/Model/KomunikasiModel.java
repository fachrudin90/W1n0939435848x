package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class KomunikasiModel {

    private String tglWaktu, info, isi, status;

    public KomunikasiModel() {
    }

    public KomunikasiModel(String tglWaktu, String info, String isi, String status) {
        this.tglWaktu = tglWaktu;
        this.info = info;
        this.isi = isi;
        this.status = status;
    }

    public String getTglWaktu() {
        return tglWaktu;
    }

    public void setTglWaktu(String tglWaktu) {
        this.tglWaktu = tglWaktu;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
