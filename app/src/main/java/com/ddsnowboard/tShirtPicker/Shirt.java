/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ddsnowboard
 */
public class Shirt {

    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    static final long DEFAULT_DIFFERENCE = 7 * 24 * 60 * 60 * 1000; // A week
    static final String TAG = "Shirt.class";
    static final int PEAK_RATING = 5;
    static ShirtsHelper shirtsHelper;
    static SQLiteDatabase db;
    static Context CONTEXT;
    int id;
    String description;
    Date lastWorn;
    int rating;

    public Shirt(int id, String description, String lastWorn, int rating, boolean inDB) {
        this.description = description;
        this.id = id;
        try {
            this.lastWorn = DATE_FORMAT.parse(lastWorn);
        } catch (ParseException ex) {
            if (lastWorn.equals("")) {
                this.lastWorn = new Date(new Date().getTime() - DEFAULT_DIFFERENCE);
            } else {
                Log.e(TAG, "The date of " + this.description + " was unparseable");
            }
        }
        if (0 < rating && PEAK_RATING >= rating) {
            this.rating = rating;
        } else {
            Log.e(TAG, "The rating of " + this.description + " was out of range");
        }
        if (!inDB) {
            if (this.id == 0) {
                Cursor c = shirtsHelper.selectAll();
                c.moveToLast();
                try {
                    this.id = c.getInt(0) + 1;
                } catch (CursorIndexOutOfBoundsException ex) {
                    this.id = 1;
                }
            }
            ContentValues values = new ContentValues();
            values.put(ShirtsHelper.DESCRIPTION, this.description);
            values.put(ShirtsHelper.DATE, this.lastWorn.getTime());
            values.put(ShirtsHelper.RATING, this.rating);
            db.insert(ShirtsHelper.DATABASE_NAME, null, values);
        }
    }

    public Shirt(int id, String description, String lastWorn, int rating) throws Exception {
        this(id, description, lastWorn, rating, true);
    }

    public Shirt(int id, String description, Date lastWorn, int rating, boolean inDB) {
        this(id, description, "", rating, inDB);
        this.lastWorn = lastWorn;
    }

    public static void setDatabase(ShirtsHelper helper) {
        shirtsHelper = helper;
        db = shirtsHelper.getWritableDatabase();
    }

    public static void setContext(Context ctx) {
        CONTEXT = ctx;
    }

    public void edit(String description, String lastWorn, int rating) {
        this.description = description;
        try {
            this.lastWorn = DATE_FORMAT.parse(lastWorn);
        } catch (ParseException e) {
            Log.e(TAG, "The date of " + this.description + " was unparseable");
        }
        this.rating = rating;
        ContentValues cv = new ContentValues();
        cv.put(ShirtsHelper.DESCRIPTION, this.description);
        cv.put(ShirtsHelper.DATE, this.lastWorn.getTime());
        cv.put(ShirtsHelper.RATING, this.rating);
        db.update(ShirtsHelper.DATABASE_NAME, cv, "_id=?", new String[]{String.valueOf(this.id)});
    }

    public void wearToday() {
        this.lastWorn = new Date();
        ContentValues newValues = new ContentValues();
        newValues.put(ShirtsHelper.DATE, this.lastWorn.getSeconds());
        db.update(ShirtsHelper.DATABASE_NAME, newValues, "_id=?", new String[]{String.valueOf(this.id)});
    }

    public void delete() {
        db.delete(shirtsHelper.DATABASE_NAME, "_id = ?", new String[]{String.valueOf(this.id)});

    }

    @Override
    public String toString() {
        return this.description;
    }

}
