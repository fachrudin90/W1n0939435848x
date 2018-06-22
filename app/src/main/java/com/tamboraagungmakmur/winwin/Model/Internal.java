package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/30/2017.
 */

public class Internal {

    public Internal(String klien, String nilai_pinjam, String durasi, String total_kembali, String margin, String interest) {
        this.klien = klien;
        this.nilai_pinjam = nilai_pinjam;
        this.durasi = durasi;
        this.total_kembali = total_kembali;
        this.margin = margin;
        this.interest = interest;
    }

    public String getKlien() {
        return klien;
    }

    public void setKlien(String klien) {
        this.klien = klien;
    }

    public String getNilai_pinjam() {
        return nilai_pinjam;
    }

    public void setNilai_pinjam(String nilai_pinjam) {
        this.nilai_pinjam = nilai_pinjam;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getTotal_kembali() {
        return total_kembali;
    }

    public void setTotal_kembali(String total_kembali) {
        this.total_kembali = total_kembali;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    private String klien;
    private String nilai_pinjam;
    private String durasi;
    private String total_kembali;
    private String margin;
    private String interest;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTahap() {
        return tahap;
    }

    public void setTahap(String tahap) {
        this.tahap = tahap;
    }

    private String tahap;

    public Internal() {
    }
}
