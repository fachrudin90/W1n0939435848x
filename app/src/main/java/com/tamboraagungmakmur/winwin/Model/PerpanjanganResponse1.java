package com.tamboraagungmakmur.winwin.Model;

import com.tamboraagungmakmur.winwin.Model.Perpanjangan;

/**
 * Created by innan on 8/21/2017.
 */

public class PerpanjanganResponse1 {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Perpanjangan[] getData() {
        return data;
    }

    public void setData(Perpanjangan[] data) {
        this.data = data;
    }

    private String code;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;
    private Perpanjangan[] data;

    public PerpanjanganResponse1() {
    }
}
