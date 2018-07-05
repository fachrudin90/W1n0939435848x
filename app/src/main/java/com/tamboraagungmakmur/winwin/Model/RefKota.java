package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefKota {

    private String id;
    private String kodepos_provinsi;
    private String kodepos_kabupaten;
    private String tolak;

    public RefKota() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodepos_provinsi() {
        return kodepos_provinsi;
    }

    public void setKodepos_provinsi(String kodepos_provinsi) {
        this.kodepos_provinsi = kodepos_provinsi;
    }

    public String getKodepos_kabupaten() {
        return kodepos_kabupaten;
    }

    public void setKodepos_kabupaten(String kodepos_kabupaten) {
        this.kodepos_kabupaten = kodepos_kabupaten;
    }

    public String getTolak() {
        return tolak;
    }

    public void setTolak(String tolak) {
        this.tolak = tolak;
    }
}
