package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/31/2017.
 */

public class LogActivityResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LogActivity[] getData() {
        return data;
    }

    public void setData(LogActivity[] data) {
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private String total;
    private LogActivity[] data;

    public LogActivityResponse() {
    }
}
