package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefMediaPembayaranResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefMediaPembayaran[] getData() {
        return data;
    }

    public void setData(RefMediaPembayaran[] data) {
        this.data = data;
    }

    private String code;
    private RefMediaPembayaran[] data;

    public RefMediaPembayaranResponse() {
    }
}
