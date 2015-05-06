/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ddsnowboard
 */
public class DateBox {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static String TAG = "DateBox";
    // The LinlearLayout we're in.
    private final LinearLayout master;
    private final EditText box;
    // We call this up if we have to yell at the user.
    private final TextView complaint;
    private final Context context;
    // The LinearLayout we make for this to contain everything.
    private final LinearLayout frame;
    private boolean complaining;
    private TodayButton todayButton;

    public DateBox(Context ctx, LinearLayout master) {
        this.master = master;
        frame = new LinearLayout(ctx);
        context = ctx;
        complaining = false;
        box = new EditText(context);
        box.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        frame.setOrientation(LinearLayout.HORIZONTAL);
        this.master.addView(this.frame);
        frame.addView(this.box);
        todayButton = new TodayButton(context, this.box);
        frame.addView(this.todayButton);
        complaint = new TextView(this.context);
        this.master.addView(this.complaint);
        box.setHint(R.string.date_box_hint);
        box.setInputType(InputType.TYPE_CLASS_DATETIME);
        complaint.setTextColor(Color.RED);
        box.addTextChangedListener(getInputChecker());
    }

    private void setComplaint(boolean complaining, String complaint) {
        this.complaining = complaining;
        if (this.complaining) {
            this.complaint.setText(complaint);
        } else {
            this.complaint.setText("");
        }

    }

    public boolean isComplaining() {

        return this.complaining;
    }

    public Date getDate() {
        try {
            return DATE_FORMAT.parse(this.box.getText().toString());
        } catch (ParseException ex) {
            Log.e(TAG, "There was a problem parsing");
            return new Date();
        }
    }

    public void setDate(Date date) {
        this.box.setText(DATE_FORMAT.format(date));
    }

    public String getText() {
        return this.box.getText().toString();
    }

    private TextWatcher getInputChecker() {
        return new TextWatcher() {

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable text) {
                if (text.toString().equals("")) {
                    setComplaint(false, null);
                } else {
                    try {
                        Date date = DATE_FORMAT.parse(text.toString());
                        if (date.after(new Date())) {
                            setComplaint(true, "That date is after today.");
                        } else {
                            setComplaint(false, null);
                        }
                    } catch (ParseException ex) {
                        setComplaint(true, "That date isn't formatted properly (YYYY-MM-DD)");
                    }
                }
            }
        };
    }
}
