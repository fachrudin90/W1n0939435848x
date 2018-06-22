package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/6/2017.
 */

public class UserResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User[] getData() {
        return data;
    }

    public void setData(User[] data) {
        this.data = data;
    }

    private String code;
    private User[] data;

    public UserResponse() {
    }
}
