package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusArtikelResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefStatusArtikel[] getData() {
        return data;
    }

    public void setData(RefStatusArtikel[] data) {
        this.data = data;
    }

    private String code;
    private RefStatusArtikel[] data;

    public RefStatusArtikelResponse() {
    }
}
