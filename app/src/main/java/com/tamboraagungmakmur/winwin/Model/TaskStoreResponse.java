package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/29/2017.
 */

public class TaskStoreResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String code, id;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private boolean error;

    public TaskStoreResponse() {
    }
}
