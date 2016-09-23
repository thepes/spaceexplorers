package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.WeaponType;

public class Marine extends LifeForm implements Human {

	public Marine() {
        super();
        setRangeWeapon(WeaponType.RIFLE);
		setActionPoints((short) 1);
		setMovementPoints((short) 6);
		setArmor((short) 0);
		setLife((short) 6);
		setMaxLife((short) 6);
        setMeleeWeaponDamage((short) 1);
		setName("Marine");
		setInternalId(1);
	}

	public Marine(LifeForm lf) {
		super(lf);
	}

	@Override
	public void endTurn() {
		setActionPoints((short)1);
		setMovementPoints((short) 6);
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
