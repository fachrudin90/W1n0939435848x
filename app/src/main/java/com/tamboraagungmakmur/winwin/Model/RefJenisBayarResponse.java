package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefJenisBayarResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefJenisBayar[] getData() {
        return data;
    }

    public void setData(RefJenisBayar[] data) {
        this.data = data;
    }

    private String code;
    private RefJenisBayar[] data;

    public RefJenisBayarResponse() {
    }
}
