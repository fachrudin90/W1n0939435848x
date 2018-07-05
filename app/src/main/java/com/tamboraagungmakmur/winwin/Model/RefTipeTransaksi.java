package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefTipeTransaksi {


    private String id;
    private String trxtype_label;
    private String trxtype_code;
    private String trxtype_penjelasan;
    private String trxtype_default_nominal_positif;
    private String trxtype_rlc;
    private String trxtype_created_by;
    private String trxtype_created_at;
    private String trxtype_updated_by;
    private String trxtype_updated_at;
    private String nominal;
    private String stat_rlc;


    public RefTipeTransaksi() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrxtype_label() {
        return trxtype_label;
    }

    public void setTrxtype_label(String trxtype_label) {
        this.trxtype_label = trxtype_label;
    }

    public String getTrxtype_code() {
        return trxtype_code;
    }

    public void setTrxtype_code(String trxtype_code) {
        this.trxtype_code = trxtype_code;
    }

    public String getTrxtype_penjelasan() {
        return trxtype_penjelasan;
    }

    public void setTrxtype_penjelasan(String trxtype_penjelasan) {
        this.trxtype_penjelasan = trxtype_penjelasan;
    }

    public String getTrxtype_default_nominal_positif() {
        return trxtype_default_nominal_positif;
    }

    public void setTrxtype_default_nominal_positif(String trxtype_default_nominal_positif) {
        this.trxtype_default_nominal_positif = trxtype_default_nominal_positif;
    }

    public String getTrxtype_rlc() {
        return trxtype_rlc;
    }

    public void setTrxtype_rlc(String trxtype_rlc) {
        this.trxtype_rlc = trxtype_rlc;
    }

    public String getTrxtype_created_by() {
        return trxtype_created_by;
    }

    public void setTrxtype_created_by(String trxtype_created_by) {
        this.trxtype_created_by = trxtype_created_by;
    }

    public String getTrxtype_created_at() {
        return trxtype_created_at;
    }

    public void setTrxtype_created_at(String trxtype_created_at) {
        this.trxtype_created_at = trxtype_created_at;
    }

    public String getTrxtype_updated_by() {
        return trxtype_updated_by;
    }

    public void setTrxtype_updated_by(String trxtype_updated_by) {
        this.trxtype_updated_by = trxtype_updated_by;
    }

    public String getTrxtype_updated_at() {
        return trxtype_updated_at;
    }

    public void setTrxtype_updated_at(String trxtype_updated_at) {
        this.trxtype_updated_at = trxtype_updated_at;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getStat_rlc() {
        return stat_rlc;
    }

    public void setStat_rlc(String stat_rlc) {
        this.stat_rlc = stat_rlc;
    }
}
