package org.copains.tools.dice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import org.copains.spaceexplorer.R;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 13/06/2016.
 */

public class DiceMg {

    public static int rollDice(int diceFaces) {
        double rnd = Math.random() * 1000;
        int diceResult = (int)(rnd % diceFaces) + 1;
        Log.i("spaceexplorers","Dice result for " + diceFaces + " faces : " + diceResult);
        return (diceResult);
    }

    public static Bitmap getBitmap(Context context,int diceResult) {
        BitmapDrawable drawable = null;
        switch (diceResult) {
            case 1:
                drawable = (BitmapDrawable)context.getDrawable(R.drawable.dice1);
                break;
            case 2:
                drawable = (BitmapDrawable)context.getDrawable(R.drawable.dice2);
                break;
            case 3:
                drawable = (BitmapDrawable)context.getDrawable(R.drawable.dice3);
                break;
            case 4:
                drawable = (BitmapDrawable)context.getDrawable(R.drawable.dice4);
                break;
            case 5:
                drawable = (BitmapDrawable)context.getDrawable(R.drawable.dice5);
                break;
            case 6:
                drawable = (BitmapDrawable)context.getDrawable(R.drawable.dice6);
                break;
        }
        if (drawable != null) {
            return drawable.getBitmap();
        }
        return null;
    }
}
