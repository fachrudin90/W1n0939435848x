package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/30/2017.
 */

public class ReferralResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Referral[] getData() {
        return data;
    }

    public void setData(Referral[] data) {
        this.data = data;
    }

    private String code;
    private Referral[] data;

    public ReferralResponse() {
    }
}
