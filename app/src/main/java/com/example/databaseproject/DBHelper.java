package com.example.databaseproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER = "create table User (" +
            "id integer primary key autoincrement," +
            "name text," +
            "key_n text," +
            "key_e text," +
            "key_d text)";

    public static final String CREATE_MSG = "create table msg (" +
            "id integer," +
            "`name` text," +
            "`text` text," +
            "`time` text)";

    public DBHelper(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_MSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        db.execSQL("drop table if exists msg");
        onCreate(db);
    }
}
