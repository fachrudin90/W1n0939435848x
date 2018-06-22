package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/10/2017.
 */

public class EmailTemplate {

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

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }

    private String id, label, konten;

    public EmailTemplate() {
    }
}
