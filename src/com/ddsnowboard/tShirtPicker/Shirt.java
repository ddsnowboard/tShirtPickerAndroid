/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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
    String description;
    Date lastWorn;
    int rating;
    static final int PEAK_RATING = 5;

    public Shirt(int id, String description, String lastWorn, int rating) throws ParseException, Exception {
        this.id = id;
        this.description = description;
        try {
            this.lastWorn = DATE_FORMAT.parse(lastWorn);
        } catch (ParseException ex) {
            if (lastWorn.equals("")) {
                this.lastWorn = new Date(new Date().getTime() - DEFAULT_DIFFERENCE);
            } else {
                throw ex;
            }
        }
        if (0 < rating && PEAK_RATING >= rating) {
            this.rating = rating;
        } else {
            throw new Exception("Rating is not within range.");
        }
    }

    public void wearToday() {
        this.lastWorn = new Date();
        ContentValues newValues = new ContentValues();
        newValues.put("date", this.lastWorn.getSeconds());
        db.update(ShirtsHelper.DATABASE_NAME, newValues, "_id=?", new String[] {String.valueOf(this.id)});
    }

    public static void setDatabase(ShirtsHelper helper) {
        shirtsHelper = helper;
        db = shirtsHelper.getWritableDatabase();
    }
    @Override
    public String toString()
    {
        return this.description;
    }
}
