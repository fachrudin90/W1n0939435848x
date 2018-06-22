package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/2/2017.
 */

public class Karyawan {

    public boolean isKar_sudah_keluar() {
        return kar_sudah_keluar;
    }

    public void setKar_sudah_keluar(boolean kar_sudah_keluar) {
        this.kar_sudah_keluar = kar_sudah_keluar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_karyawan() {
        return nama_karyawan;
    }

    public void setNama_karyawan(String nama_karyawan) {
        this.nama_karyawan = nama_karyawan;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getKar_tgl_masuk() {
        return kar_tgl_masuk;
    }

    public void setKar_tgl_masuk(String kar_tgl_masuk) {
        this.kar_tgl_masuk = kar_tgl_masuk;
    }

    public String getKar_tgl_keluar() {
        return kar_tgl_keluar;
    }

    public void setKar_tgl_keluar(String kar_tgl_keluar) {
        this.kar_tgl_keluar = kar_tgl_keluar;
    }

    private boolean kar_sudah_keluar;
    private String id;
    private String nama_karyawan;
    private String jabatan;
    private String kar_tgl_masuk;
    private String kar_tgl_keluar;

    public String getKar_email_winwin() {
        return kar_email_winwin;
    }

    public void setKar_email_winwin(String kar_email_winwin) {
        this.kar_email_winwin = kar_email_winwin;
    }

    private String kar_email_winwin;

    public Karyawan() {
    }
}
