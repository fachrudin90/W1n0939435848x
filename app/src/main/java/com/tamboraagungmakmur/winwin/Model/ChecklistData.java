package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/15/2017.
 */

public class ChecklistData {

    public Checklist1[] getPribadi() {
        return Pribadi;
    }

    public void setPribadi(Checklist1[] pribadi) {
        Pribadi = pribadi;
    }

    public Checklist1[] getPerusahaan() {
        return Perusahaan;
    }

    public void setPerusahaan(Checklist1[] perusahaan) {
        Perusahaan = perusahaan;
    }

    public Checklist1[] getTelfon() {
        return Telfon;
    }

    public void setTelfon(Checklist1[] telfon) {
        Telfon = telfon;
    }

    public Checklist1[] getSurvei() {
        return Survei;
    }

    public void setSurvei(Checklist1[] survei) {
        Survei = survei;
    }

    private Checklist1[] Pribadi, Perusahaan, Telfon, Survei;

    public ChecklistData() {
    }
}
