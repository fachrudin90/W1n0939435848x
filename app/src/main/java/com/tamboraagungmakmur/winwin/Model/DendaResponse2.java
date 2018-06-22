package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/2/2017.
 */

public class DendaResponse2 {
    public DendaResponse2() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Denda2[] getData() {
        return data;
    }

    public void setData(Denda2[] data) {
        this.data = data;
    }

    private String code;
    private Denda2[] data;

}
