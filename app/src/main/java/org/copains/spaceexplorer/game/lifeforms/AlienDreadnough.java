package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.game.WeaponType;

public class AlienDreadnough extends LifeForm implements Alien {
	
	public AlienDreadnough() {
		setRangeWeapon(WeaponType.MULTI);
		setActionPoints((short) 3);
		setMovementPoints((short) 2);
		setArmor((short) 3);
		setLife((short) 4);
		setMeleeWeaponDamage((short) 1);

	}

	@Override
	public void endTurn() {
		setActionPoints((short) 3);
		setMovementPoints((short) 2);
	}

	@Override
	public boolean isAlien() {
		return true;
	}
}
