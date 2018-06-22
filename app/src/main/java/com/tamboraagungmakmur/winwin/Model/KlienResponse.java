package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/2/2017.
 */

public class KlienResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;

    public Klien[] getData() {
        return data;
    }

    public void setData(Klien[] data) {
        this.data = data;
    }

    private Klien[] data;

    public KlienResponse() {
    }
}

