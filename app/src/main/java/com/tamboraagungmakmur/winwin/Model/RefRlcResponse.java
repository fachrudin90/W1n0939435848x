package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefRlcResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefRlc[] getData() {
        return data;
    }

    public void setData(RefRlc[] data) {
        this.data = data;
    }

    private String code;
    private RefRlc[] data;

    public RefRlcResponse() {
    }
}
