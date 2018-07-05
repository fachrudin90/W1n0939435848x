package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusPengajuanResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefStatusPengajuan[] getData() {
        return data;
    }

    public void setData(RefStatusPengajuan[] data) {
        this.data = data;
    }

    private String code;
    private RefStatusPengajuan[] data;

    public RefStatusPengajuanResponse() {
    }
}
