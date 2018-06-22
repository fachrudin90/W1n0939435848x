package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/21/2017.
 */

public class Denda {

    public Denda() {
    }

    public String getDenda_id() {
        return denda_id;
    }

    public void setDenda_id(String denda_id) {
        this.denda_id = denda_id;
    }

    public String getDenda_created_at() {
        return denda_created_at;
    }

    public void setDenda_created_at(String denda_created_at) {
        this.denda_created_at = denda_created_at;
    }

    public String getPengajuan_nomor_pengajuan() {
        return pengajuan_nomor_pengajuan;
    }

    public void setPengajuan_nomor_pengajuan(String pengajuan_nomor_pengajuan) {
        this.pengajuan_nomor_pengajuan = pengajuan_nomor_pengajuan;
    }

    public String getDenda_biaya() {
        return denda_biaya;
    }

    public void setDenda_biaya(String denda_biaya) {
        this.denda_biaya = denda_biaya;
    }

    public String getDenda_catatan() {
        return denda_catatan;
    }

    public void setDenda_catatan(String denda_catatan) {
        this.denda_catatan = denda_catatan;
    }

    private String denda_id;
    private String denda_created_at;
    private String pengajuan_nomor_pengajuan;
    private String denda_biaya;
    private String denda_catatan;
    private String cli_nama_lengkap;

    public String getCli_nama_lengkap() {
        return cli_nama_lengkap;
    }

    public void setCli_nama_lengkap(String cli_nama_lengkap) {
        this.cli_nama_lengkap = cli_nama_lengkap;
    }

    public String getCli_nomor_pelanggan() {
        return cli_nomor_pelanggan;
    }

    public void setCli_nomor_pelanggan(String cli_nomor_pelanggan) {
        this.cli_nomor_pelanggan = cli_nomor_pelanggan;
    }

    private String cli_nomor_pelanggan;

}
