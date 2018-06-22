package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Ayo Maju on 27/02/2018.
 * Updated by Muhammad Iqbal on 27/02/2018.
 */

public class ModelTransaksiPembayaran {
    String Tanggal,Keterangan,Nominal,Pengajuan,InfoSistem;

    public ModelTransaksiPembayaran() {
    }

    public ModelTransaksiPembayaran(String tanggal, String keterangan, String nominal, String pengajuan, String infoSistem) {
        Tanggal = tanggal;
        Keterangan = keterangan;
        Nominal = nominal;
        Pengajuan = pengajuan;
        InfoSistem = infoSistem;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getNominal() {
        return Nominal;
    }

    public void setNominal(String nominal) {
        Nominal = nominal;
    }

    public String getPengajuan() {
        return Pengajuan;
    }

    public void setPengajuan(String pengajuan) {
        Pengajuan = pengajuan;
    }

    public String getInfoSistem() {
        return InfoSistem;
    }

    public void setInfoSistem(String infoSistem) {
        InfoSistem = infoSistem;
    }
}
