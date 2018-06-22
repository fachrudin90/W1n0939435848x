package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/2/2017.
 */

public class PerpanjanganResponse3 {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Perpanjangan3[] getData() {
        return data;
    }

    public void setData(Perpanjangan3[] data) {
        this.data = data;
    }

    private String code;
    private Perpanjangan3[] data;

    public PerpanjanganResponse3() {
    }
}
