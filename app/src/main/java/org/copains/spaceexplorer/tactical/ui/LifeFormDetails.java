package org.copains.spaceexplorer.tactical.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import org.copains.spaceexplorer.game.lifeforms.LifeForm;

/**
 * Created by DELAIRE on 08/06/2016.
 */

public class LifeFormDetails {

    public static final void drawModal(Canvas c, LifeForm lf) {
        Log.i("spaceexplorers","display details for lf");
        int w = c.getWidth();
        int h = c.getHeight();
        int density = c.getDensity();
        int modalWidth = (int)Math.round(w*0.8);
        int modalHeight = (int)Math.round(h*0.8);
        RectF rect = new RectF((w-modalWidth) / 2,
                (h-modalHeight) / 2,
                w - ((w-modalWidth) / 2),
                h - (h-modalHeight) / 2);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setARGB(100,10,0,200);
        c.drawRect(rect,paint);
        paint.setTextSize(200);
        paint.setARGB(255,200,0,0);
        /*c.drawText("Hello",50,250,paint);
        c.drawText("2",50,500,paint);*/
        Log.i("spaceexplorers","canvas density : " + density);
    }

}
