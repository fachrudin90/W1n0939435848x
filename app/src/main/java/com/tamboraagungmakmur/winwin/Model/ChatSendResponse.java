package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/24/2017.
 */

public class ChatSendResponse {

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private boolean error;

    public ChatSendResponse() {
    }
}
