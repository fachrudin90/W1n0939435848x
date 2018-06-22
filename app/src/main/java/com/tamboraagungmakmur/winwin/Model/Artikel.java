package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/7/2017.
 */

public class Artikel {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal_input() {
        return tanggal_input;
    }

    public void setTanggal_input(String tanggal_input) {
        this.tanggal_input = tanggal_input;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String id, tanggal_input, tags, judul, penulis, konten, status;

    public Artikel() {
    }
}
