package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    private static final String TAG = "MainActivity";
    private static ArrayList<Shirt> shirts;
    private ArrayAdapter<Shirt> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Star.retrieveImages(this);
        shirts = new ArrayList<Shirt>();
        ShirtsHelper helper = new ShirtsHelper(this);
        helper.getWritableDatabase().delete(ShirtsHelper.DATABASE_NAME, null, null);
        Shirt.setDatabase(helper);
        Cursor c = helper.selectAll();
        c.moveToFirst();
        String currDescription;
        String currDate;
        int currRating;
        int currId;
        int DEBUG = c.getCount();
        while (!c.isAfterLast()) {
            currId = c.getInt(0);
            currDescription = c.getString(1);
            currDate = c.getString(2);
            currRating = c.getInt(3);
            try {
                shirts.add(new Shirt(currId, currDescription, currDate, currRating));
            } catch (Exception ex) {
                Log.e(TAG, "There was an error parsing the date from a shirt.");
            }
            c.moveToNext();
        }
        ListView list = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<Shirt>(this, android.R.layout.simple_list_item_1, android.R.id.text1, shirts);
        list.setAdapter(adapter);
    }

    public void addShirt(View v) {
        Intent intent = new Intent(this, AddShirt.class);
        startActivity(intent);
    }
}
