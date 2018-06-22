package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/30/2017.
 */

public class InternalResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Internal[] getData() {
        return data;
    }

    public void setData(Internal[] data) {
        this.data = data;
    }

    private String code;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;
    private Internal[] data;

    public InternalResume getResume() {
        return resume;
    }

    public void setResume(InternalResume resume) {
        this.resume = resume;
    }

    private InternalResume resume;

    public InternalResponse() {
    }
}
