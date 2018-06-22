package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/23/2017.
 */

public class KlienResponse1 {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Klien2 getData() {
        return data;
    }

    public void setData(Klien2 data) {
        this.data = data;
    }

    private String code;
    private Klien2 data;

    public KlienResponse1() {
    }
}
