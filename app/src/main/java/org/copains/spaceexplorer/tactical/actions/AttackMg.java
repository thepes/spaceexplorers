package org.copains.spaceexplorer.tactical.actions;

import android.util.Log;

import org.copains.spaceexplorer.game.lifeforms.Human;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.weapons.WeaponMg;
import org.copains.spaceexplorer.tactical.objects.AttackResult;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.objects.StarshipMap;

import java.util.ArrayList;
import java.util.List;

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
        if (attacker.shouldTargetRangedAttack()) {
            if (!mission.isTargeted(attackedLf)) {
                res.setHasError(true);
                res.setErrorMessage("Impossible d'atteindre la cible");
                return res;
            }
        }
        WeaponMg weaponMg = new WeaponMg();
        if (weaponMg.computeRangedWeaponTouchSuccess(attacker)) {
            res.setAttacker(attacker);
            switch (attacker.getRangeWeapon()) {

                case NONE:
                    break;
                case LASER:
                    //TODO: handle LASERS and EXPLOSIVES (laser do a roll for each lifeform on the line
                    // and explosive for each lifeform on surrounding cells
                    List<LifeForm> targets = getMultiTargets(attacker, attackedBlock);
                    for (LifeForm lf : targets) {
                        Log.i("spaceexplorers","Find target at pos : " + lf.getCoordinates().getX()
                                + "/" + lf.getCoordinates().getY());
                        computeDamage(attacker,lf,res);
                    }
                    break;
                case EXPLOSIVE:
                    break;
                case HEAVY_RIFLE:
                case RIFLE:
                    computeDamage(attacker,attackedLf,res);
                    break;
                case MULTI:
                    break;
            }

        }
        res.setDiceResult(weaponMg.getLastDiceRoll());
        return res;
    }

    private static List<LifeForm> getMultiTargets(LifeForm attacker, Coordinates attackedBlock) {
        List<LifeForm> result = new ArrayList<>();
        StarshipMap map = StarshipMap.getInstance();
        switch (attacker.getRangeWeapon()) {

            case NONE:
            case HEAVY_RIFLE:
            case RIFLE:
                return null;
            case LASER:
                if (attacker.getCoordinates().getX() == attackedBlock.getX()) {
                    int x = attacker.getCoordinates().getX();
                    if (attacker.getCoordinates().getY() < attackedBlock.getY()) {
                        for (int y = attacker.getCoordinates().getY()+1 ; y < map.getSizeY() ; y++) {
                            if (map.getRelief(x,y) == StarshipMap.WALL)
                                return result;
                            Coordinates coord = new Coordinates(x,y);
                            if (map.getRelief(x,y) == StarshipMap.DOOR) {
                                if (!CurrentMission.getInstance().getDoor(coord).isOpen())
                                    return result;
                            }
                            LifeForm lf = CurrentMission.getInstance().getLifeFormOnMap(coord);
                            if (null != lf)
                                result.add(lf);
                        }
                    } else if (attacker.getCoordinates().getY()-1 > attackedBlock.getY()) {
                        for (int y = attacker.getCoordinates().getY() ; y > 0 ; y--) {
                            if (map.getRelief(x,y) == StarshipMap.WALL)
                                return result;
                            Coordinates coord = new Coordinates(x,y);
                            if (map.getRelief(x,y) == StarshipMap.DOOR) {
                                if (!CurrentMission.getInstance().getDoor(coord).isOpen())
                                    return result;
                            }
                            LifeForm lf = CurrentMission.getInstance().getLifeFormOnMap(coord);
                            if (null != lf)
                                result.add(lf);
                        }
                    }
                } else if (attacker.getCoordinates().getY() == attackedBlock.getY()) {
                    int y = attacker.getCoordinates().getY();
                    if (attacker.getCoordinates().getX() < attackedBlock.getX()) {
                        for (int x = attacker.getCoordinates().getX()+1 ; x < map.getSizeX() ; x++) {
                            if (map.getRelief(x,y) == StarshipMap.WALL)
                                return result;
                            Coordinates coord = new Coordinates(x,y);
                            if (map.getRelief(x,y) == StarshipMap.DOOR) {
                                if (!CurrentMission.getInstance().getDoor(coord).isOpen())
                                    return result;
                            }
                            LifeForm lf = CurrentMission.getInstance().getLifeFormOnMap(coord);
                            if (null != lf)
                                result.add(lf);
                        }
                    } else if (attacker.getCoordinates().getX() > attackedBlock.getX()) {
                        for (int x = attacker.getCoordinates().getX()-1 ; x > 0 ; x--) {
                            if (map.getRelief(x,y) == StarshipMap.WALL)
                                return result;
                            Coordinates coord = new Coordinates(x,y);
                            if (map.getRelief(x,y) == StarshipMap.DOOR) {
                                if (!CurrentMission.getInstance().getDoor(coord).isOpen())
                                    return result;
                            }
                            LifeForm lf = CurrentMission.getInstance().getLifeFormOnMap(coord);
                            if (null != lf)
                                result.add(lf);
                        }
                    }
                }
                break;
            case EXPLOSIVE:
                break;
            case MULTI:
                break;
        }

        return result;
    }

    private static boolean computeDamage(LifeForm attacker, LifeForm attackedLf, AttackResult res) {
        WeaponMg weaponMg = new WeaponMg();
        CurrentMission mission = CurrentMission.getInstance();
        res.setDefender(attackedLf);
        Log.i("spaceexplorers","Target touched");
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
        return true;
    }

}
