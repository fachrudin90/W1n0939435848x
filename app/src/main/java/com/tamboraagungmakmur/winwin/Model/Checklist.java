package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/11/2017.
 */

public class Checklist {

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

    public String getGroup_label() {
        return group_label;
    }

    public void setGroup_label(String group_label) {
        this.group_label = group_label;
    }

    public String getRlc() {
        return rlc;
    }

    public void setRlc(String rlc) {
        this.rlc = rlc;
    }

    private String id, label, group_label, rlc;

    public Checklist() {
    }
}
