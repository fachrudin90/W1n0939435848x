package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/25/2017.
 */

public class Pengguna {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_karyawan() {
        return nama_karyawan;
    }

    public void setNama_karyawan(String nama_karyawan) {
        this.nama_karyawan = nama_karyawan;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean is_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(boolean is_disabled) {
        this.is_disabled = is_disabled;
    }

    private String id;
    private String nama_karyawan;
    private String roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private boolean is_disabled;

    public boolean is_ro() {
        return is_ro;
    }

    public void setIs_ro(boolean is_ro) {
        this.is_ro = is_ro;
    }

    private boolean is_ro;

    public Pengguna() {
    }
}
