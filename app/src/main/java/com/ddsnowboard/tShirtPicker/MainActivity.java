package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
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
    // I have this around in case I need to access something from this in an inconvenient
    // place. I can call this up from anywhere and it is really easy.
    public static MainActivity singletonSelf;
    public ArrayAdapter<Shirt> adapter;
    private ShirtsHelper helper;
    private ListView ListViewForShirts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singletonSelf = this;
        setContentView(R.layout.main);
        // Initializes the images for the stars.
        Star.retrieveImages(this);
        shirts = new ArrayList<Shirt>();
        helper = new ShirtsHelper(this);
        // Initialize some things in the Shirt object.
        Shirt.setDatabase(helper);
        Shirt.setContext(this);
//        IF THESE AREN'T COMMENTED, THIS ISN'T READY TO SHIP, YOU HOPELESS JACKASS!
//        DEBUG_CLEAR();
//        DEBUG_ADD();
        readInShirtsFromDB();
        ListViewForShirts = (ListView) findViewById(R.id.list);
        registerForContextMenu(ListViewForShirts);
        // I'm not 100% sure what some of these inputs are for this. Maybe I should do some research...
        adapter = new ArrayAdapter<Shirt>(this, android.R.layout.simple_list_item_1, android.R.id.text1, shirts);
        ListViewForShirts.setAdapter(adapter);
        ListViewForShirts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                goToEditScreen(index);
            }
        });
    }

    private void readInShirtsFromDB() {
        Cursor c = helper.selectAll();
        c.moveToFirst();
        String currDescription;
        long currDate;
        int currRating;
        int currId;
        while (!c.isAfterLast()) {
            currId = c.getInt(ShirtsHelper.ID_COLUMN_NUMBER);
            currDescription = c.getString(ShirtsHelper.DESCRIPTION_COLUMN_NUMBER);
            currDate = c.getLong(ShirtsHelper.DATE_COLUMN_NUMBER);
            currRating = c.getInt(ShirtsHelper.RATING_COLUMN_NUMBER);
            try {
                shirts.add(new Shirt(currId, currDescription, new Date(currDate), currRating, true));
            } catch (Exception ex) {
                Log.e(TAG, "There was an error parsing the date from a shirt.");
            }
            c.moveToNext();
        }
    }

    public void addShirt(View v) {
        Intent intent = new Intent(getApplicationContext(), AddShirt.class);
        intent.putExtra(getApplicationContext().getString(R.string.creatingShirt), true);
        startActivity(intent);
    }

    private void goToEditScreen(int index) {
        Intent intent = new Intent(getApplicationContext(), AddShirt.class);
        intent.putExtra(getApplicationContext().getString(R.string.creatingShirt), false);
        intent.putExtra(getApplicationContext().getString(R.string.index), index);
        startActivity(intent);
    }

    public void pickShirt(View v) {
        if (MainActivity.shirts.isEmpty()) {
            Log.e(TAG, "Its empty");
            return;
        }
        // This just makes a list each shirt represented proportionally to its rating and the
        // time since it has been worn, and then picks from that list randomly to get a weighted
        // decision.
        ArrayList<Shirt> weightedList = new ArrayList<Shirt>();
        for (Shirt s : MainActivity.shirts)
            for (int i = 0; i < s.daysAgoWorn() * s.rating + 1; ++i)
                weightedList.add(s);
        final Shirt choice = weightedList.get(new Random().nextInt(weightedList.size()));
        new PickConfirmationBuilder(MainActivity.this, choice).create().show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // I know these do the same thing, but I am leaving it open to expansion.
        if (intent.getStringExtra(getApplicationContext().getString(R.string.directive)).equals(getApplicationContext().getString(R.string.add))) {
            this.adapter.notifyDataSetChanged();
        } else if (intent.getStringExtra(getString(R.string.directive)).equals(getString(R.string.edit))) {
            this.adapter.notifyDataSetChanged();
        }
    }
    // This lights up the shirt that is randomly picked if it is on the screen. I thought that there
    // should be some way of knowing that the shirt you picked was picked, so I put this in.
    public void blinkListItem(int i) {
        final long TIME_TO_BLINK = 300;
        final View view = this.ListViewForShirts.getChildAt(i - this.ListViewForShirts.getFirstVisiblePosition());
        try {
            view.setBackgroundColor(Color.YELLOW);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    view.setBackgroundColor(Color.BLACK);
                }
            }, TIME_TO_BLINK);
        } catch (NullPointerException e) {
        }
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, v, menuInfo);
        getMenuInflater().inflate(R.menu.long_click_menu, contextMenu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case (R.id.delete_shirt_menu_button):
                new DeleteConfirmationBuilder(MainActivity.this, shirts.get((int) info.id), info.position).create().show();
                return true;
            case (R.id.edit_shirt_menu_button):
                goToEditScreen((int) info.id);
                return true;
            case (R.id.pick_shirt_menu_button):
                new PickConfirmationBuilder(MainActivity.this, shirts.get((int) info.id)).create().show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    // I just have these methods for development purposes. I can call them and painlessly undo whatever
    // tinkering I did and replace a standard set to play with the next time.
    private void DEBUG_CLEAR() {
        helper.getWritableDatabase().delete(ShirtsHelper.DATABASE_NAME, null, null);
    }

    private void DEBUG_ADD() {
        try {
            new Shirt(Shirt.UNKNOWN_ID, "Lake Shirt", "2015-01-01", 2, false);
            new Shirt(Shirt.UNKNOWN_ID, "Sheldon's Spot", "2015-01-15", 3, false);
            new Shirt(Shirt.UNKNOWN_ID, "Black Keys Concert", "2015-02-01", 5, false);
            new Shirt(Shirt.UNKNOWN_ID, "Dawes", "2015-02-07", 5, false);
            new Shirt(Shirt.UNKNOWN_ID, "Salmon Cross Country Shirt", "2015-01-05", 4, false);
            new Shirt(Shirt.UNKNOWN_ID, "Dixon", "2015-02-01", 4, false);
            new Shirt(Shirt.UNKNOWN_ID, "Aim High", "2014-12-31", 3, false);
            new Shirt(Shirt.UNKNOWN_ID, "Priory Invitational 2013", "2014-12-30", 4, false);
            new Shirt(Shirt.UNKNOWN_ID, "Seaside", "2015-01-05", 4, false);
            new Shirt(Shirt.UNKNOWN_ID, "Aperture Science", "2015-02-04", 3, false);
            new Shirt(Shirt.UNKNOWN_ID, "Rush Concert", "2015-02-01", 5, false);
            new Shirt(Shirt.UNKNOWN_ID, "Field Day 2014", "2015-01-20", 3, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
