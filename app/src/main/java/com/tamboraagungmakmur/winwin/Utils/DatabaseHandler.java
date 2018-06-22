package com.tamboraagungmakmur.winwin.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tamboraagungmakmur.winwin.Model.Akunting;
import com.tamboraagungmakmur.winwin.Model.Internal;
import com.tamboraagungmakmur.winwin.Model.InternalResume;
import com.tamboraagungmakmur.winwin.Model.InternalResume1;
import com.tamboraagungmakmur.winwin.Model.Operator1;
import com.tamboraagungmakmur.winwin.Model.Simulasi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by innan on 11/8/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "winwin";
    private static final String TABLE_INTERNAL = "internal";
    private static final String TABLE_AKUNTING = "akunting";
    private static final String TABLE_OPERATOR = "operator";
    private static final String TABLE_SIMULASI = "simulasi";
    private static final String TABLE_INTERNAL_RESUME = "internal_resume";
    private static final String TABLE_INTERNAL_RESUME1 = "internal_resume1";

//    klien, nilai_pinjam, durasi, total_kembali, margin, interest;
    private static final String KEY_ID = "no";
    private static final String KEY_KLIEN = "klien";
    private static final String KEY_PINJAM = "nilai_pinjam";
    private static final String KEY_DURASI = "durasi";
    private static final String KEY_KEMBALI = "total_kembali";
    private static final String KEY_MARGIN = "margin";
    private static final String KEY_INTEREST = "interest";
    private static final String KEY_STATUS = "status";
    private static final String KEY_TAHAP = "tahap";

    private static final String KEY_DATE = "date";
    private static final String KEY_NOPENG = "nomor_pengajuan";
    private static final String KEY_TIPE = "tipe";
    private static final String KEY_DEBIT = "debit";
    private static final String KEY_KREDIT = "kredit";
    private static final String KEY_SALDO = "saldo";

    private static final String KEY_NAMA = "nama";
    private static final String KEY_BARU = "baru";
    private static final String KEY_SELESAI = "selesai";
    private static final String KEY_SISA = "sisa";

    private static final String KEY_JANGKAPINJAM = "jangka_pinjam";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_PRINTPINJAM = "print_pinjam";


    private static final String KEY_PROFIT = "profit";
    private static final String KEY_PROFIT_UNPAID = "profit_unpaid";
    private static final String KEY_PROFIT_ADDITIONAL = "profit_additional";
    private static final String KEY_INCOME = "income";
    private static final String KEY_TURNOVER = "turnover";
    private static final String KEY_ROI = "roi";
    private static final String KEY_NEWCUST = "newcust";
    private static final String KEY_NEWCUST_DEFAULT = "newcust_default";
    private static final String KEY_FEEES = "fees";
    private static final String KEY_NETINCOME = "netincome";


    private static final String KEY_KOL1 = "kolom1";
    private static final String KEY_KOL2 = "kolom2";
    private static final String KEY_KOL3 = "kolom3";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AKUNTING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPERATOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIMULASI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNAL_RESUME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNAL_RESUME1);
        String CREATE_INTERNAL_TABLE = "CREATE TABLE " + TABLE_INTERNAL + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_KLIEN + " TEXT,"
                + KEY_PINJAM + " TEXT," + KEY_DURASI + " TEXT," + KEY_KEMBALI + " TEXT," + KEY_MARGIN + " TEXT," + KEY_INTEREST + " TEXT,"  + KEY_STATUS + " TEXT,"
                + KEY_TAHAP + " TEXT"+ ")";
        db.execSQL(CREATE_INTERNAL_TABLE);

        String CREATE_AKUNTING_TABLE = "CREATE TABLE " + TABLE_AKUNTING + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_KLIEN + " TEXT," + KEY_NOPENG + " TEXT," + KEY_TIPE + " TEXT," + KEY_DEBIT + " TEXT," + KEY_KREDIT + " TEXT," + KEY_SALDO + " TEXT" + ")";
        db.execSQL(CREATE_AKUNTING_TABLE);

        String CREATE_OPERATOR_TABLE = "CREATE TABLE " + TABLE_OPERATOR + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMA + " TEXT,"
                + KEY_DATE+ " TEXT," + KEY_BARU + " TEXT," + KEY_SELESAI + " TEXT," + KEY_SISA + " TEXT" + ")";
        db.execSQL(CREATE_OPERATOR_TABLE);

        String CREATE_SIMULASI_TABLE = "CREATE TABLE " + TABLE_SIMULASI + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PINJAM + " TEXT,"
                + KEY_JANGKAPINJAM + " TEXT," + KEY_TOTAL + " TEXT," + KEY_PRINTPINJAM + " TEXT" + ")";
        db.execSQL(CREATE_SIMULASI_TABLE);

        String CREATE_INTERNAL_RESUME_TABLE = "CREATE TABLE " + TABLE_INTERNAL_RESUME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PROFIT + " TEXT," + KEY_PROFIT_UNPAID + " TEXT," + KEY_PROFIT_ADDITIONAL + " TEXT," + KEY_INCOME + " TEXT,"
                + KEY_TURNOVER + " TEXT," + KEY_ROI + " TEXT," + KEY_NEWCUST + " TEXT," + KEY_NEWCUST_DEFAULT + " TEXT," + KEY_FEEES + " TEXT,"  + KEY_NETINCOME + " TEXT" + ")";
        db.execSQL(CREATE_INTERNAL_RESUME_TABLE);

        String CREATE_INTERNAL_RESUME1_TABLE = "CREATE TABLE " + TABLE_INTERNAL_RESUME1 + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_KOL1 + " TEXT," + KEY_KOL2 + " TEXT," + KEY_KOL3 + " TEXT" + ")";
        db.execSQL(CREATE_INTERNAL_RESUME1_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNAL_RESUME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERNAL_RESUME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AKUNTING);

        // Create tables again
        onCreate(db);
    }

    public void addInternal(Internal internal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KLIEN, internal.getKlien());
        values.put(KEY_PINJAM, internal.getNilai_pinjam());
        values.put(KEY_DURASI, internal.getDurasi());
        values.put(KEY_KEMBALI, internal.getTotal_kembali());
        values.put(KEY_MARGIN, internal.getMargin());
        values.put(KEY_INTEREST, internal.getInterest());
        values.put(KEY_STATUS, internal.getStatus());
        values.put(KEY_TAHAP, internal.getTahap());

        // Inserting Row
        db.insert(TABLE_INTERNAL, null, values);
        db.close(); // Closing database connection
    }

    public void addInternalResume(InternalResume internal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROFIT, internal.getProfit());
        values.put(KEY_PROFIT_UNPAID, internal.getProfit_unpaid());
        values.put(KEY_PROFIT_ADDITIONAL, internal.getProfit_additional());
        values.put(KEY_INCOME, internal.getIncome());
        values.put(KEY_TURNOVER, internal.getTurnover());
        values.put(KEY_ROI, internal.getRoi());
        values.put(KEY_NEWCUST, internal.getNewcust());
        values.put(KEY_NEWCUST_DEFAULT, internal.getNewcust_default());
        values.put(KEY_FEEES, internal.getFees());
        values.put(KEY_NETINCOME, internal.getNetincome());

        // Inserting Row
        db.insert(TABLE_INTERNAL_RESUME, null, values);
        db.close(); // Closing database connection
    }

    public void addInternalResume1(InternalResume1 internal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_KOL1, internal.getKol1());
        values.put(KEY_KOL2, internal.getKol2());
        values.put(KEY_KOL3, internal.getKol3());

        // Inserting Row
        db.insert(TABLE_INTERNAL_RESUME1, null, values);
        db.close(); // Closing database connection
    }

    public void addAkunting(Akunting internal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, internal.getDate());
        values.put(KEY_KLIEN, internal.getKlien());
        values.put(KEY_NOPENG, internal.getNomor_pengajuan());
        values.put(KEY_DEBIT, internal.getDebit());
        values.put(KEY_KREDIT, internal.getKredit());
        values.put(KEY_SALDO, internal.getSaldo());
        values.put(KEY_TIPE, internal.getTipe());

        // Inserting Row
        db.insert(TABLE_AKUNTING, null, values);
        db.close(); // Closing database connection
    }

    public void addOperator(Operator1 internal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, internal.getDate());
        values.put(KEY_BARU, internal.getBaru());
        values.put(KEY_SELESAI, internal.getSelesai());
        values.put(KEY_SISA, internal.getSisa());
        values.put(KEY_NAMA, internal.getNama());

        // Inserting Row
        db.insert(TABLE_OPERATOR, null, values);
        db.close(); // Closing database connection
    }

    public void addSimulasi(Simulasi internal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PINJAM, internal.getNilai_pinjam());
        values.put(KEY_JANGKAPINJAM, internal.getJangka_pinjam());
        values.put(KEY_PRINTPINJAM, internal.getPrint_pinjam());
        values.put(KEY_TOTAL, internal.getTotal());
        // Inserting Row
        db.insert(TABLE_SIMULASI, null, values);
        db.close(); // Closing database connection
    }

    public List<Internal> getAllInternals() {
        List<Internal> contactList = new ArrayList<Internal>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INTERNAL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Internal contact = new Internal();
                contact.setKlien(cursor.getString(1));
                contact.setNilai_pinjam(cursor.getString(2));
                contact.setDurasi(cursor.getString(3));
                contact.setTotal_kembali(cursor.getString(4));
                contact.setMargin(cursor.getString(5));
                contact.setInterest(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
}
