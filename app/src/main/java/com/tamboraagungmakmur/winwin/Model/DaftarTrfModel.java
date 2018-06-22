package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class DaftarTrfModel {

    private String tanggal, nopel, bank, norek, nama, jmlTrf, noPengajuanTerkait, status;

    public DaftarTrfModel() {
    }

    public DaftarTrfModel(String tanggal, String nopel, String bank, String norek, String nama, String jmlTrf, String noPengajuanTerkait, String status) {
        this.tanggal = tanggal;
        this.nopel = nopel;
        this.bank = bank;
        this.norek = norek;
        this.nama = nama;
        this.jmlTrf = jmlTrf;
        this.noPengajuanTerkait = noPengajuanTerkait;
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNopel() {
        return nopel;
    }

    public void setNopel(String nopel) {
        this.nopel = nopel;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJmlTrf() {
        return jmlTrf;
    }

    public void setJmlTrf(String jmlTrf) {
        this.jmlTrf = jmlTrf;
    }

    public String getNoPengajuanTerkait() {
        return noPengajuanTerkait;
    }

    public void setNoPengajuanTerkait(String noPengajuanTerkait) {
        this.noPengajuanTerkait = noPengajuanTerkait;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
