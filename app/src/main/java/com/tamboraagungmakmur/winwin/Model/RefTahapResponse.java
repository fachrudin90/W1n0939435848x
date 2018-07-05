package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefTahapResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefTahap[] getData() {
        return data;
    }

    public void setData(RefTahap[] data) {
        this.data = data;
    }

    private String code;
    private RefTahap[] data;

    public RefTahapResponse() {
    }
}
