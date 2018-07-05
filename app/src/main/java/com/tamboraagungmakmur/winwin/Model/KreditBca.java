package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/4/2017.
 */

public class KreditBca {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getMatch_loan() {
        return match_loan;
    }

    public void setMatch_loan(String match_loan) {
        this.match_loan = match_loan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean is_processable() {
        return is_processable;
    }

    public void setIs_processable(boolean is_processable) {
        this.is_processable = is_processable;
    }

    public String getTerkait() {
        return terkait;
    }

    public void setTerkait(String terkait) {
        this.terkait = terkait;
    }

    public String getPengajuan_id() {
        return pengajuan_id;
    }

    public void setPengajuan_id(String pengajuan_id) {
        this.pengajuan_id = pengajuan_id;
    }

    private boolean is_processable;
    private String id;
    private String date;
    private String note;
    private String amount;
    private String match_id;
    private String match_loan;
    private String status;
    private String terkait;
    private String pengajuan_id;

    public String getId_upload() {
        return id_upload;
    }

    public void setId_upload(String id_upload) {
        this.id_upload = id_upload;
    }

    private String id_upload;

    public KreditBca() {
    }
}
