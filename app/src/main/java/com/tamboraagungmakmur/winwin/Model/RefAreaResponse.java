package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefAreaResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefArea[] getData() {
        return data;
    }

    public void setData(RefArea[] data) {
        this.data = data;
    }

    private String code;
    private RefArea[] data;

    public RefAreaResponse() {
    }
}
