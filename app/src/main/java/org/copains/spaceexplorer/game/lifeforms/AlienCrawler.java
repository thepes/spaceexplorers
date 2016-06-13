package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.WeaponType;

/**
 * Alien se deplacant dans les couloirs pas d'arme a feu,
 * deux actions, attaques au corps a corps 
 * @author pes
 *
 */
public class AlienCrawler extends LifeForm implements Alien {
	
	public AlienCrawler() {
		setRangeWeapon(WeaponType.NONE);
		setActionPoints((short) 2);
		setMovementPoints((short) 8);
		setArmor((short) 0);
		setLife((short) 6);
		setMaxLife((short) 6);
		setMeleeWeaponDamage((short) 2);
		setName("Crawler");
	}

	@Override
	public void endTurn() {
		setActionPoints((short) 2);
		setMovementPoints((short) 8);
	}

	@Override
	public boolean isAlien() {
		return true;
	}

	@Override
	public int getSpriteId() {
		return R.drawable.alien;
	}

}
