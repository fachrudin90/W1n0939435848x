package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefJenisPembayaranResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefJenisPembayaran[] getData() {
        return data;
    }

    public void setData(RefJenisPembayaran[] data) {
        this.data = data;
    }

    private String code;
    private RefJenisPembayaran[] data;

    public RefJenisPembayaranResponse() {
    }
}
