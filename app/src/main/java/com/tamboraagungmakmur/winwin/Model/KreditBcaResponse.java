package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/4/2017.
 */

public class KreditBcaResponse {

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

    public KreditBca1 getData() {
        return data;
    }

    public void setData(KreditBca1 data) {
        this.data = data;
    }

    private boolean error;
    private String code;
    private KreditBca1 data;

    public KreditBcaResponse() {
    }
}
