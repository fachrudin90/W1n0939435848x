package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefKodePosResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefKodePos[] getData() {
        return data;
    }

    public void setData(RefKodePos[] data) {
        this.data = data;
    }

    private String code;
    private RefKodePos[] data;

    public RefKodePosResponse() {
    }
}
