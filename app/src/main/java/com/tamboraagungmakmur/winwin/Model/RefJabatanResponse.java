package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefJabatanResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefJabatan[] getData() {
        return data;
    }

    public void setData(RefJabatan[] data) {
        this.data = data;
    }

    private String code;
    private RefJabatan[] data;

    public RefJabatanResponse() {
    }
}
