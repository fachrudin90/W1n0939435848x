package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefBank {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank_label() {
        return bank_label;
    }

    public void setBank_label(String bank_label) {
        this.bank_label = bank_label;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getRlc() {
        return rlc;
    }

    public void setRlc(String rlc) {
        this.rlc = rlc;
    }

    private String id;
    private String bank_label;
    private String bank_code;
    private String rlc;

    public String getId_rlc() {
        return id_rlc;
    }

    public void setId_rlc(String id_rlc) {
        this.id_rlc = id_rlc;
    }

    private String id_rlc;

    public RefBank() {
    }
}
