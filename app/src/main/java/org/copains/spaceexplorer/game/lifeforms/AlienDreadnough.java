package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.WeaponType;

public class AlienDreadnough extends LifeForm implements Alien {
	
	public AlienDreadnough() {
		setRangeWeapon(WeaponType.MULTI);
		setActionPoints((short) 3);
		setMovementPoints((short) 2);
		setArmor((short) 3);
		setLife((short) 4);
		setMaxLife((short) 4);
		setMeleeWeaponDamage((short) 1);
		setName("Dreadnough");
	}

	public AlienDreadnough(LifeForm lf) {
		super(lf);
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

	@Override
	public int getSpriteId() {
		return R.drawable.alien;
	}

}
