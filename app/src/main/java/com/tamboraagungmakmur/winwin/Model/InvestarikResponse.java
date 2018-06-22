package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 12/8/2017.
 */

public class InvestarikResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Investarik[] getData() {
        return data;
    }

    public void setData(Investarik[] data) {
        this.data = data;
    }

    private String code;
    private Investarik[] data;

    public InvestarikResponse() {
    }
}
