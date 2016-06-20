package org.copains.tools.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 20/06/2016.
 */

public class ModalTools {

    public static final Rect drawStandardModalBox(Canvas canvas) {
        Rect boxCoords = new Rect();
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        int top, left, right, bottom;
        top = h / 3;
        bottom = h - (h/3);
        left = (int)Math.round(w*0.1);
        right = (int)Math.round(w*0.9);
        RectF rect = new RectF(left, top, right, bottom);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setARGB(100,0,50,50);
        canvas.drawRect(rect, paint);
        boxCoords.set(left,top,right,bottom);
        return boxCoords;
    }
}
