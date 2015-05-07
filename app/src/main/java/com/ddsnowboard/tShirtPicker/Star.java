/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddsnowboard.tShirtPicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author ddsnowboard
 * This is the class that makes up the RatingBox.
 */
public class Star extends ImageView {

    public static Resources resources;
    public static Drawable filledStar;
    public static Drawable outlineStar;
//  Which number star it is in the RatingBox.
    private final int index;
    private final RatingBox master;
    private boolean on;

    public Star(Context ctx, RatingBox master, boolean on, int index) {
        super(ctx);
        this.master = master;
        this.index = index;
        setScaleType(ScaleType.FIT_XY);
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));
        setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                ((Star) view).click();
            }
        });
        on = on;
        if (on) {
            setImageDrawable(filledStar);
        } else {
            setImageDrawable(outlineStar);
        }
    }

    public static void retrieveImages(Context ctx) {
        resources = ctx.getResources();
        filledStar = resources.getDrawable(R.drawable.filled_star);
        outlineStar = resources.getDrawable(R.drawable.outline_star);
    }

    public boolean isOn() {
        return this.on;
    }

    public void toggle() {
        if (on) {
            on = false;
            off();
        } else if (!this.on) {
            on = true;
            on();
        }
    }

    public void off() {
        on = false;
        setImageDrawable(outlineStar);
    }

    public void on() {
        on = true;
        setImageDrawable(filledStar);
    }

    public void click() {
        master.click(index);
    }
}
