package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/4/2017.
 */

public class Perpanjangan1 {

    public String getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getNote() {
        return note;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    private String start;
    private String end;
    private String note;
    private String created_at;
    private String created_by;

    public String getPengajuan() {
        return pengajuan;
    }

    public void setPengajuan(String pengajuan) {
        this.pengajuan = pengajuan;
    }

    private String pengajuan;

    public Perpanjangan1() {
    }
}
