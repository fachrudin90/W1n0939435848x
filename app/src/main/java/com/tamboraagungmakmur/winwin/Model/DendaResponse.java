package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/21/2017.
 */

public class DendaResponse {

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

    public String getPengajuan() {
        return pengajuan;
    }

    public void setPengajuan(String pengajuan) {
        this.pengajuan = pengajuan;
    }

    private String pengajuan;
    private Data data;

    public DendaResponse() {
    }

    public String getId() {
        return data.getId();
    }

    public String getAmount() {
        return data.getAmount();
    }

    public String getNote() {
        return data.getNote();
    }

    public String getCreated_at() {
        return data.getCreated_at();
    }

    public String getCreated_by() {
        return data.getCreated_by();
    }

    private class Data {
        public String getId() {
            return id;
        }

        public String getAmount() {
            return amount;
        }

        public String getNote() {
            return note;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        private String id, amount, note, created_at, created_by;

        public Data() {
        }
    }

}
