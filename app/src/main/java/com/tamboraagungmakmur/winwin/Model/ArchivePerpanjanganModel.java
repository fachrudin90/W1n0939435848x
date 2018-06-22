package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class ArchivePerpanjanganModel {

    private String tglPermintaan, noPengajuan, tglMulai, durasi, jatuhTempo, catatan, status;

    public ArchivePerpanjanganModel() {
    }

    public ArchivePerpanjanganModel(String tglPermintaan, String noPengajuan, String tglMulai, String durasi, String jatuhTempo, String catatan, String status) {
        this.tglPermintaan = tglPermintaan;
        this.noPengajuan = noPengajuan;
        this.tglMulai = tglMulai;
        this.durasi = durasi;
        this.jatuhTempo = jatuhTempo;
        this.catatan = catatan;
        this.status = status;
    }

    public String getTglPermintaan() {
        return tglPermintaan;
    }

    public void setTglPermintaan(String tglPermintaan) {
        this.tglPermintaan = tglPermintaan;
    }

    public String getNoPengajuan() {
        return noPengajuan;
    }

    public void setNoPengajuan(String noPengajuan) {
        this.noPengajuan = noPengajuan;
    }

    public String getTglMulai() {
        return tglMulai;
    }

    public void setTglMulai(String tglMulai) {
        this.tglMulai = tglMulai;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getJatuhTempo() {
        return jatuhTempo;
    }

    public void setJatuhTempo(String jatuhTempo) {
        this.jatuhTempo = jatuhTempo;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
