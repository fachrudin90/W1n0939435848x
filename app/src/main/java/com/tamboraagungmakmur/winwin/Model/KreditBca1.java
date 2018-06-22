package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/4/2017.
 */

public class KreditBca1 {
    public KreditBca1() {
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean is_finish() {
        return is_finish;
    }

    public void setIs_finish(boolean is_finish) {
        this.is_finish = is_finish;
    }

    public KreditBca[] getData() {
        return data;
    }

    public void setData(KreditBca[] data) {
        this.data = data;
    }

    private String title;
    private boolean is_finish;
    private KreditBca[] data;

}
