/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * @author ddsnowboard
 */
public class ShirtsHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "shirts";
    public final static int DATABASE_VERSION = 1;
    public final static String DATABASE_CREATE = "create table " + DATABASE_NAME + " (_id integer primary key autoincrement, description text, date integer, rating integer not null)";
    public final static String DESCRIPTION = "description";
    public final static String DATE = "date";
    public final static String RATING = "rating";
    public final static String TAG = "ShirtsHelper";

    public ShirtsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqld) {
        sqld.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int oldVersion, int newVersion) {
        // IMPLEMENT THIS IF YOU EVER NEED TO UPDATE THE DATABASE. 
    }

    public Cursor selectAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(DATABASE_NAME, null, null, null, null, null, null);
    }

}
