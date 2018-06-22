package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 12/7/2017.
 */

public class InvestorNotesResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InvestorNotes[] getData() {
        return data;
    }

    public void setData(InvestorNotes[] data) {
        this.data = data;
    }

    private String code;
    private InvestorNotes[] data;

    public InvestorNotesResponse() {
    }
}
