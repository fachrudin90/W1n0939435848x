package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class LogAktivitasModel {

    private String tglWaktu, waktu, tipe, tabel, aksesIp, aksesBrowser;

    public LogAktivitasModel() {
    }

    public LogAktivitasModel(String tglWaktu, String waktu, String tipe, String tabel, String aksesIp, String aksesBrowser) {
        this.tglWaktu = tglWaktu;
        this.waktu = waktu;
        this.tipe = tipe;
        this.tabel = tabel;
        this.aksesIp = aksesIp;
        this.aksesBrowser = aksesBrowser;
    }

    public String getTglWaktu() {
        return tglWaktu;
    }

    public void setTglWaktu(String tglWaktu) {
        this.tglWaktu = tglWaktu;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getTabel() {
        return tabel;
    }

    public void setTabel(String tabel) {
        this.tabel = tabel;
    }

    public String getAksesIp() {
        return aksesIp;
    }

    public void setAksesIp(String aksesIp) {
        this.aksesIp = aksesIp;
    }

    public String getAksesBrowser() {
        return aksesBrowser;
    }

    public void setAksesBrowser(String aksesBrowser) {
        this.aksesBrowser = aksesBrowser;
    }
}
