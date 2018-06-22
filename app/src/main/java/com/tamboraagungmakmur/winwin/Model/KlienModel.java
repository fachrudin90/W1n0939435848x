package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class KlienModel {

    private String nopel, namaKtp, notelp, alamat, jns_kelamin, status, aktiva, action;

    public KlienModel() {
    }

    public KlienModel(String nopel, String namaKtp, String notelp, String alamat, String jns_kelamin, String status, String aktiva, String action) {
        this.nopel = nopel;
        this.namaKtp = namaKtp;
        this.notelp = notelp;
        this.alamat = alamat;
        this.jns_kelamin = jns_kelamin;
        this.status = status;
        this.aktiva = aktiva;
        this.action = action;
    }

    public String getNopel() {
        return nopel;
    }

    public void setNopel(String nopel) {
        this.nopel = nopel;
    }

    public String getNamaKtp() {
        return namaKtp;
    }

    public void setNamaKtp(String namaKtp) {
        this.namaKtp = namaKtp;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJns_kelamin() {
        return jns_kelamin;
    }

    public void setJns_kelamin(String jns_kelamin) {
        this.jns_kelamin = jns_kelamin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAktiva() {
        return aktiva;
    }

    public void setAktiva(String aktiva) {
        this.aktiva = aktiva;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
