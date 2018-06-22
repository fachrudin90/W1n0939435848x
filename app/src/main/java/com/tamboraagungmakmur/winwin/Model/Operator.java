package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/31/2017.
 */

public class Operator {

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Operator1[] getStat() {
        return stat;
    }

    public void setStat(Operator1[] stat) {
        this.stat = stat;
    }

    private String nama;
    private Operator1[] stat;

    public Operator() {
    }
}
