package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "MainActivity";
    public static ArrayList<Shirt> shirts;
    private ArrayAdapter<Shirt> adapter;
    private ShirtsHelper helper;
    private ListView list;

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
        long currDate;
        int currRating;
        int currId;
        while (!c.isAfterLast()) {
            currId = c.getInt(0);
            currDescription = c.getString(1);
            currDate = c.getLong(2);
            currRating = c.getInt(3);
            try {
                shirts.add(new Shirt(currId, currDescription, new Date(currDate), currRating, true));
            } catch (Exception ex) {
                Log.e(TAG, "There was an error parsing the date from a shirt.");
            }
            c.moveToNext();
        }
        list = (ListView) findViewById(R.id.list);
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

    public void pickShirt(View v) {
        if (MainActivity.shirts.isEmpty()) {
            Log.e(TAG, "Its empty");
            return;
        }
        ArrayList<Shirt> weightedList = new ArrayList<Shirt>();
        for (Shirt s : MainActivity.shirts)
            for (int i = 0; i < s.daysAgoWorn() * s.rating + 1; ++i)
                weightedList.add(s);
        final Shirt choice = weightedList.get(new Random().nextInt(weightedList.size()));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] sides = getResources().getStringArray(R.array.pick_shirt_message);
        builder.setMessage(sides[0] + choice.description + sides[1]);
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choice.wearToday();
                blinkListItem(shirts.indexOf(choice));
            }
        });
        builder.create().show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getStringExtra(getApplicationContext().getString(R.string.directive)).equals(getApplicationContext().getString(R.string.add))) {
            this.adapter.notifyDataSetChanged();
        } else if (intent.getStringExtra(getString(R.string.directive)).equals(getString(R.string.edit))) {
            this.adapter.notifyDataSetChanged();
        }
    }

    private void blinkListItem(int i) {
        final long TIME_TO_BLINK = 300;
        final View view = this.list.getChildAt(i + this.list.getFirstVisiblePosition());
        view.setBackgroundColor(Color.YELLOW);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                view.setBackgroundColor(Color.BLACK);
            }
        }, TIME_TO_BLINK);
    }

    private void DEBUG_CLEAR() {
        helper.getWritableDatabase().delete(ShirtsHelper.DATABASE_NAME, null, null);
    }
}
