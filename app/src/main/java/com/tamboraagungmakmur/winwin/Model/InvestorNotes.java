package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 12/7/2017.
 */

public class InvestorNotes {


    private String note_id;
    private String note_id_investor;
    private String note_catatan;
    private String note_created_at;
    private String kar_namalengkap;

    public InvestorNotes() {
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getNote_id_investor() {
        return note_id_investor;
    }

    public void setNote_id_investor(String note_id_investor) {
        this.note_id_investor = note_id_investor;
    }

    public String getNote_catatan() {
        return note_catatan;
    }

    public void setNote_catatan(String note_catatan) {
        this.note_catatan = note_catatan;
    }

    public String getNote_created_at() {
        return note_created_at;
    }

    public void setNote_created_at(String note_created_at) {
        this.note_created_at = note_created_at;
    }

    public String getKar_namalengkap() {
        return kar_namalengkap;
    }

    public void setKar_namalengkap(String kar_namalengkap) {
        this.kar_namalengkap = kar_namalengkap;
    }
}
