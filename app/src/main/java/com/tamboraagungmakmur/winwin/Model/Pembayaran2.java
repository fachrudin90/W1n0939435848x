package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/2/2017.
 */

public class Pembayaran2 {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    private String amount;
    private String note;
    private String media;
    private String jenis;
    private String created_at;
    private String created_by;

    public String getNomor_pengajuan() {
        return nomor_pengajuan;
    }

    public void setNomor_pengajuan(String nomor_pengajuan) {
        this.nomor_pengajuan = nomor_pengajuan;
    }

    private String nomor_pengajuan;

    public Pembayaran2() {
    }
}
