package com.ddsnowboard.tShirtPicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ddsnowboard on 2/7/2015.
 */
public class DeleteConfirmationBuilder extends AlertDialog.Builder {
    public static final String TAG = "DeleteConfirmationBuilder";
    private static MainActivity context;
    private Shirt curr;
    private int position;

    public DeleteConfirmationBuilder(final Context context, final MainActivity mainActivity, final Shirt shirt, final int position) {
        super(context);
        this.context = mainActivity;
        this.setCurr(shirt);
        this.setPosition(position);
        this.setMessage("Are you sure you want to delete " + shirt.description + "?");
        this.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        this.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // DON'T CHANGE THIS. CHANGE onYes() TO CHANGE THIS.
                DeleteConfirmationBuilder.onYes(getCurr(), getPosition());
            }
        });
    }

    public DeleteConfirmationBuilder(final MainActivity mainActivity, final Shirt shirt, final int position) {
        this(mainActivity, mainActivity, shirt, position);
    }

    public static void onYes(Shirt shirt, int position) {
        shirt.delete();
        MainActivity.shirts.remove(position);
        context.adapter.notifyDataSetChanged();
    }

    public Shirt getCurr() {
        return curr;
    }

    public void setCurr(Shirt curr) {
        this.curr = curr;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
