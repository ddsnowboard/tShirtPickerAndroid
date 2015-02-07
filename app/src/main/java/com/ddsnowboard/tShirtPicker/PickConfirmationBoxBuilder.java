package com.ddsnowboard.tShirtPicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ddsnowboard on 1/22/2015.
 */
public class PickConfirmationBoxBuilder extends AlertDialog.Builder {
    public PickConfirmationBoxBuilder(final MainActivity ctx, final Shirt choice) {
        super(ctx);
        this.setMessage("Do you want to wear " + choice.description + " today?");
        this.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choice.wearToday();
                ctx.blinkListItem(ctx.adapter.getPosition(choice));
            }
        });
        this.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
    }
}
