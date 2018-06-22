package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/10/2017.
 */

public class EmailTemplateResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EmailTemplate[] getData() {
        return data;
    }

    public void setData(EmailTemplate[] data) {
        this.data = data;
    }

    private String code;
    private EmailTemplate[] data;

    public EmailTemplateResponse() {
    }
}
