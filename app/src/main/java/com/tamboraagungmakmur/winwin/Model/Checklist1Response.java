package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/15/2017.
 */

public class Checklist1Response {

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

    public ChecklistData getData() {
        return data;
    }

    public void setData(ChecklistData data) {
        this.data = data;
    }

    private boolean error;
    private String code;
    private ChecklistData data;

    public Checklist1Response() {
    }
}
