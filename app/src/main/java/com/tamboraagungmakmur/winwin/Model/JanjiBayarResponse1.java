package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/21/2017.
 */

public class JanjiBayarResponse1 {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JanjiBayar[] getData() {
        return data;
    }

    public void setData(JanjiBayar[] data) {
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
    private JanjiBayar[] data;

    public JanjiBayarResponse1() {
    }
}
