package org.copains.tools.ui;

import android.graphics.Canvas;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 20/06/2016.
 */

public class StringAndFontTools {

    private static final int MAX_LINES_ON_SCREEN = 30;

    /**
     * returns a text size for Standard text (based on screen size)
     * @param c
     * @return the textsize
     */
    public static final int getStandardTextSize(Canvas c) {
        return (c.getHeight()/MAX_LINES_ON_SCREEN);
    }

}
