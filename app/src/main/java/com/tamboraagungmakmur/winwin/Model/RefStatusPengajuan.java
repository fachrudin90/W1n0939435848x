package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusPengajuan {


    private String id;
    private String ajustat_label;
    private String ajustat_code;
    private String ajustat_is_approve;
    private String wajib;
    private String stat_rlc;


    public RefStatusPengajuan() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAjustat_label() {
        return ajustat_label;
    }

    public void setAjustat_label(String ajustat_label) {
        this.ajustat_label = ajustat_label;
    }

    public String getAjustat_code() {
        return ajustat_code;
    }

    public void setAjustat_code(String ajustat_code) {
        this.ajustat_code = ajustat_code;
    }

    public String getAjustat_is_approve() {
        return ajustat_is_approve;
    }

    public void setAjustat_is_approve(String ajustat_is_approve) {
        this.ajustat_is_approve = ajustat_is_approve;
    }

    public String getWajib() {
        return wajib;
    }

    public void setWajib(String wajib) {
        this.wajib = wajib;
    }

    public String getStat_rlc() {
        return stat_rlc;
    }

    public void setStat_rlc(String stat_rlc) {
        this.stat_rlc = stat_rlc;
    }
}
