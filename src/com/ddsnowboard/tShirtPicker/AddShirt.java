/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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
    private DateBox dateBox;
    private EditText descriptionBox;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.add_shirt);
        this.descriptionBox = (EditText) findViewById(R.id.description);
        this.ratingBox = new RatingBox(this.getBaseContext(), (LinearLayout) findViewById(R.id.stars));
        this.dateBox = new DateBox(this, (LinearLayout) findViewById(R.id.last_worn));
    }

    public void submit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(R.string.date, this.dateBox.getText());
        intent.putExtra(R.string.description, this.descriptionBox.getText());
        intent.putExtra(R.string.rating, this.ratingBox.get());
        intent.putExtra(R.string.directive, R.string.add);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
