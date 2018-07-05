package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefPerusahaanResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefPerusahaan[] getData() {
        return data;
    }

    public void setData(RefPerusahaan[] data) {
        this.data = data;
    }

    private String code;
    private RefPerusahaan[] data;

    public RefPerusahaanResponse() {
    }
}
