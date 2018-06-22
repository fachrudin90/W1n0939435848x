package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/2/2017.
 */

public class PembayaranResponse2 {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Pembayaran2[] getData() {
        return data;
    }

    public void setData(Pembayaran2[] data) {
        this.data = data;
    }

    private String code;
    private Pembayaran2[] data;

    public PembayaranResponse2() {
    }
}
