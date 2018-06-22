package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class KlienRewardModel {

    private String nopel, nama, jmlRef, aksi;

    public KlienRewardModel() {
    }

    public KlienRewardModel(String nopel, String nama, String jmlRef, String aksi) {
        this.nopel = nopel;
        this.nama = nama;
        this.jmlRef = jmlRef;
        this.aksi = aksi;
    }

    public String getNopel() {
        return nopel;
    }

    public void setNopel(String nopel) {
        this.nopel = nopel;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJmlRef() {
        return jmlRef;
    }

    public void setJmlRef(String jmlRef) {
        this.jmlRef = jmlRef;
    }

    public String getAksi() {
        return aksi;
    }

    public void setAksi(String aksi) {
        this.aksi = aksi;
    }
}
