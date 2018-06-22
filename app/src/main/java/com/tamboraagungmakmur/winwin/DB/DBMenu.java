package com.tamboraagungmakmur.winwin.DB;

/**
 * Created by Tambora on 04/01/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBMenu extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_menu";
    private static final String TABLE_MENU = "tb_menu";
    private static final String KEY_IDMENU = "idmenu";
    private static final String KEY_IDCATEGORY = "idcategory";
    private static final String KEY_IDSUB = "idsub";
    private static final String KEY_TITLE = "title";

    public DBMenu(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MENU + "("
                + KEY_IDMENU + " INTEGER,"
                + KEY_IDCATEGORY + " INTEGER,"
                + KEY_IDSUB+ " INTEGER,"
                + KEY_TITLE + " TEXT" +")";

        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        // Creating tables again
        onCreate(db);
    }


//    // Adding new dataContact
//    public void addAbsen(DBModel dbModel) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_PIN, dbModel.getPin());
//        values.put(KEY_DTIME, dbModel.getDtime());
//
//        // Inserting Row
//        db.insert(TABLE_ABSEN, null, values);
//        db.close(); // Closing database connection
//    }
//
//    // Getting dataContacts Count
//    public int getAbsenCount() {
//        int count = 0;
//        String countQuery = "SELECT  * FROM " + TABLE_ABSEN;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        if (cursor != null && !cursor.isClosed()) {
//            count = cursor.getCount();
//            cursor.close();
//        }
//
//        cursor.close();
//        db.close();
//        // return count
//        return count;
//    }
//
//    public int deleteAllAbsen() {
//        int count = 0;
//        String countQuery = "DELETE FROM " + TABLE_ABSEN;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//
//        if (cursor != null && !cursor.isClosed()) {
//            count = cursor.getCount();
//            cursor.close();
//        }
//
//        cursor.close();
//        db.close();
//        // return count
//        return count;
//    }
//
//    public String getAllAbsen() {
//
//        // Select All Query
//        String all = "";
//        String selectQuery = "SELECT  * FROM " + TABLE_ABSEN;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//
//                all = all + "('" + cursor.getString(0) + "','" + cursor.getString(1) + "'),";
//            } while (cursor.moveToNext());
//        }
//
//        if (!all.equals("")) {
//            all = all.replaceFirst(".$", "");
//        }
//
//        cursor.close();
//        db.close();
//        // return contact list
//        return all;
//    }

}
