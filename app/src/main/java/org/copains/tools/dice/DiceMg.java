package org.copains.tools.dice;

import android.util.Log;

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
}
