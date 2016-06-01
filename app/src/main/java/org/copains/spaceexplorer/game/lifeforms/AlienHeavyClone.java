package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.game.WeaponType;

public class AlienHeavyClone extends LifeForm {
	
	public AlienHeavyClone() {
		setRangeWeapon(WeaponType.HEAVY_RIFLE);
		setActionPoints((short) 1);
		setMovementPoints((short) 4);
		setArmor((short) 1);
		setLife((short) 4);
		setMeleeWeaponDamage((short) 1);

	}

	@Override
	public void endTurn() {
		setActionPoints((short) 1);
		setMovementPoints((short) 4);
	}

}
