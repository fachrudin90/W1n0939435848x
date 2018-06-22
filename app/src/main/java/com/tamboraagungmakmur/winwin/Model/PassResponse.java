package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/7/2017.
 */

public class PassResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private String code, data;
    private boolean error;

    public PassResponse() {
    }
}
