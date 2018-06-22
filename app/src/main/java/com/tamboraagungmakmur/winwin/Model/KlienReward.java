package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/13/2017.
 */

public class KlienReward {

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getNomor_pelanggan() {
        return nomor_pelanggan;
    }

    public void setNomor_pelanggan(String nomor_pelanggan) {
        this.nomor_pelanggan = nomor_pelanggan;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getJumlah_referral() {
        return jumlah_referral;
    }

    public void setJumlah_referral(String jumlah_referral) {
        this.jumlah_referral = jumlah_referral;
    }

    private String id_client, nomor_pelanggan, nama_lengkap, jumlah_referral;

    public KlienReward() {
    }
}
