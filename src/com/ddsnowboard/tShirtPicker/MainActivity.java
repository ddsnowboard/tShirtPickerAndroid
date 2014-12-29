package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ArrayList<Shirt> shirts = new ArrayList<Shirt>();
        ListView list = (ListView) findViewById(R.id.list);

    }
}
