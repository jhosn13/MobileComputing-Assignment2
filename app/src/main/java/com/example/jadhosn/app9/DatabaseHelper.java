package com.example.jadhosn.app9;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JadHosn on 3/5/18.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DB_name = "app9.db";

    public DatabaseHelper(Context context)
    {
        super(context, DB_name, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL(Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
