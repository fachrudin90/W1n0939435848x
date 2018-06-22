package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 12/9/2017.
 */

public class InvestorDetResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Investor getData() {
        return data;
    }

    public void setData(Investor data) {
        this.data = data;
    }

    private String code;
    private Investor data;

    public InvestorDetResponse() {
    }
}
