package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefBankResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefBank[] getData() {
        return data;
    }

    public void setData(RefBank[] data) {
        this.data = data;
    }

    private String code;
    private RefBank[] data;

    public RefBankResponse() {
    }
}
