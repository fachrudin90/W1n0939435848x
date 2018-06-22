package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/30/2017.
 */

public class Simulasi {

    public String getNilai_pinjam() {
        return nilai_pinjam;
    }

    public void setNilai_pinjam(String nilai_pinjam) {
        this.nilai_pinjam = nilai_pinjam;
    }

    public String getJangka_pinjam() {
        return jangka_pinjam;
    }

    public void setJangka_pinjam(String jangka_pinjam) {
        this.jangka_pinjam = jangka_pinjam;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPrint_pinjam() {
        return print_pinjam;
    }

    public void setPrint_pinjam(String print_pinjam) {
        this.print_pinjam = print_pinjam;
    }

    private String nilai_pinjam, jangka_pinjam, total, print_pinjam;

    public Simulasi() {
    }
}
