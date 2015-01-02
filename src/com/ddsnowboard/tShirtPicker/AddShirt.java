/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 *
 * @author ddsnowboard
 */
public class AddShirt extends Activity {

    /**
     * Called when the activity is first created.
     *
     * @param icicle
     */
    private RatingBox ratingBox;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.add_shirt);
        this.ratingBox = new RatingBox(this.getBaseContext(), (LinearLayout)findViewById(R.id.stars));
    }

    public void submit(View view) {
        Toast.makeText(this, ((EditText) findViewById(R.id.description)).getText(), Toast.LENGTH_LONG).show();
    }
}
