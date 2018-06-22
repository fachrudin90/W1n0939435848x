package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/9/2017.
 */

public class EmailResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;

    public Email[] getData() {
        return data;
    }

    public void setData(Email[] data) {
        this.data = data;
    }

    private Email[] data;

    public EmailResponse() {
    }

}
