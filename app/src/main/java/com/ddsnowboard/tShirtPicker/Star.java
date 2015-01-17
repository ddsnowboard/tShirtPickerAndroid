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
 */
public class Star extends ImageView {

    public static Resources res;
    public static Drawable filledStar;
    public static Drawable outlineStar;
    private final int index;
    private final RatingBox master;
    private boolean on;

    public Star(Context ctx, RatingBox master, boolean on, int index) {
        super(ctx);
        this.master = master;
        this.index = index;
        this.setScaleType(ScaleType.FIT_XY);
        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f));
        this.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                ((Star) view).click();
            }
        });
        this.on = on;
        if (on) {
            this.setImageDrawable(filledStar);
        } else {
            this.setImageDrawable(outlineStar);
        }
    }

    public static void retrieveImages(Context ctx) {
        res = ctx.getResources();
        filledStar = res.getDrawable(R.drawable.filled_star);
        outlineStar = res.getDrawable(R.drawable.outline_star);
    }

    public boolean isOn() {
        return this.on;
    }

    public void toggle() {
        if (this.on) {
            this.on = false;
            this.off();
        } else if (!this.on) {
            this.on = true;
            this.on();
        }
    }

    public void off() {
        this.on = false;
        this.setImageDrawable(outlineStar);
    }

    public void on() {
        this.on = true;
        this.setImageDrawable(filledStar);
    }

    public void click() {
        this.master.click(this.index);
    }
}
