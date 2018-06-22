package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/28/2017.
 */

public class LinksResponse {

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

    public Links[] getData() {
        return data;
    }

    public void setData(Links[] data) {
        this.data = data;
    }

    private boolean error;
    private String code;
    private Links[] data;

    public LinksResponse() {
    }
}
