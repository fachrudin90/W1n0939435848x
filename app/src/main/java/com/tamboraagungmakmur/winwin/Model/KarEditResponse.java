package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/2/2017.
 */

public class KarEditResponse {

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

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

    private boolean error;
    private String code, data;

    public KarEditResponse() {
    }
}
