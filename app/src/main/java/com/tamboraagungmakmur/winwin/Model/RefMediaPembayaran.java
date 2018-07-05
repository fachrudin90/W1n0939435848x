package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefMediaPembayaran {


    private String id;
    private String label;
    private String id_pendapatan;
    private String id_biaya;
    private String flag;


    public RefMediaPembayaran() {
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

    public String getId_pendapatan() {
        return id_pendapatan;
    }

    public void setId_pendapatan(String id_pendapatan) {
        this.id_pendapatan = id_pendapatan;
    }

    public String getId_biaya() {
        return id_biaya;
    }

    public void setId_biaya(String id_biaya) {
        this.id_biaya = id_biaya;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
