package com.greentea.locker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "PlaceName.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Database.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Database.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context) {
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create() {
        mDBHelper.onCreate(mDB);
    }

    public void close() {
        mDB.close();
    }

    // Insert DB
    public long insertColumn(String placeName, String chkList) {
        ContentValues values = new ContentValues();
        values.put(Database.CreateDB.PLACENAME, placeName);
        values.put(Database.CreateDB.CHKLIST, chkList);
        return mDB.insert(Database.CreateDB._TABLENAME0, null, values);
    }

    // Update DB
    public boolean updateColumn(long id, String placeName, String chkList) {
        ContentValues values = new ContentValues();
        values.put(Database.CreateDB.PLACENAME, placeName);
        values.put(Database.CreateDB.CHKLIST, chkList);
        return mDB.update(Database.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(Database.CreateDB._TABLENAME0, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id) {
        return mDB.delete(Database.CreateDB._TABLENAME0, "_id=" + id, null) > 0;
    }

    // Select DB
    public Cursor selectColumns() {
        return mDB.query(Database.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    // sort by column
    public Cursor sortColumn(String sort) {
        Cursor c = mDB.rawQuery("SELECT * FROM usertable ORDER BY " + sort + ";", null);
        return c;
    }
}