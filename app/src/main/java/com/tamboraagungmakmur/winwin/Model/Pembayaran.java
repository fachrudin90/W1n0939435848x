package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/21/2017.
 */

public class Pembayaran {

    public String getPemb_id() {
        return pemb_id;
    }

    public void setPemb_id(String pemb_id) {
        this.pemb_id = pemb_id;
    }

    public String getPemb_tanggal() {
        return pemb_tanggal;
    }

    public void setPemb_tanggal(String pemb_tanggal) {
        this.pemb_tanggal = pemb_tanggal;
    }

    public String getPemb_waktu() {
        return pemb_waktu;
    }

    public void setPemb_waktu(String pemb_waktu) {
        this.pemb_waktu = pemb_waktu;
    }

    public String getPemb_nominal() {
        return pemb_nominal;
    }

    public void setPemb_nominal(String pemb_nominal) {
        this.pemb_nominal = pemb_nominal;
    }

    public String getPengajuan_nomor_pengajuan() {
        return pengajuan_nomor_pengajuan;
    }

    public void setPengajuan_nomor_pengajuan(String pengajuan_nomor_pengajuan) {
        this.pengajuan_nomor_pengajuan = pengajuan_nomor_pengajuan;
    }

    public String getPemb_catatan() {
        return pemb_catatan;
    }

    public void setPemb_catatan(String pemb_catatan) {
        this.pemb_catatan = pemb_catatan;
    }

    public String getMediabayar_label() {
        return mediabayar_label;
    }

    public void setMediabayar_label(String mediabayar_label) {
        this.mediabayar_label = mediabayar_label;
    }

    public String getJenisbayar_label() {
        return jenisbayar_label;
    }

    public void setJenisbayar_label(String jenisbayar_label) {
        this.jenisbayar_label = jenisbayar_label;
    }

    private String pemb_id;
    private String pemb_tanggal;
    private String pemb_waktu;
    private String pemb_nominal;
    private String pengajuan_nomor_pengajuan;
    private String pemb_catatan;
    private String mediabayar_label;
    private String jenisbayar_label;

    public String getCli_nama_lengkap() {
        return cli_nama_lengkap;
    }

    public void setCli_nama_lengkap(String cli_nama_lengkap) {
        this.cli_nama_lengkap = cli_nama_lengkap;
    }

    private String cli_nama_lengkap;

    public String getCli_nomor_pelanggan() {
        return cli_nomor_pelanggan;
    }

    public void setCli_nomor_pelanggan(String cli_nomor_pelanggan) {
        this.cli_nomor_pelanggan = cli_nomor_pelanggan;
    }

    private String cli_nomor_pelanggan;

    public Pembayaran() {
    }
}
