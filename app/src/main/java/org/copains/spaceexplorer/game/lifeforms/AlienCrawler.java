package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.game.WeaponType;

/**
 * Alien se deplacant dans les couloirs pas d'arme a feu,
 * deux actions, attaques au corps a corps 
 * @author pes
 *
 */
public class AlienCrawler extends LifeForm {
	
	public AlienCrawler() {
		setRangeWeapon(WeaponType.NONE);
		setActionPoints((short) 2);
		setMovementPoints((short) 8);
		setArmor((short) 0);
		setLife((short) 6);
		setMeleeWeaponDamage((short) 2);

	}

	@Override
	public void endTurn() {
		setActionPoints((short) 2);
		setMovementPoints((short) 8);
	}

}
