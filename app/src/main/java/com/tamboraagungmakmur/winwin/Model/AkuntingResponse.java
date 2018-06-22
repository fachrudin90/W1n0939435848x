package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/30/2017.
 */

public class AkuntingResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Akunting[] getData() {
        return data;
    }

    public void setData(Akunting[] data) {
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
    private Akunting[] data;

    public AkuntingResponse() {
    }
}
