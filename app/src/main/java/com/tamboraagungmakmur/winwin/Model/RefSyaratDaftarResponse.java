package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefSyaratDaftarResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefSyaratDaftar[] getData() {
        return data;
    }

    public void setData(RefSyaratDaftar[] data) {
        this.data = data;
    }

    private String code;
    private RefSyaratDaftar[] data;

    public RefSyaratDaftarResponse() {
    }
}
