package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by Tambora on 04/03/2017.
 */
public class NotifiactionModel {

    private String img, title, times;

    public NotifiactionModel() {
    }

    public NotifiactionModel(String img, String title, String times) {
        this.img = img;
        this.title = title;
        this.times = times;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
