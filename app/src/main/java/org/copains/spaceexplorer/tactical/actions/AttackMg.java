package org.copains.spaceexplorer.tactical.actions;

import android.util.Log;

import org.copains.spaceexplorer.game.lifeforms.Human;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.weapons.WeaponMg;
import org.copains.spaceexplorer.tactical.objects.AttackResult;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;

/**
 * Created by Sébastien Delaire <the.pes@gmail.com>
 * on 10/06/2016.
 */

public class AttackMg {

    public static AttackResult shoot(LifeForm attacker, Coordinates attackedBlock) {
        CurrentMission mission = CurrentMission.getInstance();
        AttackResult res = new AttackResult();
        LifeForm attackedLf = mission.getLifeFormOnMap(attackedBlock);
        if (null == attackedLf) {
            Log.i("spaceexplorers", "No Life Form here");
            res.setHasError(true);
            res.setErrorMessage("Pas d'ennemi ici");
            return res;
        }
        if (attackedLf instanceof Human) {
            res.setHasError(true);
            res.setErrorMessage("Pas de tir ami");
            return res;
        }
        if (!mission.isTargeted(attackedLf)) {
            res.setHasError(true);
            res.setErrorMessage("Impossible d'atteindre la cible");
            return res;
        }
        WeaponMg weaponMg = new WeaponMg();
        if (weaponMg.computeRangedWeaponTouchSuccess(attacker)) {
            res.setAttacker(attacker);
            res.setDefender(attackedLf);
            Log.i("spaceexplorers","Target touched");
            //TODO: handle LASERS and EXPLOSIVES (laser do a roll for each lifeform on the line
            // and explosive for each lifeform on surrounding cells
            short damage = weaponMg.getWeaponDamage(attacker);
            res.setLostLifePoints(weaponMg.getLastDamage());
            Log.i("spaceexplorers","damage : " + damage);
            if (damage >= attackedLf.getLife()) {
                Log.i("spaceexplorers","Target destroyed");
                mission.removeLifeFormFromMap(attackedLf);
                //TODO: remove life form from mission
            } else {
                attackedLf.removeLife(damage);
                Log.i("spaceexplorers","Remaining Life : " + attackedLf.getLife());
            }
        }
        res.setDiceResult(weaponMg.getLastDiceRoll());
        return res;
    }

}
