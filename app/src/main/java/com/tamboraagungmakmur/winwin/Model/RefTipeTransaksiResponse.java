package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefTipeTransaksiResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RefTipeTransaksi[] getData() {
        return data;
    }

    public void setData(RefTipeTransaksi[] data) {
        this.data = data;
    }

    private String code;
    private RefTipeTransaksi[] data;

    public RefTipeTransaksiResponse() {
    }
}
