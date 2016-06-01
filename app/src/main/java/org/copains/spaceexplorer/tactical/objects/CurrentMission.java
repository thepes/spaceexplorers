package org.copains.spaceexplorer.tactical.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.copains.spaceexplorer.ai.manager.AlienMg;
import org.copains.spaceexplorer.game.WeaponType;
import org.copains.spaceexplorer.game.lifeforms.HeavyMarine;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.lifeforms.Marine;
import org.copains.spaceexplorer.game.objects.Door;

import android.util.Log;

public class CurrentMission {

	private static CurrentMission instance = null;
	
	private boolean teamInPosition = false;
	private LifeForm team[] = new LifeForm[6];
	private List<LifeForm> aliens;
	private List<Door> doors;
	
	private CurrentMission() {
		for (int i = 0 ; i < 3 ; i++) {
			team[i] = new Marine();
		}
		team[3] = new HeavyMarine(WeaponType.EXPLOSIVE);
		team[4] = new HeavyMarine(WeaponType.LASER);
		team[5] = new HeavyMarine(WeaponType.HEAVY_RIFLE);
		initDoors();
		initAliens();
	}
	
	private void initAliens() {
		Random rnd = new Random();
		aliens = new ArrayList<LifeForm>();
		StarshipMap map = StarshipMap.getInstance();
		AlienMg alienMg = AlienMg.getInstance();
		while (alienMg.getRemainingAliens() > 0) {
			for (int x = 0 ; x < map.getSizeX() ; x ++)
				for (int y = 0 ; y < map.getSizeY() ; y++) {
					if (map.getRelief(x, y) == StarshipMap.FLOOR) {
						// une chance sur 6 d'avoir un alien sur la case
						if (rnd.nextInt(6) == 0) {
							LifeForm alien = alienMg.getRandomAlienForPlacement();
							if (null == alien)
								break;
							alien.setPosX((short)x);
							alien.setPosY((short)y);
							aliens.add(alien);
							if (alienMg.getRemainingAliens() == 0)
								break;
						}
					}
				}
		}
	}

	private void initDoors() {
		doors = new ArrayList<Door>();
		StarshipMap map = StarshipMap.getInstance();
		for (int y = 0 ; y < map.getSizeY() ; y++)
		for (int x = 0 ; x < map.getSizeX() ; x++) {
			if (map.getRelief(x, y) == StarshipMap.DOOR) {
				boolean newDoor = true;
				Coordinates start = new Coordinates(x, y);
				for (Door d : doors) {
					if (start.isInside(d.getTopLeft(), d.getBottomRight())) {
						newDoor = false;
					}
				}
				// if new Door, looking for door end
				if (newDoor) {
					if (map.getRelief(x+1, y) == StarshipMap.DOOR) {
						while (map.getRelief(x, y) == StarshipMap.DOOR) {
							x++;
						}
						Coordinates stop = new Coordinates(x-1, y);
						Door d = new Door(start,stop);
						Log.i("Space", d.toString());
						doors.add(d);
					} else if (map.getRelief(x, y+1) == StarshipMap.DOOR) {
						int tmpY = y;
						while (map.getRelief(x, tmpY) == StarshipMap.DOOR) {
							tmpY++;
						}
						Coordinates stop = new Coordinates(x, tmpY-1);
						Door d = new Door(start,stop);
						Log.i("Space", d.toString());
						doors.add(d);
					} else {
						Door d = new Door(start,start);
						Log.i("Space", d.toString());
						doors.add(d);						
					}
				}
			}
		}
	}
	
	public Door getDoor(Coordinates c) {
		for (Door d : doors) {
			if (c.isInside(d.getTopLeft(), d.getBottomRight())) {
				return (d);
			}
		}
		return (null);
	}
	
	public boolean doorIsOpenable(Coordinates c) {
		Door d = getDoor(c);
		if (null != d) {
			return (!d.isOpen());
		}
		return (false);
	}
	
	public List<Door> getAdjacentDoors(Coordinates coord, boolean onlyClosed) {
		if (null == coord) {
			return (null);
		}
		ArrayList<Door> doors = new ArrayList<Door>();
		Coordinates tmp = new Coordinates(coord.getX()-1,coord.getY());
		Door d = getDoor(tmp);
		if (canAdd(d, onlyClosed)) {
				doors.add(d);
		}
		tmp = new Coordinates(coord.getX()+1,coord.getY());
		d = getDoor(tmp);
		if (canAdd(d, onlyClosed)) {
			doors.add(d);
		}
		tmp = new Coordinates(coord.getX(),coord.getY()-1);
		d = getDoor(tmp);
		if (canAdd(d, onlyClosed)) {
			doors.add(d);
		}
		tmp = new Coordinates(coord.getX(),coord.getY()+1);
		d = getDoor(tmp);
		if (canAdd(d, onlyClosed)) {
			doors.add(d);
		}
		return (doors);
	}
	
	private boolean canAdd(Door d, boolean onlyClosed) {
		if (null != d)
			if (onlyClosed){
				if (!d.isOpen()) {
					return (true);
				}
			} else {
				return (true);
			}
		return (false);
	}

	public static CurrentMission getInstance() {
		if (null == instance) {
			instance = new CurrentMission();
		}
		return (instance);
	}
	
	public LifeForm getTeamMember(int i) {
		return (team[i]);
	}
	
	public LifeForm getLifeFormOnMap(Coordinates pos) {
		for (LifeForm marine : team) {
			if ((marine.getPosX() == (short)pos.getX()) && (marine.getPosY() == (short)pos.getY())) {
				return (marine);
			}
		}
		return (null);
	}
	
	public boolean isTeamInPosition() {
		return (teamInPosition);
	}
	
	public void setTeamInPosition(boolean pos) {
		teamInPosition = pos;
	}

	/**
	 * fin du tour. 
	 * reinitialisation des Points d'action 
	 * TODO: actions alien ou MDJ
	 * @return
	 */
	public boolean endTurn() {
		for (LifeForm lf : team) {
			lf.endTurn();
		}
		return (true);
	}
}
