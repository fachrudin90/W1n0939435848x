package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class ArchivePengajuanModel {

    private String tglBuat, noPengajuan, klien, pinjam, durasi, bayar, jatuhTempo, status;

    public ArchivePengajuanModel() {
    }

    public ArchivePengajuanModel(String tglBuat, String noPengajuan, String klien, String pinjam, String durasi, String bayar, String jatuhTempo, String status) {
        this.tglBuat = tglBuat;
        this.noPengajuan = noPengajuan;
        this.klien = klien;
        this.pinjam = pinjam;
        this.durasi = durasi;
        this.bayar = bayar;
        this.jatuhTempo = jatuhTempo;
        this.status = status;
    }

    public String getTglBuat() {
        return tglBuat;
    }

    public void setTglBuat(String tglBuat) {
        this.tglBuat = tglBuat;
    }

    public String getNoPengajuan() {
        return noPengajuan;
    }

    public void setNoPengajuan(String noPengajuan) {
        this.noPengajuan = noPengajuan;
    }

    public String getKlien() {
        return klien;
    }

    public void setKlien(String klien) {
        this.klien = klien;
    }

    public String getPinjam() {
        return pinjam;
    }

    public void setPinjam(String pinjam) {
        this.pinjam = pinjam;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getBayar() {
        return bayar;
    }

    public void setBayar(String bayar) {
        this.bayar = bayar;
    }

    public String getJatuhTempo() {
        return jatuhTempo;
    }

    public void setJatuhTempo(String jatuhTempo) {
        this.jatuhTempo = jatuhTempo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
