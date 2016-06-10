package org.copains.spaceexplorer.tactical.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import org.copains.spaceexplorer.game.lifeforms.LifeForm;

/**
 * Created by DELAIRE on 08/06/2016.
 */

public class LifeFormDetails {

    private static final int margin = 10;

    public static final void drawModal(Canvas c, LifeForm lf) {
        Log.i("spaceexplorers","display details for lf");
        int w = c.getWidth();
        int h = c.getHeight();
        int density = c.getDensity();
        int modalWidth = (int)Math.round(w*0.8);
        int modalHeight = (int)Math.round(h*0.8);
        int textSize = modalHeight / 15;

        int left = (w - modalWidth) / 2;
        int top = (h - modalHeight) / 2;
        int right = w - left;
        int bottom = h - top;
        RectF rect = new RectF(left, top, right, bottom);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setARGB(100,10,0,200);
        c.drawRect(rect,paint);
        paint.setTextSize(textSize);
        paint.setARGB(255,200,0,0);
        Rect r = new Rect();
        paint.getTextBounds("ploplop",0,7,r);
        Log.i("spaceexplorers", "Text Rect (h/w): " + r.height() + "/" + r.width());
        c.drawText(lf.getName(),left + margin,top + margin + textSize,paint);
        c.drawText("PV: "+lf.getLife() + "/" + lf.getMaxLife(), left + margin, top + 2*(textSize + margin),paint);
        //c.drawText("2",50,500,paint);
        Log.i("spaceexplorers","canvas density : " + density);
    }

}
