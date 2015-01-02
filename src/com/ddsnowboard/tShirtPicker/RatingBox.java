/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.Context;
import android.widget.LinearLayout;

/**
 *
 * @author ddsnowboard
 */
public class RatingBox {

    private static final int DEFAULT_STARS = 3;
    private static final int NUM_OF_STARS = 5;
    private Star[] stars = new Star[NUM_OF_STARS];
    private LinearLayout master;
    private Context context;

    public RatingBox(Context context, LinearLayout master) {
        this.context = context;
        this.master = master;
        for (int i = 0; i < NUM_OF_STARS; i++) {
            this.stars[i] = new Star(this.context, this, false);
            this.master.addView(this.stars[i]);
        }
        for (int i = 0; i < DEFAULT_STARS; i++) {
            this.stars[i].on();
        }
    }
}
