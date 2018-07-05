package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusTestimoniResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefStatusTestimoni[] getData() {
        return data;
    }

    public void setData(RefStatusTestimoni[] data) {
        this.data = data;
    }

    private String code;
    private RefStatusTestimoni[] data;

    public RefStatusTestimoniResponse() {
    }
}
