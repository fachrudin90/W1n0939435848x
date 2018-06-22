package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/24/2017.
 */

public class ChatResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Chat[] getData() {
        return data;
    }

    public void setData(Chat[] data) {
        this.data = data;
    }

    private String code;
    private Chat[] data;

    public ChatResponse() {
    }
}
