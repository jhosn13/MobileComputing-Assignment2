package com.example.jadhosn.app9;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JadHosn on 3/5/18.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DB_name = "app9.db";


    //Table Creation - depends on user input
    public static final String TABLE_PATIENT = "VAR";
    public static final String COLUMN_PATIENT_TIME = "time";
    public static final String COLUMN_PATIENT_X = "X";
    public static final String COLUMN_PATIENT_Y = "Y";
    public static final String COLUMN_PATIENT_Z = "Z";

    //SQL Query for creation
    private static final String SQL_CREATE_TABLE_PATIENT =
            "CREATE TABLE "+ TABLE_PATIENT+ "("
            +COLUMN_PATIENT_TIME + " TEXT NOT NULL, "
            +COLUMN_PATIENT_X +" FLOAT NOT NULL, "
            +COLUMN_PATIENT_Y+" FLOAT NOT NULL, "
            +COLUMN_PATIENT_Z+" FLOAT NOT NULL "
            +");";

    public DatabaseHelper(Context context)
    {
        super(context, DB_name, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PATIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_PATIENT+"");
        onCreate(db);
    }
    public boolean insertDate(String time, float x, float y, float z)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PATIENT_TIME, time);
        contentValues.put(COLUMN_PATIENT_X, x);
        contentValues.put(COLUMN_PATIENT_Y, y);
        contentValues.put(COLUMN_PATIENT_Z, z);

        long result = db.insert(TABLE_PATIENT,null,contentValues);
        if(result == -1)return false;
        else return true;

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_PATIENT,null);
        return result;
    }

    public Cursor getTime()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor result = db.rawQuery("select time from "+TABLE_PATIENT,null);
        return result;
    }

    public Cursor getX()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor result = db.rawQuery("select X from "+TABLE_PATIENT,null);
        return result;
    }

    public Cursor getY()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor result = db.rawQuery("select Y from "+TABLE_PATIENT,null);
        return result;
    }

    public Cursor getZ()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor result = db.rawQuery("select Z from "+TABLE_PATIENT,null);
        return result;
    }

}
