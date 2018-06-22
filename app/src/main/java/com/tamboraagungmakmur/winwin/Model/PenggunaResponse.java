package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/25/2017.
 */

public class PenggunaResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Pengguna[] getData() {
        return data;
    }

    public void setData(Pengguna[] data) {
        this.data = data;
    }

    private String code;
    private Pengguna[] data;

    public PenggunaResponse() {
    }
}
