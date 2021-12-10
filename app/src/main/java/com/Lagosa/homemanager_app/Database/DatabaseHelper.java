package com.Lagosa.homemanager_app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "userInfo";
    private static final String USER_ID_COL = "userid";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID_COL + " varchar(36) NOT NULL UNIQUE)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUserId(UUID userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID_COL,userId.toString());

        db.insert(TABLE_NAME,null,contentValues);
    }

    public Cursor getUserId(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT userid FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(sql,null);
        return data;
    }
}
