package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.game.WeaponType;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.tools.PathFinder;

import java.util.UUID;

public class LifeForm {

    private UUID uuid = UUID.randomUUID();

	private short actionPoints;
	private short movementPoints;
	private short life;
    private short maxLife;

	private WeaponType rangeWeapon;
	private short meleeWeaponDamage;
	private short armor;
	private String name;
    private int internalId;
	
	private short posX = -1;
	private short posY = -1;

	private boolean visibleOnMap = true;

    public LifeForm() {

    }

	public LifeForm(LifeForm lf) {
        this.uuid = lf.getUuid();
        this.actionPoints = lf.getActionPoints();
        this.movementPoints = lf.getMovementPoints();
        this.life = lf.getLife();
        this.maxLife = lf.getMaxLife();
        this.rangeWeapon = lf.getRangeWeapon();
        this.meleeWeaponDamage = lf.getMeleeWeaponDamage();
        this.armor = lf.getArmor();
        this.name = lf.getName();
        this.posX = lf.getPosX();
        this.posY = lf.getPosY();
        this.visibleOnMap = lf.isVisibleOnMap();
        this.internalId = lf.getInternalId();
    }
	
	public boolean canMove() {
		if (!visibleOnMap)
            return false;
		if (movementPoints == 0) {
			return (false);
		}
		return (PathFinder.getInstance().canMove(posX, posY));
	}
	
	public boolean canDoAction() {
		if (!visibleOnMap)
            return false;
        return (actionPoints > 0);
	}
	
	public boolean canOpenDoor() {
        if (!visibleOnMap)
            return false;
		if (movementPoints == 0) {
			return (false);
		}
		CurrentMission mission = CurrentMission.getInstance();
		return ((mission.doorIsOpenable(new Coordinates(posX-1, posY)))
				|| (mission.doorIsOpenable(new Coordinates(posX+1, posY)))
				|| (mission.doorIsOpenable(new Coordinates(posX, posY-1)))
				|| (mission.doorIsOpenable(new Coordinates(posX, posY+1))));
	}

    public int getSpriteId() {
        return -1;
    }
	
	public boolean openDoor() {
		return (true);
	}
	
	public Coordinates getCoordinates() {
		return (new Coordinates(posX, posY));
	}
	
	/**
	 * @return the actionPoints
	 */
	public short getActionPoints() {
		return actionPoints;
	}
	/**
	 * @param actionPoints the actionPoints to set
	 */
	public void setActionPoints(short actionPoints) {
		this.actionPoints = actionPoints;
	}
	/**
	 * @return the life
	 */
	public short getLife() {
		return life;
	}
	/**
	 * @param life the life to set
	 */
	public void setLife(short life) {
		this.life = life;
	}
	/**
	 * @return the rangeWeapon
	 */
	public WeaponType getRangeWeapon() {
		return rangeWeapon;
	}
	/**
	 * @param rangeWeapon the rangeWeapon to set
	 */
	public void setRangeWeapon(WeaponType rangeWeapon) {
		this.rangeWeapon = rangeWeapon;
	}
	/**
	 * @return the meleeWeaponDamage
	 */
	public short getMeleeWeaponDamage() {
		return meleeWeaponDamage;
	}
	/**
	 * @param meleeWeaponDamage the meleeWeaponDamage to set
	 */
	public void setMeleeWeaponDamage(short meleeWeaponDamage) {
		this.meleeWeaponDamage = meleeWeaponDamage;
	}
	/**
	 * @return the armor
	 */
	public short getArmor() {
		return armor;
	}
	/**
	 * @param armor the armor to set
	 */
	public void setArmor(short armor) {
		this.armor = armor;
	}
	/**
	 * @return the posX
	 */
	public short getPosX() {
		return posX;
	}
	/**
	 * @param posX the posX to set
	 */
	public void setPosX(short posX) {
		this.posX = posX;
	}
	/**
	 * @return the posY
	 */
	public short getPosY() {
		return posY;
	}
	/**
	 * @param posY the posY to set
	 */
	public void setPosY(short posY) {
		this.posY = posY;
	}
	/**
	 * @return the movementPoints
	 */
	public short getMovementPoints() {
		return movementPoints;
	}
	/**
	 * @param movementPoints the movementPoints to set
	 */
	public void setMovementPoints(short movementPoints) {
		this.movementPoints = movementPoints;
	}

	public void endTurn() {

	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(short maxLife) {
        this.maxLife = maxLife;
    }

	public void removeLife(short damage) {
		life -= damage;
	}

	public boolean shouldTargetRangedAttack() {
		if (rangeWeapon == WeaponType.RIFLE || rangeWeapon == WeaponType.HEAVY_RIFLE)
            return true;
        return false;
	}

    public boolean isVisibleOnMap() {
        return visibleOnMap;
    }

    public void setVisibleOnMap(boolean vis) {
        visibleOnMap = vis;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getInternalId() {
        return internalId;
    }

    public void setInternalId(int internalId) {
        this.internalId = internalId;
    }
}
