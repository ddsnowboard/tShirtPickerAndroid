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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author ddsnowboard
 */
public class AddShirt extends Activity {

    /**
     * Called when the activity is first created.
     */
    private RatingBox ratingBox;
    private DateBox dateBox;
    private EditText descriptionBox;
    private Intent intent;
    private boolean creating;
    private Shirt curr;
    private Button deleteButton;
    private static final String TAG = "AddShirt";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        intent = getIntent();
        setContentView(R.layout.add_shirt);
        this.descriptionBox = (EditText) findViewById(R.id.description);
        this.ratingBox = new RatingBox(this.getBaseContext(), (LinearLayout) findViewById(R.id.stars));
        this.dateBox = new DateBox(this, (LinearLayout) findViewById(R.id.last_worn));
        this.deleteButton = (Button) findViewById(R.id.delete_button);
        this.creating = !intent.getBooleanExtra(getString(R.string.filled), false);
        if (!creating) {
            curr = MainActivity.shirts.get(intent.getIntExtra(getString(R.string.index), -1));
            this.descriptionBox.setText(curr.description);
            this.dateBox.setDate(curr.lastWorn);
            this.ratingBox.setRating(curr.rating);
            setUpDeleteButton(deleteButton);
        }
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

    public void setUpDeleteButton(Button button) {
        button.setVisibility(View.VISIBLE);
        // Found the below on https://stackoverflow.com/a/5327404/2570117. It keeps the edges darker than
        // the middle so it still looks like a button.
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    public void submit() {
        if (creating) {
            MainActivity.shirts.add(new Shirt(0, this.descriptionBox.getText().toString(), this.dateBox.getText(), this.ratingBox.get(), false));
        } else {
            curr.edit(this.descriptionBox.getText().toString(), this.dateBox.getText(), this.ratingBox.get());
        }
        sendToMain();
    }

    public void deleteShirt(View view) {
        if (curr != null) {
            curr.delete();
            MainActivity.shirts.remove(intent.getIntExtra(getString(R.string.index), -1));
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent outboundIntent = new Intent(this, MainActivity.class);
        outboundIntent.putExtra(getString(R.string.directive), getString(R.string.edit));
        outboundIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        outboundIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(outboundIntent);
        this.finish();
    }
}
