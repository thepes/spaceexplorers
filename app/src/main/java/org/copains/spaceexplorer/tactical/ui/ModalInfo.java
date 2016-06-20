package org.copains.spaceexplorer.tactical.ui;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 14/06/2016.
 */

public interface ModalInfo {

    public static final int MARGIN = 30;

    public boolean draw(Canvas canvas, Context context);

}
