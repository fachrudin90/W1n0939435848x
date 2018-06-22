package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 12/8/2017.
 */

public class InvestorDoc {

    public String getInv_doc_id() {
        return inv_doc_id;
    }

    public void setInv_doc_id(String inv_doc_id) {
        this.inv_doc_id = inv_doc_id;
    }

    public String getInv_doc_inv_id() {
        return inv_doc_inv_id;
    }

    public void setInv_doc_inv_id(String inv_doc_inv_id) {
        this.inv_doc_inv_id = inv_doc_inv_id;
    }

    public String getJnsfile_label() {
        return jnsfile_label;
    }

    public void setJnsfile_label(String jnsfile_label) {
        this.jnsfile_label = jnsfile_label;
    }

    public String getJnsfile_code() {
        return jnsfile_code;
    }

    public void setJnsfile_code(String jnsfile_code) {
        this.jnsfile_code = jnsfile_code;
    }

    public String getInv_doc_file() {
        return inv_doc_file;
    }

    public void setInv_doc_file(String inv_doc_file) {
        this.inv_doc_file = inv_doc_file;
    }

    public String getInv_doc_keterangan() {
        return inv_doc_keterangan;
    }

    public void setInv_doc_keterangan(String inv_doc_keterangan) {
        this.inv_doc_keterangan = inv_doc_keterangan;
    }

    private String inv_doc_id;
    private String inv_doc_inv_id;
    private String jnsfile_label;
    private String jnsfile_code;
    private String inv_doc_file;
    private String inv_doc_keterangan;

    public String getInvestor_nomor_investor() {
        return investor_nomor_investor;
    }

    public void setInvestor_nomor_investor(String investor_nomor_investor) {
        this.investor_nomor_investor = investor_nomor_investor;
    }

    private String investor_nomor_investor;

    public String getInv_doc_type_id() {
        return inv_doc_type_id;
    }

    public void setInv_doc_type_id(String inv_doc_type_id) {
        this.inv_doc_type_id = inv_doc_type_id;
    }

    private String inv_doc_type_id;

    public InvestorDoc() {
    }
}
