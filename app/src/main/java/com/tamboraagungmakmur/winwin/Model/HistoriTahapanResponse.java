package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/16/2017.
 */

public class HistoriTahapanResponse {

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

    public HistoriTahapan[] getData() {
        return data;
    }

    public void setData(HistoriTahapan[] data) {
        this.data = data;
    }

    private boolean error;
    private String code;
    private HistoriTahapan[] data;

    public HistoriTahapanResponse() {
    }
}
