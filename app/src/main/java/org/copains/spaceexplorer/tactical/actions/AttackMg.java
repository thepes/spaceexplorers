package org.copains.spaceexplorer.tactical.actions;

import android.util.Log;

import org.copains.spaceexplorer.game.lifeforms.Human;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.tactical.objects.AttackResult;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;

/**
 * Created by DELAIRE on 10/06/2016.
 */

public class AttackMg {

    public static AttackResult shoot(LifeForm attacker, Coordinates attackedBlock) {
        CurrentMission mission = CurrentMission.getInstance();
        AttackResult res = new AttackResult();
        LifeForm lf = mission.getLifeFormOnMap(attackedBlock);
        if (null == lf) {
            Log.i("spaceexplorers", "No Life Form here");
            res.setHasError(true);
            res.setErrorMessage("No Life Form here");
            return res;
        }
        if (lf instanceof Human) {
            res.setHasError(true);
            res.setErrorMessage("Pas de tir ami");
            return res;
        }
        return null;
    }

}
