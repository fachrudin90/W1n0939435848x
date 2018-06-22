package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class ArchivePembayaranModel {

    private String tglBayar, nominal, noPengajuan, catatan, mediaBayar, jenisPembayaran;

    public ArchivePembayaranModel() {
    }

    public ArchivePembayaranModel(String tglBayar, String nominal, String noPengajuan, String catatan, String mediaBayar, String jenisPembayaran) {
        this.tglBayar = tglBayar;
        this.nominal = nominal;
        this.noPengajuan = noPengajuan;
        this.catatan = catatan;
        this.mediaBayar = mediaBayar;
        this.jenisPembayaran = jenisPembayaran;
    }

    public String getTglBayar() {
        return tglBayar;
    }

    public void setTglBayar(String tglBayar) {
        this.tglBayar = tglBayar;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getNoPengajuan() {
        return noPengajuan;
    }

    public void setNoPengajuan(String noPengajuan) {
        this.noPengajuan = noPengajuan;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getMediaBayar() {
        return mediaBayar;
    }

    public void setMediaBayar(String mediaBayar) {
        this.mediaBayar = mediaBayar;
    }

    public String getJenisPembayaran() {
        return jenisPembayaran;
    }

    public void setJenisPembayaran(String jenisPembayaran) {
        this.jenisPembayaran = jenisPembayaran;
    }
}
