package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/2/2017.
 */

public class KaryawanResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Karyawan[] getData() {
        return data;
    }

    public void setData(Karyawan[] data) {
        this.data = data;
    }

    private String code;
    private Karyawan[] data;

    public KaryawanResponse() {
    }
}
