package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/27/2017.
 */

public class KlienFileResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public KlienFile[] getData() {
        return data;
    }

    public void setData(KlienFile[] data) {
        this.data = data;
    }

    private String code;
    private KlienFile[] data;

    public KlienFileResponse() {
    }
}
