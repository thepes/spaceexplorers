package org.copains.spaceexplorer.network.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.objects.Door;
import org.copains.spaceexplorer.network.objects.LifeFormAction;
import org.copains.spaceexplorer.tactical.objects.AttackResult;

import java.util.List;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 11/07/2016.
 */

public class LifeFormActionMg {

    public static boolean save(LifeFormAction action) {
        action.save();
        return true;
    }

    public static boolean lifeFormCreated(LifeForm lf) {
        LifeFormAction action = new LifeFormAction();
        action.setActionType(LifeFormAction.ACTION_CREATION);
        action.setActorUuid(lf.getUuid().toString());
        save(action);
        Log.i("spaceeexplorer","Creation Lifeform UUID : " + lf.getUuid().toString() + " / Action ID : " + action.getId());
        return true;
    }

    public static boolean recordAttackResult(LifeForm actor, AttackResult attackResult) {
        LifeFormAction action = new LifeFormAction();
        if (attackResult.getMultipleDefenders() != null) {
            action.setActionType(LifeFormAction.ACTION_SHOOT_MULTI);
        } else {
            action.setActionType(LifeFormAction.ACTION_SHOOT);
        }
        GsonBuilder gBuilder = new GsonBuilder();
        Gson gson = gBuilder.create();
        action.setActionResult(gson.toJson(attackResult));
        action.setActorUuid(actor.getUuid().toString());
        save(action);
        return true;
    }

    public static boolean recordOpenDoor(LifeForm actor, Door door) {
        LifeFormAction action = new LifeFormAction();
        action.setActionType(LifeFormAction.ACTION_OPEN);
        GsonBuilder gBuilder = new GsonBuilder();
        Gson gson = gBuilder.create();
        action.setActionResult(gson.toJson(door));
        action.setActorUuid(actor.getUuid().toString());
        save(action);
        return true;
    }

    public static boolean recordMovement(LifeForm actor) {
        LifeFormAction action = new LifeFormAction();
        action.setActionType(LifeFormAction.ACTION_MOVE);
        action.setActorUuid(actor.getUuid().toString());
        save(action);
        return true;
    }

    public static List<LifeFormAction> list() {
        List<LifeFormAction> actions = LifeFormAction.listAll(LifeFormAction.class,"id ASC");
        return actions;
    }

}
