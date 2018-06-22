package com.tamboraagungmakmur.winwin.voip.Utils;

/**
 * Created by Web on 16/04/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {

    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "com.tamboraagungmakmur.winwinadmin.log";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_IDUSER = "iduser";

    public static final String KEY_NAMA = "name";

    public static final String KEY_IDSESSION = "idsession";

    public static final String KEY_INCALL = "incall";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String iduser, String nama) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_IDUSER, iduser);

        editor.putString(KEY_NAMA, nama);


        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status

        boolean stLogin = true;

        if (!this.isLoggedIn()) {

            stLogin = false;
        }

        return stLogin;

    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getIduser() {
        return pref.getString(KEY_IDUSER, null);
    }

    public String getNama() {
        return pref.getString(KEY_NAMA, null);
    }

    public String getIdsession() {
        return pref.getString(KEY_IDSESSION, null);
    }

    public boolean getInCall() {
        return pref.getBoolean(KEY_INCALL, false);
    }


    public void setNama(String nama) {
        editor.putString(KEY_NAMA, nama);
        editor.commit();
    }

    public void setIduser(String iduser) {
        editor.putString(KEY_IDUSER, iduser);
        editor.commit();
    }

    public void setIdsession(String idsession) {
        editor.putString(KEY_IDSESSION, idsession);
        editor.commit();
    }

    public void setInCall(boolean incall) {
        editor.putBoolean(KEY_INCALL, incall);
        editor.commit();
    }
}