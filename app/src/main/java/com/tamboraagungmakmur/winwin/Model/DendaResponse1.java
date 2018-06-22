package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/21/2017.
 */

public class DendaResponse1 {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Denda[] getData() {
        return data;
    }

    public void setData(Denda[] data) {
        this.data = data;
    }

    private String code;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;
    private Denda[] data;

    public DendaResponse1() {
    }
}
