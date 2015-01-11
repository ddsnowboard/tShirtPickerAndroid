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

    private final LinearLayout master;
    private final EditText box;
    private final TextView complaint;
    private final Context context;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private boolean complaining;
    private final static String TAG = "DateBox";
    private final LinearLayout frame;
    private TodayButton todayButton;

    public DateBox(Context ctx, LinearLayout master) {
        this.master = master;
        this.frame = new LinearLayout(ctx);
        this.context = ctx;
        this.complaining = false;
        this.box = new EditText(this.context);
        this.box.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        this.frame.setOrientation(LinearLayout.HORIZONTAL);
        this.master.addView(this.frame);
        this.frame.addView(this.box);
        this.todayButton = new TodayButton(ctx, this.box);
        this.frame.addView(this.todayButton);
        this.complaint = new TextView(this.context);
        this.master.addView(this.complaint);
        this.box.setHint(R.string.date_text);
        this.box.setInputType(InputType.TYPE_CLASS_DATETIME);
        this.complaint.setTextColor(Color.RED);
        this.box.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable text) {
                if (text.toString().equals("")) {
                    setComplaint(false, null);
                } else {
                    try {
                        Date date = format.parse(text.toString());
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
        });

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
            return format.parse(this.box.getText().toString());
        } catch (ParseException ex) {
            Log.e(TAG, "There was a problem parsing");
            return new Date();
        }
    }

    public void setDate(Date date) {
        this.box.setText(format.format(date));
    }

    public String getText() {
        return this.box.getText().toString();
    }

}
