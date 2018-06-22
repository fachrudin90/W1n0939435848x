package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/30/2017.
 */

public class Akunting {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKlien() {
        return klien;
    }

    public void setKlien(String klien) {
        this.klien = klien;
    }

    public String getNomor_pengajuan() {
        return nomor_pengajuan;
    }

    public void setNomor_pengajuan(String nomor_pengajuan) {
        this.nomor_pengajuan = nomor_pengajuan;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getKredit() {
        return kredit;
    }

    public void setKredit(String kredit) {
        this.kredit = kredit;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    private String date, klien, nomor_pengajuan, tipe, debit, kredit, saldo;

    public Akunting() {
    }
}
