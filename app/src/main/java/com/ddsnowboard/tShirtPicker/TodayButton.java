/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ddsnowboard
 */
public class TodayButton extends Button {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private final EditText box;
    private final Context context;

    public TodayButton(Context context, final EditText box) {
        super(context);
        this.context = context;
        this.box = box;
        this.setText("Today");
        this.setSingleLine();
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                box.setText(format.format(new Date()));
            }
        });
    }

}
