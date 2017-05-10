package com.crackretail.sdk.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * Created by Shivam on 26-Jul-16.
 */
public class btnback extends Drawable
{
   // int width=48;

    @Override
    public void draw(Canvas canvas) {



        Paint fillpaint=new Paint();
        fillpaint.setStyle(Paint.Style.FILL);
        fillpaint.setColor(Color.parseColor("#777777"));
        fillpaint.setAntiAlias(true);

        canvas.drawCircle(36, 36, 32, fillpaint);



        Paint strokepaint=new Paint();
        strokepaint.setStyle(Paint.Style.STROKE);
        strokepaint.setColor(Color.parseColor("#ffffff"));
        strokepaint.setStrokeWidth(8);
        strokepaint.setAntiAlias(true);
        canvas.drawCircle(36, 36, 32, strokepaint);

        Paint txtpaint=new Paint();
        txtpaint.setColor(Color.WHITE);
        txtpaint.setTextAlign(Paint.Align.CENTER);
        txtpaint.setTextSize(42);
        txtpaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        txtpaint.setAntiAlias(true);

        canvas.drawText("X",36,52,txtpaint);



    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
