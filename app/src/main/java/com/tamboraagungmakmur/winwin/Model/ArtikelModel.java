package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class ArtikelModel {

    private String tglWaktu, judul, penulis, kataKunci, status;

    public ArtikelModel() {
    }

    public ArtikelModel(String tglWaktu, String judul, String penulis, String kataKunci, String status) {
        this.tglWaktu = tglWaktu;
        this.judul = judul;
        this.penulis = penulis;
        this.kataKunci = kataKunci;
        this.status = status;
    }

    public String getTglWaktu() {
        return tglWaktu;
    }

    public void setTglWaktu(String tglWaktu) {
        this.tglWaktu = tglWaktu;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getKataKunci() {
        return kataKunci;
    }

    public void setKataKunci(String kataKunci) {
        this.kataKunci = kataKunci;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
