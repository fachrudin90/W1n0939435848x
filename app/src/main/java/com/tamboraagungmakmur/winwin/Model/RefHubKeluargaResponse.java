package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefHubKeluargaResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefHubKeluarga[] getData() {
        return data;
    }

    public void setData(RefHubKeluarga[] data) {
        this.data = data;
    }

    private String code;
    private RefHubKeluarga[] data;

    public RefHubKeluargaResponse() {
    }
}
