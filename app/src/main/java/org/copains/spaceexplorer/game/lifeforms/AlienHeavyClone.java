package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.game.WeaponType;

public class AlienHeavyClone extends LifeForm implements Alien {
	
	public AlienHeavyClone() {
		setRangeWeapon(WeaponType.HEAVY_RIFLE);
		setActionPoints((short) 1);
		setMovementPoints((short) 4);
		setArmor((short) 1);
		setLife((short) 4);
		setMaxLife((short) 4);
		setMeleeWeaponDamage((short) 1);
		setName("Clone Blindé");
	}

	@Override
	public void endTurn() {
		setActionPoints((short) 1);
		setMovementPoints((short) 4);
	}

	@Override
	public boolean isAlien() {
		return true;
	}
}
