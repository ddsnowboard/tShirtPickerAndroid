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
    //     This class is the object equivalent of the sql row for each shirt. It is way easier to work with
//    in Java than reading the table all the time.
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    //         This is the default time you wore the shirt (one week). It puts this if it doesn't
//    know when you last wore it.
    static final long DEFAULT_DIFFERENCE = 7 * 24 * 60 * 60 * 1000;
    static final String TAG = "Shirt";
    static final int PEAK_RATING = 5;
    static final int UNKNOWN_ID = -1;
    private static final long MILLISECONDS_IN_DAY = 86400000;
    static ShirtsHelper shirtsHelper;
    static SQLiteDatabase db;
    static Context context;
    int id;
    String description;
    Date lastWorn;
    int rating;

    public Shirt(int id, String description, String lastWorn, int rating, boolean inDB) {
        this.description = description;
        this.id = id;
//        If you can parse the date, do it. Otherwise, it was worn a week ago (or whatever the time in
//        DEFAULT_DIFFERENCE is.
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
//             If the ID is unknown, then look at the table, find the last shirt, and give this the
//            id after that.
            if (this.id == UNKNOWN_ID) {
                Cursor c = shirtsHelper.selectAll();
                c.moveToLast();
                try {
                    this.id = c.getInt(0) + 1;
                } catch (CursorIndexOutOfBoundsException ex) {
                    // If the list is empty, give it an id 1 and move on.
                    this.id = 1;
                }
            }
            ContentValues values = new ContentValues();
            values.put(ShirtsHelper.DESCRIPTION, this.description);
            values.put(ShirtsHelper.DATE, this.lastWorn.getTime());
            values.put(ShirtsHelper.RATING, this.rating);
            Log.e(Shirt.TAG, "Inserted shirt at row: " + String.valueOf(db.insert(ShirtsHelper.DATABASE_NAME, null, values)));
        }
    }

    // Assumes that the Shirt is the the database if you don't tell it otherwise.
    public Shirt(int id, String description, String lastWorn, int rating) throws Exception {
        this(id, description, lastWorn, rating, true);
    }
    // It can also take a Date object as the date, as opposed to a string.
    public Shirt(int id, String description, Date lastWorn, int rating, boolean inDB) {
        this(id, description, "", rating, inDB);
        this.lastWorn = lastWorn;
    }
    // This sets the database for all the shirts to access.
    public static void setDatabase(ShirtsHelper helper) {
        shirtsHelper = helper;
        db = shirtsHelper.getWritableDatabase();
    }

    public static void setContext(Context ctx) {
        context = ctx;
    }

    public void edit(String description, String lastWorn, int rating) {
        this.description = description;
        try {
            this.lastWorn = DATE_FORMAT.parse(lastWorn);
        } catch (ParseException e) {
            Log.e(TAG, "The date of " + this.description + " was unparseable");
            this.lastWorn = new Date(new Date().getTime()-DEFAULT_DIFFERENCE);
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
        newValues.put(ShirtsHelper.DATE, this.lastWorn.getTime());
        db.update(ShirtsHelper.DATABASE_NAME, newValues, "_id=?", new String[]{String.valueOf(this.id)});
    }

    public void delete() {
        db.delete(ShirtsHelper.DATABASE_NAME, "_id = ?", new String[]{String.valueOf(this.id)});

    }

    public int daysAgoWorn() {
        int out = 0;
        out = (int) ((new Date().getTime() - this.lastWorn.getTime()) / MILLISECONDS_IN_DAY);
        return out;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
