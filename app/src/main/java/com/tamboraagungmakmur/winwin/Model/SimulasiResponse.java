package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/30/2017.
 */

public class SimulasiResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Simulasi[] getData() {
        return data;
    }

    public void setData(Simulasi[] data) {
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
    private Simulasi[] data;

    public SimulasiResponse() {
    }
}
