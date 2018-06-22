package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/9/2017.
 */

public class Task {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getNomor_pengajuan() {
        return nomor_pengajuan;
    }

    public void setNomor_pengajuan(String nomor_pengajuan) {
        this.nomor_pengajuan = nomor_pengajuan;
    }

    public String getNomor_pelanggan() {
        return nomor_pelanggan;
    }

    public void setNomor_pelanggan(String nomor_pelanggan) {
        this.nomor_pelanggan = nomor_pelanggan;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(String finished_at) {
        this.finished_at = finished_at;
    }

    public String getFinished_note() {
        return finished_note;
    }

    public void setFinished_note(String finished_note) {
        this.finished_note = finished_note;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public boolean is_finished() {
        return is_finished;
    }

    public void setIs_finished(boolean is_finished) {
        this.is_finished = is_finished;
    }

    public boolean is_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    private String id;
    private String task;
    private String nomor_pengajuan;
    private String nomor_pelanggan;
    private String deadline;
    private String finished_at;
    private String finished_note;
    private String created_at;
    private String created_by;
    private String status;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPengajuan_jatuh_tempo() {
        return pengajuan_jatuh_tempo;
    }

    public void setPengajuan_jatuh_tempo(String pengajuan_jatuh_tempo) {
        this.pengajuan_jatuh_tempo = pengajuan_jatuh_tempo;
    }

    private String pengajuan_jatuh_tempo;

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    private String nama_lengkap;
    private boolean is_finished, is_verified;

    public String getTipe_tugas() {
        return tipe_tugas;
    }

    public void setTipe_tugas(String tipe_tugas) {
        this.tipe_tugas = tipe_tugas;
    }

    private String tipe_tugas;

    public Task() {
    }
}
