package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/21/2017.
 */

public class JanjiBayar {

    private String id;
    private String pengajuan_janji_bayar_aktif_pada;
    private String nomor_pengajuan;
    private String nama_lengkap;
    private String pengajuan_janji_bayar_tgl;

    public JanjiBayar() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPengajuan_janji_bayar_aktif_pada() {
        return pengajuan_janji_bayar_aktif_pada;
    }

    public void setPengajuan_janji_bayar_aktif_pada(String pengajuan_janji_bayar_aktif_pada) {
        this.pengajuan_janji_bayar_aktif_pada = pengajuan_janji_bayar_aktif_pada;
    }

    public String getNomor_pengajuan() {
        return nomor_pengajuan;
    }

    public void setNomor_pengajuan(String nomor_pengajuan) {
        this.nomor_pengajuan = nomor_pengajuan;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getPengajuan_janji_bayar_tgl() {
        return pengajuan_janji_bayar_tgl;
    }

    public void setPengajuan_janji_bayar_tgl(String pengajuan_janji_bayar_tgl) {
        this.pengajuan_janji_bayar_tgl = pengajuan_janji_bayar_tgl;
    }
}
