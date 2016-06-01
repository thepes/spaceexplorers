package org.copains.spaceexplorer.game.lifeforms;

import org.copains.spaceexplorer.game.WeaponType;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.tools.PathFinder;

public abstract class LifeForm {
	
	private short actionPoints;
	private short movementPoints;
	private short life;
	
	private WeaponType rangeWeapon;
	private short meleeWeaponDamage;
	private short armor;
	
	private short posX = -1;
	private short posY = -1;
	
	
	public boolean canMove() {
		if (movementPoints == 0) {
			return (false);
		}
		return (PathFinder.getInstance().canMove(posX, posY));
	}
	
	public boolean canDoAction() {
		return (actionPoints > 0);
	}
	
	public boolean canOpenDoor() {
		if (movementPoints == 0) {
			return (false);
		}
		CurrentMission mission = CurrentMission.getInstance();
		return ((mission.doorIsOpenable(new Coordinates(posX-1, posY)))
				|| (mission.doorIsOpenable(new Coordinates(posX+1, posY)))
				|| (mission.doorIsOpenable(new Coordinates(posX, posY-1)))
				|| (mission.doorIsOpenable(new Coordinates(posX, posY+1))));
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

	public abstract void endTurn();

}
