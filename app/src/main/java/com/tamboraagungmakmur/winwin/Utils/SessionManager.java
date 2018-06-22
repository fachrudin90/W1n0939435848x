package com.tamboraagungmakmur.winwin.Utils;

/**
 * Created by Web on 16/04/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.volley.VolleyError;
import com.tamboraagungmakmur.winwin.LoginActivity;
import com.tamboraagungmakmur.winwin.R;

public class SessionManager {

    SharedPreferences pref;

    Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "com.tamboraagungmakmur.winwin.log";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_IDADMIN = "idadmin";

    public static final String KEY_USERNAME = "username";
    public static final String SESSION_ID = "";

    public static final String KEY_IDMENU = "idmenu";

    public static final String KEY_PAGE = "page";

    public static final String KEY_TITLE = "title";

    public static final String JABATAN = "";

    public static final boolean ANALIS = false;
    public static final boolean PELUNASAN = false;
    public static final boolean PERPANJANGAN = false;
    public static final boolean DENDA = false;
    public static final boolean COLLECTION = false;
    public static final boolean PENCAIRAN = false;
    public static final boolean PERSETUJUAN = false;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String idadmin, String username, String sessionId) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_IDADMIN, idadmin);

        editor.putString(KEY_USERNAME, username);
        editor.putString(SESSION_ID, sessionId);
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

    public void setIdMenu(int idMenu) {

        editor.putInt(KEY_IDMENU, idMenu);
        editor.commit();
    }

    public void setPage(int page) {

        editor.putInt(KEY_PAGE, page);
        editor.commit();
    }

    public void setTitle(String title) {

        editor.putString(KEY_TITLE, title);
        editor.commit();
    }

    public String getUsername() {

        return pref.getString(KEY_USERNAME, null);
    }

    public String getIdadmin() {
        return pref.getString(KEY_IDADMIN, null);
    }

    public String getSessionId() {
        return pref.getString(SESSION_ID, null);
    }

    public void setJABATAN(String jabatan) {
        editor.putString("Jabatan", jabatan);
        editor.commit();
    }

    public String getJABATAN() {
        return pref.getString("Jabatan", null);
    }

    public void setANALIS(boolean bool) {
        editor.putBoolean("Analis", bool);
        editor.commit();
    }

    public void setDENDA(boolean bool) {
        editor.putBoolean("Denda", bool);
        editor.commit();
    }

    public void setPERSETUJUAN(boolean bool) {
        editor.putBoolean("Persetujuan", bool);
        editor.commit();
    }

    public void setPELUNASAN(boolean bool) {
        editor.putBoolean("Pelunasan", bool);
        editor.commit();
    }

    public void setPENCAIRAN(boolean bool) {
        editor.putBoolean("Pencairan", bool);
        editor.commit();
    }

    public void setPERPANJANGAN(boolean bool) {
        editor.putBoolean("Perpanjangan", bool);
        editor.commit();
    }

    public void setCOLLECTION(boolean bool) {
        editor.putBoolean("Collection", bool);
        editor.commit();
    }

    public boolean isANALIS() {
        return pref.getBoolean("Analis", false);
    }

    public boolean isDENDA() {
        return pref.getBoolean("Denda", false);
    }

    public boolean isPERSETUJUAN() {
        return pref.getBoolean("Persetujuan", false);
    }

    public boolean isPELUNASAN() {
        return pref.getBoolean("Pelunasan", false);
    }

    public boolean isPENCAIRAN() {
        return pref.getBoolean("Pencairan", false);
    }

    public boolean isPERPANJANGAN() {
        return pref.getBoolean("Perpanjangan", false);
    }

    public boolean isCOLLECTION() {
        return pref.getBoolean("Collection", false);
    }

    public int getIdMenu() {

        return pref.getInt(KEY_IDMENU, 0);

    }

    public int getPage() {

        return pref.getInt(KEY_PAGE, 0);
    }

    public String getTitle() {

        return pref.getString(KEY_TITLE, null);
    }

    public void errorHandling(VolleyError error) {

        if(error != null) {
            if (error.networkResponse != null) {

                if (error.networkResponse.statusCode == 404) {
                    logoutUser();
                    GlobalToast.ShowToast(_context, _context.getString(R.string.session_expired));
                    Intent intent = new Intent(_context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    _context.startActivity(intent);
                } else {
                    GlobalToast.ShowToast(_context, _context.getString(R.string.gagal_server));
                }
            } else {
                GlobalToast.ShowToast(_context, _context.getString(R.string.gagal_server));

            }
        }else{
            GlobalToast.ShowToast(_context, _context.getString(R.string.gagal_server));

        }

    }

}