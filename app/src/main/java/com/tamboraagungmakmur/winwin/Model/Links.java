package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/28/2017.
 */

public class Links {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String id, nomor_pelanggan, nama_lengkap, label, type;

    public Links() {
    }
}
