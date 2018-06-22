package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 8/21/2017.
 */

public class PembayaranResponse {

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
    private Data data;

    public String getPengajuan() {
        return pengajuan;
    }

    public void setPengajuan(String pengajuan) {
        this.pengajuan = pengajuan;
    }

    private String pengajuan;

    public PembayaranResponse() {
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

    public String getMedia() {
        return data.getMedia();
    }

    public String getJenis() {
        return data.getJenis();
    }

    public String getCreatedAt() {
        return data.getCreated_at();
    }

    public String getCreatedBy() {
        return data.getCreated_by();
    }

    private class Data {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getMedia() {
            return media;
        }

        public void setMedia(String media) {
            this.media = media;
        }

        public String getJenis() {
            return jenis;
        }

        public void setJenis(String jenis) {
            this.jenis = jenis;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        private String amount;
        private String note;
        private String media;
        private String jenis;
        private String created_at;
        private String created_by;

        public Data() {
        }
    }

}
