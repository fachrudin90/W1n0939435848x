package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefTahapPencairanResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefTahapPencairan[] getData() {
        return data;
    }

    public void setData(RefTahapPencairan[] data) {
        this.data = data;
    }

    private String code;
    private RefTahapPencairan[] data;

    public RefTahapPencairanResponse() {
    }
}
