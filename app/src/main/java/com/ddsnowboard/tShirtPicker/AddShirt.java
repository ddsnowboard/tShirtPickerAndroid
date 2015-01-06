/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author ddsnowboard
 */
public class AddShirt extends Activity {

    /**
     * Called when the activity is first created.
     *
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

    public void checkBoxes(View view) {
        if (this.descriptionBox.length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.emptyDescriptionWarning);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    submit();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            builder.create().show();
        } else {
            submit();
        }
    }

    public void submit() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(this.getString(R.string.date), this.dateBox.getText());
        intent.putExtra(this.getString(R.string.description), this.descriptionBox.getText().toString());
        intent.putExtra(this.getString(R.string.rating), this.ratingBox.get());
        intent.putExtra(this.getString(R.string.directive), this.getString(R.string.add));
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
