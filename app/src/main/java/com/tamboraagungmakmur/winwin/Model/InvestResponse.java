package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 12/8/2017.
 */

public class InvestResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Invest[] getData() {
        return data;
    }

    public void setData(Invest[] data) {
        this.data = data;
    }

    private String code;
    private Invest[] data;

    public InvestResponse() {
    }
}