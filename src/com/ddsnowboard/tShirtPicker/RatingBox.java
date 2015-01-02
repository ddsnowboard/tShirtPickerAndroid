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
    private final Star[] stars;
    private final LinearLayout master;
    private final Context context;
    private int number;

    public RatingBox(Context context, LinearLayout master) {
        this.stars = new Star[NUM_OF_STARS];
        this.context = context;
        this.master = master;
        this.number = DEFAULT_STARS;
        for (int i = 0; i < NUM_OF_STARS; i++) {
            this.stars[i] = new Star(this.context, this, false, i);
            this.master.addView(this.stars[i]);
        }
        for (int i = 0; i < DEFAULT_STARS; i++) {
            this.stars[i].on();
        }
    }

    public void click(int n) {
        for (int i = 0; i < stars.length; i++) {
            this.stars[i].off();
        }
        for (int i = 0; i <= n; i++) {
            this.stars[i].on();
        }
        this.number = n;
    }

    public int get() {
        return this.number + 1;
    }

}
