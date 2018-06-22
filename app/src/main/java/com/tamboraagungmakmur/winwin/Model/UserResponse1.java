package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/7/2017.
 */

public class UserResponse1 {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getId() {
        return data.getId();
    }

    public String getNama_karyawan() {
        return data.getNama_karyawan();
    }

    public Roles[] getRoles() {
        return data.getRoles();
    }

    private String code;
    private Data data;

    private class Data {
        public String getId() {
            return id;
        }

        public String getNama_karyawan() {
            return nama_karyawan;
        }

        public Roles[] getRoles() {
            return roles;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setNama_karyawan(String nama_karyawan) {
            this.nama_karyawan = nama_karyawan;
        }

        public void setRoles(Roles[] roles) {
            this.roles = roles;
        }

        private String id, nama_karyawan;
        private Roles[] roles;

        public Data() {
        }
    }

    public UserResponse1() {
    }
}
