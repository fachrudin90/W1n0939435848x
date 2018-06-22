package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/2/2017.
 */

public class LoginResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    private String code;
    private String session_id;

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    private String roles;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    private String member_id;

    public String getSession_expire() {
        return session_expire;
    }

    public void setSession_expire(String session_expire) {
        this.session_expire = session_expire;
    }

    private String session_expire;

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    private String jabatan;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private boolean error;

    public LoginResponse() {
    }
}
