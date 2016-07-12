package org.copains.spaceexplorer.network.manager;

import android.util.Log;

import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.network.objects.LifeFormAction;

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

}
