package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefWorkItemResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefWorkItem[] getData() {
        return data;
    }

    public void setData(RefWorkItem[] data) {
        this.data = data;
    }

    private String code;
    private RefWorkItem[] data;

    public RefWorkItemResponse() {
    }
}
