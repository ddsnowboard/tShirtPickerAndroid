package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends Activity {
//See if you can set up the database next time. 
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ArrayList<Shirt> shirts = new ArrayList<Shirt>();
        try {
            shirts.add(new Shirt(1, "TestShirt", "2014-12-11", 3));
        } catch(Exception e) {
            
        }
        ListView list = (ListView) findViewById(R.id.list);
        ArrayAdapter<Shirt> adapter = new ArrayAdapter<Shirt>(this, android.R.layout.simple_list_item_1,android.R.id.text1, shirts);
        list.setAdapter(adapter);
    }
}
