package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/28/2017.
 */

public class NotesResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Notes[] getData() {
        return data;
    }

    public void setData(Notes[] data) {
        this.data = data;
    }

    private String code;
    private Notes[] data;

    public NotesResponse() {
    }
}
