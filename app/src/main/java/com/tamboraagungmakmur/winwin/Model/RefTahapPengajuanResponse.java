package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefTahapPengajuanResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefTahapPengajuan[] getData() {
        return data;
    }

    public void setData(RefTahapPengajuan[] data) {
        this.data = data;
    }

    private String code;
    private RefTahapPengajuan[] data;

    public RefTahapPengajuanResponse() {
    }
}
