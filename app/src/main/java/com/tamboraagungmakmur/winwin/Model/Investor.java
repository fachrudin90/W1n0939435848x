package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 12/7/2017.
 */

public class Investor {

    public InvestorDoc[] getDocs() {
        return docs;
    }

    public void setDocs(InvestorDoc[] docs) {
        this.docs = docs;
    }

    private InvestorDoc[] docs;

    public String getInvestor_id() {
        return investor_id;
    }

    public void setInvestor_id(String investor_id) {
        this.investor_id = investor_id;
    }

    public String getInvestor_nomor_investor() {
        return investor_nomor_investor;
    }

    public void setInvestor_nomor_investor(String investor_nomor_investor) {
        this.investor_nomor_investor = investor_nomor_investor;
    }

    public String getInvestor_nama() {
        return investor_nama;
    }

    public void setInvestor_nama(String investor_nama) {
        this.investor_nama = investor_nama;
    }

    public String getInvestor_alamat() {
        return investor_alamat;
    }

    public void setInvestor_alamat(String investor_alamat) {
        this.investor_alamat = investor_alamat;
    }

    public String getInvestor_email() {
        return investor_email;
    }

    public void setInvestor_email(String investor_email) {
        this.investor_email = investor_email;
    }

    public String getInvestor_handphone() {
        return investor_handphone;
    }

    public void setInvestor_handphone(String investor_handphone) {
        this.investor_handphone = investor_handphone;
    }

    public String getInvestor_nomor_rekening() {
        return investor_nomor_rekening;
    }

    public void setInvestor_nomor_rekening(String investor_nomor_rekening) {
        this.investor_nomor_rekening = investor_nomor_rekening;
    }

    public String getInvestor_id_status() {
        return investor_id_status;
    }

    public void setInvestor_id_status(String investor_id_status) {
        this.investor_id_status = investor_id_status;
    }

    public String getInvestor_rlc() {
        return investor_rlc;
    }

    public void setInvestor_rlc(String investor_rlc) {
        this.investor_rlc = investor_rlc;
    }

    public String getInvestor_id_web() {
        return investor_id_web;
    }

    public void setInvestor_id_web(String investor_id_web) {
        this.investor_id_web = investor_id_web;
    }

    private String investor_id;
    private String investor_nomor_investor;
    private String investor_nama;
    private String investor_alamat;
    private String investor_email;
    private String investor_handphone;
    private String investor_nomor_rekening;
    private String investor_id_status;
    private String investor_rlc;
    private String investor_id_web;

    public String getRef_inv_stat_label() {
        return ref_inv_stat_label;
    }

    public void setRef_inv_stat_label(String ref_inv_stat_label) {
        this.ref_inv_stat_label = ref_inv_stat_label;
    }

    private String ref_inv_stat_label;

    public Investor() {
    }
}
