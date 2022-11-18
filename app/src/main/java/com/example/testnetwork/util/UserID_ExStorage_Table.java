package com.example.testnetwork.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserID_ExStorage_Table extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userID_LocalStorage";
    private static final String TABLE_CONTACTS = "tontacts";
    private static final String KEY_NAME = "name";
    private static final String KEY_ACCOUNT = "account";
    //private static final String KEY_PASSWORD = "password";
    //private static final String KEY_GENDER = "gender";
    //private static final String KEY_AGE = "age";

    public UserID_ExStorage_Table(Context context) {
        //创建数据库
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_NAME + "INTEGER PRIMARY KEY," + KEY_NAME + "TEXT,"
                + KEY_ACCOUNT + "TEXT)";
        //创建表名
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void addUSerID(UserID_ExStorage_Method contact) {
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public void addUserID(UserID_ExStorage_Method userID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, userID.getName());
        values.put(KEY_ACCOUNT, userID.getAccount());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public UserID_ExStorage_Method getUserID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                KEY_NAME, KEY_NAME, KEY_ACCOUNT}, KEY_NAME + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        UserID_ExStorage_Method contact = new UserID_ExStorage_Method(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        return contact;
    }
}
