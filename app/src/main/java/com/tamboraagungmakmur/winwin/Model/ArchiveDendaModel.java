package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class ArchiveDendaModel {

    private String tglDenda, noPengajuan, jmlDenda, keterangan;

    public ArchiveDendaModel() {
    }

    public ArchiveDendaModel(String tglDenda, String noPengajuan, String jmlDenda, String keterangan) {
        this.tglDenda = tglDenda;
        this.noPengajuan = noPengajuan;
        this.jmlDenda = jmlDenda;
        this.keterangan = keterangan;
    }

    public String getTglDenda() {
        return tglDenda;
    }

    public void setTglDenda(String tglDenda) {
        this.tglDenda = tglDenda;
    }

    public String getNoPengajuan() {
        return noPengajuan;
    }

    public void setNoPengajuan(String noPengajuan) {
        this.noPengajuan = noPengajuan;
    }

    public String getJmlDenda() {
        return jmlDenda;
    }

    public void setJmlDenda(String jmlDenda) {
        this.jmlDenda = jmlDenda;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
