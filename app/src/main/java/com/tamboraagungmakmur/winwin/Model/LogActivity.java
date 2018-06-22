package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/31/2017.
 */

public class LogActivity {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLog_date() {
        return log_date;
    }

    public void setLog_date(String log_date) {
        this.log_date = log_date;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getStat_total_rekomendasi_setujui() {
        return stat_total_rekomendasi_setujui;
    }

    public void setStat_total_rekomendasi_setujui(String stat_total_rekomendasi_setujui) {
        this.stat_total_rekomendasi_setujui = stat_total_rekomendasi_setujui;
    }

    public String getStat_total_rekomendasi_tolak() {
        return stat_total_rekomendasi_tolak;
    }

    public void setStat_total_rekomendasi_tolak(String stat_total_rekomendasi_tolak) {
        this.stat_total_rekomendasi_tolak = stat_total_rekomendasi_tolak;
    }

    public String getStat_total_lihat_profil_klien() {
        return stat_total_lihat_profil_klien;
    }

    public void setStat_total_lihat_profil_klien(String stat_total_lihat_profil_klien) {
        this.stat_total_lihat_profil_klien = stat_total_lihat_profil_klien;
    }

    public String getStat_total_proses_pengajuan() {
        return stat_total_proses_pengajuan;
    }

    public void setStat_total_proses_pengajuan(String stat_total_proses_pengajuan) {
        this.stat_total_proses_pengajuan = stat_total_proses_pengajuan;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(String logout_time) {
        this.logout_time = logout_time;
    }

    public String getLogout_by() {
        return logout_by;
    }

    public void setLogout_by(String logout_by) {
        this.logout_by = logout_by;
    }

    private String id;
    private String log_date;
    private String login_time;
    private String stat_total_rekomendasi_setujui;
    private String stat_total_rekomendasi_tolak;
    private String stat_total_lihat_profil_klien;
    private String stat_total_proses_pengajuan;
    private String logout_time;
    private String logout_by;

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    private String diff;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;

    public LogActivity() {
    }
}
