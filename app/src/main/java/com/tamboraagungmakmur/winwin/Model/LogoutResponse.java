package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/2/2017.
 */

public class LogoutResponse {

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private boolean error;
    private String code, message;


    public LogoutResponse() {
    }
}
