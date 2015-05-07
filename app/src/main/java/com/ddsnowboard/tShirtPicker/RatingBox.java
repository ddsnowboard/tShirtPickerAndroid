/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * @author ddsnowboard
 */
public class RatingBox {

    private static final int DEFAULT_STARS = 3;
    private static final int NUM_OF_STARS = Shirt.PEAK_RATING;
    private final Star[] stars;
    private final LinearLayout master;
    private final Context context;
    private int number;

    public RatingBox(Context context, LinearLayout master) {
        this.stars = new Star[NUM_OF_STARS];
        this.context = context;
        this.master = master;
        this.number = DEFAULT_STARS;
        // This instantiates the stars.
        for (int i = 0; i < NUM_OF_STARS; i++) {
            this.stars[i] = new Star(this.context, this, false, i);
            this.master.addView(this.stars[i]);
        }
        // This fills in the default number that should be filled in.
        for (int i = 0; i < DEFAULT_STARS; i++) {
            this.stars[i].on();
        }
    }
//    If a star is clicked on, it calls this in its parent, giving its index, which lights up
//     the proper number of other stars.
    public void click(int n) {
        if (n <= NUM_OF_STARS) {
//             It shuts them all off first. It seems inefficient, but it works fine on my ancient
//             phone, so I figure it's ok.
            for (Star star : stars) {
                star.off();
            }
            for (int i = 0; i <= n; i++) {
                this.stars[i].on();
            }
            this.number = n;
        }
    }
//  number is zero-indexed, so I have to do this.
    public int get() {
        return this.number + 1;
    }

    public void setRating(int rating) {
        click(rating - 1);
    }
}
