package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusRumahResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefStatusRumah[] getData() {
        return data;
    }

    public void setData(RefStatusRumah[] data) {
        this.data = data;
    }

    private String code;
    private RefStatusRumah[] data;

    public RefStatusRumahResponse() {
    }
}
