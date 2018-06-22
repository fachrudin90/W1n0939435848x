package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/7/2017.
 */

public class ArtikelResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Artikel[] getData() {
        return data;
    }

    public void setData(Artikel[] data) {
        this.data = data;
    }

    private String code;
    private Artikel[] data;

    public ArtikelResponse() {
    }
}
