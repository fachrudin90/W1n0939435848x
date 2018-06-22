package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/16/2017.
 */

public class HistoriTahapan {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTahap() {
        return tahap;
    }

    public void setTahap(String tahap) {
        this.tahap = tahap;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private String date, tahap, user, note;

    public HistoriTahapan() {
    }
}
