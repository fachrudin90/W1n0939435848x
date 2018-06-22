package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 02/03/2017.
 */
public class ArchivePencarianModel {

    private String pengajuan, bank, namaNorek, jmlTrf, status;

    public ArchivePencarianModel() {
    }

    public ArchivePencarianModel(String pengajuan, String bank, String namaNorek, String jmlTrf, String status) {
        this.pengajuan = pengajuan;
        this.bank = bank;
        this.namaNorek = namaNorek;
        this.jmlTrf = jmlTrf;
        this.status = status;
    }

    public String getPengajuan() {
        return pengajuan;
    }

    public void setPengajuan(String pengajuan) {
        this.pengajuan = pengajuan;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNamaNorek() {
        return namaNorek;
    }

    public void setNamaNorek(String namaNorek) {
        this.namaNorek = namaNorek;
    }

    public String getJmlTrf() {
        return jmlTrf;
    }

    public void setJmlTrf(String jmlTrf) {
        this.jmlTrf = jmlTrf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
