package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.game.WeaponType;

public class Marine extends LifeForm {

	public Marine() {
		setRangeWeapon(WeaponType.RIFLE);
		setActionPoints((short) 1);
		setMovementPoints((short) 6);
		setArmor((short) 0);
		setLife((short) 6);
		setMeleeWeaponDamage((short) 1);
	}

	@Override
	public void endTurn() {
		setActionPoints((short)1);
		setMovementPoints((short) 6);
	}
	
	
	
}
