package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefProsesImportResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefProsesImport[] getData() {
        return data;
    }

    public void setData(RefProsesImport[] data) {
        this.data = data;
    }

    private String code;
    private RefProsesImport[] data;

    public RefProsesImportResponse() {
    }
}
