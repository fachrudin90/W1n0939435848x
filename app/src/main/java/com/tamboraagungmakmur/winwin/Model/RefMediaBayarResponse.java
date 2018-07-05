package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefMediaBayarResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefMediaBayar[] getData() {
        return data;
    }

    public void setData(RefMediaBayar[] data) {
        this.data = data;
    }

    private String code;
    private RefMediaBayar[] data;

    public RefMediaBayarResponse() {
    }
}
