package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends Activity {
//See if you can set up the database next time. 

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ArrayList<Shirt> shirts = new ArrayList<Shirt>();
        ShirtsHelper helper = new ShirtsHelper(this);
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
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            c.moveToNext();
        }
        ListView list = (ListView) findViewById(R.id.list);
        ArrayAdapter<Shirt> adapter = new ArrayAdapter<Shirt>(this, android.R.layout.simple_list_item_1, android.R.id.text1, shirts);
        list.setAdapter(adapter);
    }
}
