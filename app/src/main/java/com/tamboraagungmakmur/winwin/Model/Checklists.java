package com.tamboraagungmakmur.winwin.Model;

/**
 * Created by innan on 10/19/2017.
 */

public class Checklists {

    public String get_session() {
        return _session;
    }

    public void set_session(String _session) {
        this._session = _session;
    }

    public Checklist2[] getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist2[] checklist) {
        this.checklist = checklist;
    }

    private String _session;
    private Checklist2[] checklist;

    public Checklists() {
    }
}
