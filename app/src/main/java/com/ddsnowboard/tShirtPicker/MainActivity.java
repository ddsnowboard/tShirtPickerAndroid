package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "MainActivity";
    public static ArrayList<Shirt> shirts;
    private ArrayAdapter<Shirt> adapter;
    private ShirtsHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Star.retrieveImages(this);
        shirts = new ArrayList<Shirt>();
        helper = new ShirtsHelper(this);
        Shirt.setDatabase(helper);
        Shirt.setContext(this);
//        DEBUG_CLEAR();
        Cursor c = helper.selectAll();
        c.moveToFirst();
        String currDescription;
        int currDate;
        int currRating;
        int currId;
        while (!c.isAfterLast()) {
            currId = c.getInt(0);
            currDescription = c.getString(1);
            currDate = c.getInt(2);
            currRating = c.getInt(3);
            try {
                shirts.add(new Shirt(currId, currDescription, new Date(currDate), currRating, true));
            } catch (Exception ex) {
                Log.e(TAG, "There was an error parsing the date from a shirt.");
            }
            c.moveToNext();
        }
        ListView list = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<Shirt>(this, android.R.layout.simple_list_item_1, android.R.id.text1, shirts);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AddShirt.class);
                intent.putExtra(getApplicationContext().getString(R.string.filled), true);
                intent.putExtra(getApplicationContext().getString(R.string.index), i);
                startActivity(intent);
            }
        });
    }

    public void addShirt(View v) {
        Intent intent = new Intent(this, AddShirt.class);
        intent.putExtra(getApplicationContext().getString(R.string.filled), false);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getStringExtra(getApplicationContext().getString(R.string.directive)).equals(getApplicationContext().getString(R.string.add))) {
            this.adapter.notifyDataSetChanged();
        } else if (intent.getStringExtra(getString(R.string.directive)).equals(getString(R.string.edit))) {
            this.adapter.notifyDataSetChanged();
        }
    }

    private void DEBUG_CLEAR() {
        helper.getWritableDatabase().delete(ShirtsHelper.DATABASE_NAME, null, null);
    }
}
