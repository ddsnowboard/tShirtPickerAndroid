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

    private static final String TAG = "AddShirt";
    // GUI Elements; cf. add_shirt.xml
    // The top two are objects that I wrote and are drawn with
    // Java "on the fly", the bottom two are in the xml and
    // are native to Android.
    private RatingBox ratingBox;
    private DateBox dateBox;
    private EditText descriptionBox;
    private Button deleteButton;

    private Intent intent;
    // Whether this instance is creating a shirt or editing an existing one.
    // This affects whether we update or insert the database at the end.
    private boolean creating;
    private Shirt curr;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        intent = getIntent();
        setContentView(R.layout.add_shirt);
        ratingBox = new RatingBox(this.getBaseContext(), (LinearLayout) findViewById(R.id.stars));
        dateBox = new DateBox(this.getBaseContext(), (LinearLayout) findViewById(R.id.last_worn));
        deleteButton = (Button) findViewById(R.id.delete_button);
        descriptionBox = (EditText) findViewById(R.id.description);
        creating = intent.getBooleanExtra(getString(R.string.creatingShirt), false);
        // If we're not creating a shirt, put the default values in all the boxes.
        if (!creating) {
            curr = MainActivity.shirts.get(intent.getIntExtra(getString(R.string.index), -1));
            descriptionBox.setText(curr.description);
            dateBox.setDate(curr.lastWorn);
            ratingBox.setRating(curr.rating);
            setUpDeleteButton(deleteButton);
        }
    }

    // Standard input checking
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

    // I don't exactly know I set the color this way instead of in xml. Hmmm...
    public void setUpDeleteButton(Button button) {
        button.setVisibility(View.VISIBLE);
        // Found the below on https://stackoverflow.com/a/5327404/2570117. It keeps the edges darker than
        // the middle so it still looks like a button.
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    public void submit() {
        // All DB functionality is handled in the Shirt object, so I don't have to play with it
        // here.
        if (creating) {
            MainActivity.shirts.add(new Shirt(0, this.descriptionBox.getText().toString(), this.dateBox.getText(), this.ratingBox.get(), false));
        } else {
            curr.edit(this.descriptionBox.getText().toString(), this.dateBox.getText(), this.ratingBox.get());
        }
        sendToMain();
    }

    public void askDeleteShirt(View view) {
        if (curr != null) {
            final DeleteConfirmationBuilder builder = new DeleteConfirmationBuilder(this, MainActivity.singletonSelf, curr, intent.getIntExtra(getString(R.string.index), -1));
            // This is how I have it send my back to main. I re-set the yes button to do it as well
            // the normal things to do on yes.
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DeleteConfirmationBuilder.onYes(builder.getCurr(), builder.getPosition());
                    sendToMain();
                }
            });
            builder.create().show();
        }
    }

    private void sendToMain() {
        Intent outboundIntent = new Intent(this, MainActivity.class);
        outboundIntent.putExtra(getString(R.string.directive), getString(R.string.edit));
        // I don't know why these are here, but I have a feeling there's a really good
        // reason for it that I forgot. I might have accidentally replaced it with this.finish()
        // though.
//        outboundIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        outboundIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(outboundIntent);
        this.finish();
    }
}
