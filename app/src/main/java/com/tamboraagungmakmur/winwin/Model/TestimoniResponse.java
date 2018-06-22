package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 9/13/2017.
 */

public class TestimoniResponse {

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Testimoni[] getData() {
        return data;
    }

    public void setData(Testimoni[] data) {
        this.data = data;
    }

    private boolean error;
    private String code;
    private Testimoni[] data;

    public TestimoniResponse() {
    }
}
