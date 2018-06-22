package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/31/2017.
 */

public class OperatorResponse {

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

    private String code;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String count;
    private Data data;

    public OperatorResponse() {
    }

    public String getTotal_baru() {
        return data.getTotal_baru();
    }

    public String getTotal_selesai() {
        return data.getTotal_selesai();
    }

    public String getTotal_sisa() {
        return data.getTotal_sisa();
    }

    public Operator[] getOperators() {
        return data.getOperators();
    }

    private class Data {

        public String getTotal_baru() {
            return total_baru;
        }

        public String getTotal_selesai() {
            return total_selesai;
        }

        public String getTotal_sisa() {
            return total_sisa;
        }

        public Operator[] getOperators() {
            return operators;
        }

        public void setTotal_baru(String total_baru) {
            this.total_baru = total_baru;
        }

        public void setTotal_selesai(String total_selesai) {
            this.total_selesai = total_selesai;
        }

        public void setTotal_sisa(String total_sisa) {
            this.total_sisa = total_sisa;
        }

        public void setOperators(Operator[] operators) {
            this.operators = operators;
        }

        private String total_baru, total_selesai, total_sisa;
        private Operator[] operators;

        public Data() {
        }
    }

}
