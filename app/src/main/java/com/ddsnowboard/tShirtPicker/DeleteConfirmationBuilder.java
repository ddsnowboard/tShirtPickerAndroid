package com.ddsnowboard.tShirtPicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

/**
 * Created by ddsnowboard on 2/7/2015.
 */
public class DeleteConfirmationBuilder extends AlertDialog.Builder {
    public static final String TAG = "DeleteConfirmationBuilder";
    private Shirt curr;
    private int position;

    public DeleteConfirmationBuilder(final Context context, final Shirt shirt, final int position) {
        super(context);
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
                DeleteConfirmationBuilder.onYes(shirt, position);
            }
        });
    }

    public DeleteConfirmationBuilder(final MainActivity context, final Shirt shirt, final int position) {
        this((Context) context, shirt, position);
        this.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onYes(getCurr(), getPosition());
                context.adapter.notifyDataSetChanged();
            }
        });
    }

    public static void onYes(Shirt shirt, int position) {
        shirt.delete();
        MainActivity.shirts.remove(position);
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
