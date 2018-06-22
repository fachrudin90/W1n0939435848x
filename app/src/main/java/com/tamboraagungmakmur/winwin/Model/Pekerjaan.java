package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/28/2017.
 */

public class Pekerjaan {

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

    public String getRlc() {
        return rlc;
    }

    public void setRlc(String rlc) {
        this.rlc = rlc;
    }

    public String getRlc_id() {
        return rlc_id;
    }

    public void setRlc_id(String rlc_id) {
        this.rlc_id = rlc_id;
    }

    public boolean isTolak() {
        return tolak;
    }

    public void setTolak(boolean tolak) {
        this.tolak = tolak;
    }

    private String id, label, rlc, rlc_id;
    private boolean tolak;

    public Pekerjaan() {
    }
}
