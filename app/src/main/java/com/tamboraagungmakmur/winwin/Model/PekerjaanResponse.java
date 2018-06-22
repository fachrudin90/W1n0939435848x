package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/28/2017.
 */

public class PekerjaanResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Pekerjaan[] getData() {
        return data;
    }

    public void setData(Pekerjaan[] data) {
        this.data = data;
    }

    private String code;
    private Pekerjaan[] data;

    public PekerjaanResponse() {
    }
}
