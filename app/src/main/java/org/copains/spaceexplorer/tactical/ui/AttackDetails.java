package org.copains.spaceexplorer.tactical.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.tactical.objects.AttackResult;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 13/06/2016.
 */

public class AttackDetails implements ModalInfo {

    private LifeForm attacker, defender;
    private int dice, damage;

    public AttackDetails(LifeForm attacker, LifeForm defender, int diceResult, int damage) {
        dice = diceResult;
        this.attacker = attacker;
        this.defender = defender;
        this.damage = damage;
    }

    public AttackDetails(AttackResult attackResult) {
        this(attackResult.getAttacker(),attackResult.getDefender(),attackResult.getDiceResult()
                ,attackResult.getLostLifePoints());
    }

    public boolean draw(Canvas canvas, Context context) {
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
        canvas.drawRect(rect,paint);
        return true;
    }

}
