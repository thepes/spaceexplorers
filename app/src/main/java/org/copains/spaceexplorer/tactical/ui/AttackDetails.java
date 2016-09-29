package org.copains.spaceexplorer.tactical.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.tactical.objects.AttackResult;
import org.copains.spaceexplorer.tactical.views.MapView;
import org.copains.tools.dice.DiceMg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 13/06/2016.
 */

public class AttackDetails implements ModalInfo {

    private final static int HEIGHT_RATIO = 4;

    private LifeForm attacker, defender;
    private List<LifeForm> multipleDefenders;
    private int dice, damage;

    public AttackDetails(LifeForm attacker, LifeForm defender, int diceResult, int damage) {
        this.dice = diceResult;
        this.attacker = attacker;
        this.defender = defender;
        this.damage = damage;
    }

    public AttackDetails(AttackResult attackResult) {
        this(attackResult.getAttacker(),attackResult.getDefender(),attackResult.getDiceResult()
                ,attackResult.getLostLifePoints());
        if (null != attackResult.getMultipleDefenders()) {
            this.multipleDefenders = attackResult.getMultipleDefenders();
        }
    }

    public boolean draw(Canvas canvas, Context context) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        int top, left, right, bottom;
        top = h / HEIGHT_RATIO;
        bottom = h - (h/HEIGHT_RATIO);
        left = (int)Math.round(w*0.1);
        right = (int)Math.round(w*0.9);
        RectF rect = new RectF(left, top, right, bottom);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(context.getResources().getColor(R.color.modal_background));
        canvas.drawRect(rect,paint);
        int centerH = (left+right)/2;
        int centerV = (top+bottom)/2;
        int diceX = centerH - (MapView.TILE_SIZE/2);
        int diceY = bottom - MapView.TILE_SIZE - 20;
        Rect r = new Rect(diceX,diceY,diceX + MapView.TILE_SIZE, diceY+MapView.TILE_SIZE);
        Log.i("spaceexplorers","DiceForBitmap : " + dice);
        int lifeformsHeight = MapView.TILE_SIZE;
        if (null != multipleDefenders) {
            Log.i("spaceexplorers","Cibles touchees : " + multipleDefenders.size());
            lifeformsHeight = (MapView.TILE_SIZE+10) * multipleDefenders.size();
        }
        paint.setColor(context.getResources().getColor(R.color.modal_attackers));
        RectF attackers = new RectF(left + 20, top + MapView.TILE_SIZE, left + MapView.TILE_SIZE + 40,
                top + MapView.TILE_SIZE + lifeformsHeight);
        canvas.drawRect(attackers,paint);
        BitmapDrawable lfIcon = (BitmapDrawable)context.getDrawable(attacker.getSpriteId());
        canvas.drawBitmap(lfIcon.getBitmap(),null,attackers,null);
        int defTop, defRight, defLeft, defBottom;
        defTop = top + MapView.TILE_SIZE;
        defRight = right - 40 - MapView.TILE_SIZE;
        defLeft = right - 20;
        defBottom = top + MapView.TILE_SIZE + lifeformsHeight;
        RectF defenders = new RectF(defRight, defTop, defLeft, defBottom);
        paint.setColor(context.getResources().getColor(R.color.modal_defenders));
        canvas.drawRect(defenders,paint);
        Rect defRect;
        if (null != multipleDefenders) {
            int i = 0;
            for (LifeForm lf : multipleDefenders) {
                defRect = new Rect(defRight, top + ((MapView.TILE_SIZE+10)*i),defLeft,
                        top + ((MapView.TILE_SIZE+10)*i) + MapView.TILE_SIZE);

                i++;
            }
        } else {
            lfIcon = (BitmapDrawable)context.getDrawable(defender.getSpriteId());
            canvas.drawBitmap(lfIcon.getBitmap(),null,defenders,null);
        }
        canvas.drawBitmap(DiceMg.getBitmap(context,dice),null,r,null);

        return true;
    }

    /**
     * this method is to be used for attacks involving multiple targets (laser or explosive)
     * @param lf the lifeform touched
     */
    public void addDefender(LifeForm lf) {
        if (null == multipleDefenders) {
            multipleDefenders = new ArrayList<>();
        }
        multipleDefenders.add(lf);
    }

}
