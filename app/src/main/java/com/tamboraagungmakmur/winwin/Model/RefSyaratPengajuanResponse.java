package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefSyaratPengajuanResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefSyaratPengajuan[] getData() {
        return data;
    }

    public void setData(RefSyaratPengajuan[] data) {
        this.data = data;
    }

    private String code;
    private RefSyaratPengajuan[] data;

    public RefSyaratPengajuanResponse() {
    }
}
