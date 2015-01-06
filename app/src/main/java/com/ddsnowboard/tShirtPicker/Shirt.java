/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ddsnowboard
 */
public class Shirt {

    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("F");
    static final long DEFAULT_DIFFERENCE = 7 * 24 * 60 * 60 * 1000; // A week
    static ShirtsHelper shirtsHelper;
    static SQLiteDatabase db;
    int id;
    static final String TAG = "Shirt.class";
    String description;
    Date lastWorn;
    int rating;
    static final int PEAK_RATING = 5;

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
            values.put(ShirtsHelper.DATE, this.lastWorn.getSeconds());
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

    public void wearToday() {
        this.lastWorn = new Date();
        ContentValues newValues = new ContentValues();
        newValues.put("date", this.lastWorn.getSeconds());
        db.update(ShirtsHelper.DATABASE_NAME, newValues, "_id=?", new String[]{String.valueOf(this.id)});
    }

    public static void setDatabase(ShirtsHelper helper) {
        shirtsHelper = helper;
        db = shirtsHelper.getWritableDatabase();
    }

    @Override
    public String toString() {
        return this.description;
    }
}
