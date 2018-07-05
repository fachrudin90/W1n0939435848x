package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefFrekuensiResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefFrekuensi[] getData() {
        return data;
    }

    public void setData(RefFrekuensi[] data) {
        this.data = data;
    }

    private String code;
    private RefFrekuensi[] data;

    public RefFrekuensiResponse() {
    }
}
