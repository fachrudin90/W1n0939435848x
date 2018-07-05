package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefStatusKlien {


    private String id;
    private String stat_label;
    private String stat_code;
    private String stat_is_permittoloan;
    private String stat_is_banned;
    private String stat_rlc;
    private String permittoloan;
    private String banned;


    public RefStatusKlien() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStat_label() {
        return stat_label;
    }

    public void setStat_label(String stat_label) {
        this.stat_label = stat_label;
    }

    public String getStat_code() {
        return stat_code;
    }

    public void setStat_code(String stat_code) {
        this.stat_code = stat_code;
    }

    public String getStat_is_permittoloan() {
        return stat_is_permittoloan;
    }

    public void setStat_is_permittoloan(String stat_is_permittoloan) {
        this.stat_is_permittoloan = stat_is_permittoloan;
    }

    public String getStat_is_banned() {
        return stat_is_banned;
    }

    public void setStat_is_banned(String stat_is_banned) {
        this.stat_is_banned = stat_is_banned;
    }

    public String getStat_rlc() {
        return stat_rlc;
    }

    public void setStat_rlc(String stat_rlc) {
        this.stat_rlc = stat_rlc;
    }

    public String getPermittoloan() {
        return permittoloan;
    }

    public void setPermittoloan(String permittoloan) {
        this.permittoloan = permittoloan;
    }

    public String getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        this.banned = banned;
    }
}
