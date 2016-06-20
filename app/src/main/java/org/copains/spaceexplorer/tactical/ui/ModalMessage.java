package org.copains.spaceexplorer.tactical.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.copains.tools.ui.ModalTools;
import org.copains.tools.ui.StringAndFontTools;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 20/06/2016.
 */

public class ModalMessage implements ModalInfo {

    private String message;

    public ModalMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean draw(Canvas canvas, Context context) {
        Rect box = ModalTools.drawStandardModalBox(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setARGB(255,255,0,0);
        int textsize = StringAndFontTools.getStandardTextSize(canvas);
        paint.setTextSize(textsize);
        canvas.drawText(message,box.left + MARGIN , box.top + textsize + MARGIN, paint);
        return false;
    }
}
