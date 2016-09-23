package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.WeaponType;

public class HeavyMarine extends LifeForm implements Human {

	public HeavyMarine(WeaponType weapon) {
		setRangeWeapon(weapon);
		setActionPoints((short) 1);
		setMovementPoints((short) 4);
		setArmor((short) 1);
		setLife((short) 6);
        setMaxLife((short) 6);
        setMeleeWeaponDamage((short) 1);
		setName("Marine blindé");
		setInternalId(2);
	}

	public HeavyMarine(LifeForm lf) {
		super(lf);
	}

	@Override
	public void endTurn() {
		setActionPoints((short)1);
		setMovementPoints((short) 4);
	}

	@Override
	public boolean isHuman() {
		return true;
	}

	@Override
	public int getSpriteId() {
		return R.drawable.marine;
	}

}
