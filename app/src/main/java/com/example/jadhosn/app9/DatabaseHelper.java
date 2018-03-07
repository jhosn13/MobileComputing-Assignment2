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

    /*
    String SQL_CREATE_TABLE_PATIENT = "CREATE TABLE TrialPatient ("
            +COLUMN_PATIENT_TIME + " TEXT NOT NULL, "
            +COLUMN_PATIENT_X +" FLOAT NOT NULL, "
            +COLUMN_PATIENT_Y+" FLOAT NOT NULL, "
            +COLUMN_PATIENT_Z+" FLOAT NOT NULL "
            +") ;";
    */

    //Table Creation - depends on user input
    String TABLE_PATIENT = "TrialPatient";
    public static final String COLUMN_PATIENT_TIME = "time";
    public static final String COLUMN_PATIENT_X = "X";
    public static final String COLUMN_PATIENT_Y = "Y";
    public static final String COLUMN_PATIENT_Z = "Z";

    public DatabaseHelper(Context context)
    {
        super(context, DB_name, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_PATIENT = "CREATE TABLE TrialPatient ("
                +COLUMN_PATIENT_TIME + " TEXT NOT NULL, "
                +COLUMN_PATIENT_X +" FLOAT NOT NULL, "
                +COLUMN_PATIENT_Y+" FLOAT NOT NULL, "
                +COLUMN_PATIENT_Z+" FLOAT NOT NULL "
                +") ;";
        db.execSQL(SQL_CREATE_TABLE_PATIENT);
    }

     public void addTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        //SQL Query for creation
        String SQL_CREATE_TABLE_PATIENT = "CREATE TABLE "+name+ " ("
                        +COLUMN_PATIENT_TIME + " TEXT NOT NULL, "
                        +COLUMN_PATIENT_X +" FLOAT NOT NULL, "
                        +COLUMN_PATIENT_Y+" FLOAT NOT NULL, "
                        +COLUMN_PATIENT_Z+" FLOAT NOT NULL "
                        +") ;";
        db.execSQL(SQL_CREATE_TABLE_PATIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_PATIENT+"");
        onCreate(db);
    }

    public boolean insertData(String name, String time, double x, double y, double z)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PATIENT_TIME, time);
        contentValues.put(COLUMN_PATIENT_X, x);
        contentValues.put(COLUMN_PATIENT_Y, y);
        contentValues.put(COLUMN_PATIENT_Z, z);

        long result = db.insert(name,null,contentValues);
        if(result == -1)return false;
        else return true;

    }

    public Cursor getAllData(String name)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+name,null);
        return result;
    }


}
