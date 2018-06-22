package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 12/14/2017.
 */

public class InvestlainResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Investlain[] getData() {
        return data;
    }

    public void setData(Investlain[] data) {
        this.data = data;
    }

    private String code;
    private Investlain[] data;

    public InvestlainResponse() {
    }
}
