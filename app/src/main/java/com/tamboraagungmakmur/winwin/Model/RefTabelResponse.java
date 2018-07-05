package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefTabelResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefTabel[] getData() {
        return data;
    }

    public void setData(RefTabel[] data) {
        this.data = data;
    }

    private String code;
    private RefTabel[] data;

    public RefTabelResponse() {
    }
}
