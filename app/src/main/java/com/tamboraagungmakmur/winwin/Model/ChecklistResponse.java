package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/11/2017.
 */

public class ChecklistResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Checklist[] getData() {
        return data;
    }

    public void setData(Checklist[] data) {
        this.data = data;
    }

    private String code;
    private Checklist[] data;

    public ChecklistResponse() {
    }
}
