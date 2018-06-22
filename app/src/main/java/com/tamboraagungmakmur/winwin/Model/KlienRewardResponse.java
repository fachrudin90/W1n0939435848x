package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/13/2017.
 */

public class KlienRewardResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public KlienReward[] getData() {
        return data;
    }

    public void setData(KlienReward[] data) {
        this.data = data;
    }

    private String code;
    private KlienReward[] data;

    public KlienRewardResponse() {
    }
}
