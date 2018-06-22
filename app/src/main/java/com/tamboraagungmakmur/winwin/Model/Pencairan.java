package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/26/2017.
 */

public class Pencairan {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomor_pengajuan() {
        return nomor_pengajuan;
    }

    public void setNomor_pengajuan(String nomor_pengajuan) {
        this.nomor_pengajuan = nomor_pengajuan;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private String id, nomor_pengajuan, nomor_pelanggan, nama_lengkap, nomor_rekening, nama_rekening, amount, status, created_at;

    public Pencairan() {
    }
}
