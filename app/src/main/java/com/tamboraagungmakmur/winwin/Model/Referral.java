package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/30/2017.
 */

public class Referral {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getNomor_pelanggan() {
        return nomor_pelanggan;
    }

    public void setNomor_pelanggan(String nomor_pelanggan) {
        this.nomor_pelanggan = nomor_pelanggan;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public String getNomor_rekening() {
        return nomor_rekening;
    }

    public void setNomor_rekening(String nomor_rekening) {
        this.nomor_rekening = nomor_rekening;
    }

    public String getNama_rekening() {
        return nama_rekening;
    }

    public void setNama_rekening(String nama_rekening) {
        this.nama_rekening = nama_rekening;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getList_nomor_pengajuan() {
        return list_nomor_pengajuan;
    }

    public void setList_nomor_pengajuan(String[] list_nomor_pengajuan) {
        this.list_nomor_pengajuan = list_nomor_pengajuan;
    }

    private String nominal;
    private String nomor_pelanggan;
    private String nama_bank;
    private String nomor_rekening;
    private String nama_rekening;
    private String created_at;
    private String status;
    private String[] list_nomor_pengajuan;

    public Referral() {
    }
}
