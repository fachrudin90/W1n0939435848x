package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/26/2017.
 */

public class KlienDetailResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public KlienDetail getData() {
        return data;
    }

    public void setData(KlienDetail data) {
        this.data = data;
    }

    private String code;
    private KlienDetail data;

    public KlienDetailResponse() {
    }
}
