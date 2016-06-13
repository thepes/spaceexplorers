package org.copains.spaceexplorer.tactical.ui;

import android.content.Context;
import android.graphics.Canvas;

import org.copains.spaceexplorer.game.lifeforms.LifeForm;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 13/06/2016.
 */

public class AttackDetails {

    private LifeForm attacker, defender;
    private int dice, damage;

    public AttackDetails(LifeForm attacker, LifeForm defender, int diceResult, int damage) {
        dice = diceResult;
        this.attacker = attacker;
        this.defender = defender;
        this.damage = damage;
    }

    public boolean draw(Canvas canvas, Context context) {

        return true;
    }

}
