package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/13/2017.
 */

public class PengajuanDetail {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PengajuanDetailData getData() {
        return data;
    }

    public void setData(PengajuanDetailData data) {
        this.data = data;
    }

    private String code;
    private PengajuanDetailData data;

    public PengajuanDetail() {
    }
}
