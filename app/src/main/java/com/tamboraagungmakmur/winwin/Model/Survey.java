package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/13/2017.
 */

public class Survey {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRencana() {
        return rencana;
    }

    public void setRencana(String rencana) {
        this.rencana = rencana;
    }

    public String getCatatan_rencana() {
        return catatan_rencana;
    }

    public void setCatatan_rencana(String catatan_rencana) {
        this.catatan_rencana = catatan_rencana;
    }

    public String getPelaksanaan() {
        return pelaksanaan;
    }

    public void setPelaksanaan(String pelaksanaan) {
        this.pelaksanaan = pelaksanaan;
    }

    public String getPelaksana() {
        return pelaksana;
    }

    public void setPelaksana(String pelaksana) {
        this.pelaksana = pelaksana;
    }

    public String getHasil_sesuai() {
        return hasil_sesuai;
    }

    public void setHasil_sesuai(String hasil_sesuai) {
        this.hasil_sesuai = hasil_sesuai;
    }

    public String getHasil_catatan() {
        return hasil_catatan;
    }

    public void setHasil_catatan(String hasil_catatan) {
        this.hasil_catatan = hasil_catatan;
    }

    private String rencana;

    public String getRencana_tanggal() {
        return rencana_tanggal;
    }

    public void setRencana_tanggal(String rencana_tanggal) {
        this.rencana_tanggal = rencana_tanggal;
    }

    public String getRencana_waktu() {
        return rencana_waktu;
    }

    public void setRencana_waktu(String rencana_waktu) {
        this.rencana_waktu = rencana_waktu;
    }

    private String rencana_tanggal;
    private String rencana_waktu;
    private String catatan_rencana;
    private String pelaksanaan;
    private String pelaksana;
    private String hasil_sesuai;
    private String hasil_catatan;

    public Survey() {
    }
}
