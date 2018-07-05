package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefJkResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefJk[] getData() {
        return data;
    }

    public void setData(RefJk[] data) {
        this.data = data;
    }

    private String code;
    private RefJk[] data;

    public RefJkResponse() {
    }
}
