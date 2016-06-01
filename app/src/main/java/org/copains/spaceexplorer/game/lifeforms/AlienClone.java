package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.game.WeaponType;

public class AlienClone extends LifeForm {
	
	public AlienClone() {
		setRangeWeapon(WeaponType.RIFLE);
		setActionPoints((short) 1);
		setMovementPoints((short) 6);
		setArmor((short) 0);
		setLife((short) 4);
		setMeleeWeaponDamage((short) 1);

	}

	@Override
	public void endTurn() {
		setActionPoints((short) 1);
		setMovementPoints((short) 6);
	}

}
