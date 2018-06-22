package com.tamboraagungmakmur.winwin;

import com.tamboraagungmakmur.winwin.Model.Pencairan;

/**
 * Created by innan on 8/26/2017.
 */

public class PencairanResponse1 {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Pencairan[] getData() {
        return data;
    }

    public void setData(Pencairan[] data) {
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
    private Pencairan[] data;

    public PencairanResponse1() {
    }

}
