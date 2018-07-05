package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefJenisPembayaran {


    private String id;
    private String label;
    private String code;
    private String rlc;


    public RefJenisPembayaran() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRlc() {
        return rlc;
    }

    public void setRlc(String rlc) {
        this.rlc = rlc;
    }
}
