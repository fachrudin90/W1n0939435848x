package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusKlienResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefStatusKlien[] getData() {
        return data;
    }

    public void setData(RefStatusKlien[] data) {
        this.data = data;
    }

    private String code;
    private RefStatusKlien[] data;

    public RefStatusKlienResponse() {
    }
}
