package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefKotaResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefKota[] getData() {
        return data;
    }

    public void setData(RefKota[] data) {
        this.data = data;
    }

    private String code;
    private RefKota[] data;

    public RefKotaResponse() {
    }
}
