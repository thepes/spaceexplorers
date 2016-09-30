package org.copains.tools.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.StringTokenizer;

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

    public static void drawText(Canvas canvas, Rect box, String text, Paint paint) {
        StringTokenizer lines = new StringTokenizer(text,"\n");
        if (!lines.hasMoreTokens())
            return;

        int textSize = StringAndFontTools.getStandardTextSize(canvas);
        int verticalOffset = textSize + 5;
        while (lines.hasMoreTokens()) {
            StringTokenizer st = new StringTokenizer(lines.nextToken()," ");
            int charCount = paint.breakText(text, true, box.width() - 10, null);
            int currentChars = 0;

            String textLine = "";
            while (charCount > 0) {

                if (!st.hasMoreTokens()) {
                    canvas.drawText(textLine, box.left + 5, box.top + verticalOffset, paint);
                    return;
                }
                String token = st.nextToken();
                if (textLine.length() + token.length() > charCount) {
                    canvas.drawText(textLine, box.left + 5, box.top + verticalOffset, paint);
                    textLine = "";
                    String endString = token;
                    while (st.hasMoreTokens()) {
                        endString += " " + st.nextToken();
                    }
                    st = new StringTokenizer(endString, " ");
                    verticalOffset += textSize + 5;
                    charCount = paint.breakText(endString, true, box.width() - 10, null);
                    if (charCount == endString.length()) {
                        canvas.drawText(endString, box.left + 5, box.top + verticalOffset, paint);
                        return;
                    }
                } else {
                    textLine += token + " ";
                }
            }
            verticalOffset += textSize + 5;
        }
    }
}
