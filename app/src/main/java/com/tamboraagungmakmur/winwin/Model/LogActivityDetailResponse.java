package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/31/2017.
 */

public class LogActivityDetailResponse {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LogActivityDetail[] getData() {
        return data;
    }

    public void setData(LogActivityDetail[] data) {
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
    private LogActivityDetail[] data;

    public LogActivityDetailResponse() {
    }
}
