package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 11/27/2017.
 */

public class RefRlc {


    private String id;
    private String short_desc;
    private String viewed_by_admin;
    private String viewed_by_superuser;
    private String referenced;
    private String send_notifications;
    private String created_by;
    private String created_at;
    private String updated_by;
    private String updated_at;
    private String admin;
    private String superuser;
    private String notif;

    public RefRlc() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getViewed_by_admin() {
        return viewed_by_admin;
    }

    public void setViewed_by_admin(String viewed_by_admin) {
        this.viewed_by_admin = viewed_by_admin;
    }

    public String getViewed_by_superuser() {
        return viewed_by_superuser;
    }

    public void setViewed_by_superuser(String viewed_by_superuser) {
        this.viewed_by_superuser = viewed_by_superuser;
    }

    public String getReferenced() {
        return referenced;
    }

    public void setReferenced(String referenced) {
        this.referenced = referenced;
    }

    public String getSend_notifications() {
        return send_notifications;
    }

    public void setSend_notifications(String send_notifications) {
        this.send_notifications = send_notifications;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getSuperuser() {
        return superuser;
    }

    public void setSuperuser(String superuser) {
        this.superuser = superuser;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }
}
