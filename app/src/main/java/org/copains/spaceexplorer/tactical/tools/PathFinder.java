package org.copains.spaceexplorer.tactical.tools;

import java.util.Hashtable;
import java.util.List;

import org.copains.spaceexplorer.R;
import org.copains.spaceexplorer.game.lifeforms.LifeForm;
import org.copains.spaceexplorer.game.objects.Door;
import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.CurrentMission;
import org.copains.spaceexplorer.tactical.objects.StarshipMap;
import org.copains.tools.geometry.LineEquation;

import android.R.bool;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.Log;

public class PathFinder {
	
	private static PathFinder instance = null;
	
	private Hashtable<String, Coordinates> moveGrid;
	private Hashtable<String, Coordinates> shootGrid;
	private Hashtable<String, Coordinates> exploredGrid;
	
	private PathFinder() {
		
	}
	
	public static PathFinder getInstance() {
		if (null == instance) {
			instance = new PathFinder();
		}
		return (instance);
	}
	
	public boolean canMove(int x, int y) {
		return (canWalk(x-1, y) || canWalk(x+1, y) || canWalk(x, y-1) || canWalk(x, y+1));
	}
	
	public boolean canWalk(int x, int y) {
		StarshipMap map = StarshipMap.getInstance();
		CurrentMission mission = CurrentMission.getInstance();
		short relief = map.getRelief(x, y);
		Coordinates c = new Coordinates(x, y);
		if ((relief == StarshipMap.FLOOR) || (relief == StarshipMap.START)) {
			if (null == mission.getLifeFormOnMap(c)) {
				return (true);
			} else {
				return (false);
			}
		}
		if (relief == StarshipMap.DOOR) {
			Door d = mission.getDoor(c);
			return (d.isOpen());
		}
		return (false);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param form 
	 * @return
	 */
	public boolean canShootLaser(int x, int y, LifeForm form) {
		if ((x != form.getPosX()) && (y != form.getPosY()))
			return false;
		StarshipMap map = StarshipMap.getInstance();
		CurrentMission mission = CurrentMission.getInstance();
		short relief = map.getRelief(x, y);
		Coordinates c = new Coordinates(x, y);
		if ((relief == StarshipMap.FLOOR) || (relief == StarshipMap.START)) {
			return (true);
		}
		if (relief == StarshipMap.DOOR) {
			Door d = mission.getDoor(c);
			return (d.isOpen());
		}
		return (false);
	}
	
	public boolean canShootRifle(int x, int y, LifeForm form) {
		boolean canShoot = true;
		// checking lines
		LineEquation eq = new LineEquation(x, y, form.getPosX(), form.getPosY());
		if (x == form.getPosX()) {
			int i = form.getPosY();
			while (i != y) {
				if (y > form.getPosY())
					i++;
				else
					i--;
				if (!canShootOver(new Coordinates(x, i)))
					return (false);
			}
			return (true);
		}
		if (y == form.getPosY()) {
			int i = form.getPosX();
			while (i != x) {
				if (x > form.getPosX())
					i++;
				else
					i--;
				if (!canShootOver(new Coordinates(i, y)))
					return (false);
			}
			return (true);
		}
		int i = form.getPosX();
		while (i != x) {
			if (x > form.getPosX())
				i++;
			else
				i--;			
			int j = eq.getY(i);
			if (!canShootOver(new Coordinates(i, j)))
				return (false);
		}
		return (true);
	}
	
	public boolean drawHighlight(Canvas c) {
		StarshipMap map = StarshipMap.getInstance();
		Coordinates display = MapViewHelper.getInstance(map, c).getDisplayAreaStart();
		MapViewEvents events = MapViewEvents.getInstance();
		MapViewHelper viewHelper = MapViewHelper.getInstance(map, c);
		CurrentMission mission = CurrentMission.getInstance();
		LifeForm form = events.getSelectedLifeForm();
		switch (events.getHighlightAction()) {
		// door opening action selected
		case R.string.open:
			List<Door> doors = mission.getAdjacentDoors(form.getCoordinates(), true);
			if ((null != doors) && (doors.size() > 0)) {
				for (Door d : doors) {
					List<Coordinates> doorTiles = d.getAllTiles();
					for (Coordinates tile : doorTiles) {
						// draw highlight
						RectF rect = viewHelper.convertTileToDisplayRect(tile);
						if (null != rect) { 
							Paint paint = new Paint();
							paint.setStyle(Style.FILL);
							paint.setAntiAlias(true);
							paint.setARGB(100, 255, 0, 0);
							c.drawRect(rect, paint);
							events.addVisibleMapEvent((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom, tile);
						}
					}
				}
			}
			break;

		// lifeform move action
		case R.string.move:
			moveGrid = new Hashtable<String, Coordinates>();
			buildMovementGrid(form.getPosX(), form.getPosY(), form.getMovementPoints());
			for (Coordinates tile : moveGrid.values()) {
				RectF rect = viewHelper.convertTileToDisplayRect(tile);
				if (null != rect) { 
					Paint paint = new Paint();
					paint.setStyle(Style.FILL);
					paint.setAntiAlias(true);
					paint.setARGB(100, 255, 0, 0);
					c.drawRect(rect, paint);
					events.addVisibleMapEvent((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom, tile);
				}
				
			}
			break;
		case R.string.action:
			shootGrid = new Hashtable<String, Coordinates>();
			exploredGrid = new Hashtable<String, Coordinates>();
            mission.reinitAttack();
			switch (form.getRangeWeapon())
			{
			case LASER:
				// lignes droites
				buildHeavyLaserShootLine(form.getPosX(), form.getPosY(),form);
				break;
			case RIFLE:
			case HEAVY_RIFLE:
			default:
				// zone de vision
				buildRifleShootLine(form.getPosX(), form.getPosY(),form);
				break;
			}
			for (Coordinates tile : shootGrid.values()) {
				RectF rect = viewHelper.convertTileToDisplayRect(tile);
				if (null != rect) { 
					Paint paint = new Paint();
					paint.setStyle(Style.FILL);
					paint.setAntiAlias(true);
					paint.setARGB(100, 255, 0, 0);
					c.drawRect(rect, paint);
					events.addVisibleMapEvent((int)rect.left, (int)rect.top, (int)rect.right, (int)rect.bottom, tile);
				}
			}
            if (null != mission.getTargetableLifeForms()) {
                for (LifeForm lf : mission.getTargetableLifeForms()) {
                    RectF rect = viewHelper.convertTileToDisplayRect(lf.getCoordinates());
                    if (null != rect) {
                        Paint paint = new Paint();
                        paint.setStyle(Style.FILL);
                        paint.setAntiAlias(true);
                        paint.setARGB(100, 255, 0, 0);
                        c.drawRect(rect, paint);
                        events.addVisibleMapEvent((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom, lf.getCoordinates());
                    }
                }
            }
			break;
		default:
			break;
		}
		return (true);
	}

	private boolean buildMovementGrid(int x, int y, int movePoints) {
		if (movePoints == 0) {
			return (false);
		}
		if (canWalk(x+1, y)) {
			moveGrid.put((x+1)+"_"+y, new Coordinates(x+1, y));
			buildMovementGrid(x+1, y, movePoints-1);
		}
		if (canWalk(x-1, y)) {
			moveGrid.put((x-1)+"_"+y, new Coordinates(x-1, y));
			buildMovementGrid(x-1, y, movePoints-1);
		}
		if (canWalk(x, y+1)) {
			moveGrid.put(x+"_"+(y+1), new Coordinates(x, y+1));
			buildMovementGrid(x, y+1, movePoints-1);
		}
		if (canWalk(x, y-1)) {
			moveGrid.put(x+"_"+(y-1), new Coordinates(x, y-1));
			buildMovementGrid(x, y-1, movePoints-1);
		}

		return (true);
	}

	
	private boolean buildHeavyLaserShootLine(int x, int y, LifeForm form) {
		StarshipMap map = StarshipMap.getInstance();
		if ((x < 0) || (x >= map.getSizeX()) )
			return true;
		if ((y < 0) || (y >= map.getSizeY()) )
			return true;
		exploredGrid.put(x+"_"+y, new Coordinates(x, y));
		if (!exploredGrid.containsKey((x+1)+"_"+y))
			if (canShootLaser(x+1, y,form)) {
				shootGrid.put((x+1)+"_"+y, new Coordinates(x+1, y));
				buildHeavyLaserShootLine(x+1, y,form);
			}
		if (!exploredGrid.containsKey((x-1)+"_"+y))
			if (canShootLaser(x-1, y,form)) {
				shootGrid.put((x-1)+"_"+y, new Coordinates(x-1, y));
				buildHeavyLaserShootLine(x-1, y,form);
			}
		if (!exploredGrid.containsKey(x+"_"+(y+1)))
			if (canShootLaser(x, y+1,form)) {
				shootGrid.put(x+"_"+(y+1), new Coordinates(x, y+1));
				buildHeavyLaserShootLine(x, y+1,form);
			}
		if (!exploredGrid.containsKey(x+"_"+(y-1)))
			if (canShootLaser(x, y-1,form)) {
				shootGrid.put(x+"_"+(y-1), new Coordinates(x, y-1));
				buildHeavyLaserShootLine(x, y-1,form);
			}
		return (true);
	}
	
	/**
	 * methode permettant de calculer les cases qui sont dans la ligne de vue
	 * @param x
	 * @param y
	 * @param form
	 * @return
	 */
	private boolean buildRifleShootLine(int x, int y, LifeForm form) {
		StarshipMap map = StarshipMap.getInstance();
		if ((x < 0) || (x >= map.getSizeX()) )
			return true;
		if ((y < 0) || (y >= map.getSizeY()) )
			return true;
		exploredGrid.put(x+"_"+y, new Coordinates(x, y));
		if (!exploredGrid.containsKey((x+1)+"_"+y))
			if (canShootRifle(x+1, y,form)) {
				shootGrid.put((x+1)+"_"+y, new Coordinates(x+1, y));
				buildRifleShootLine(x+1, y,form);
			}
		if (!exploredGrid.containsKey((x-1)+"_"+y))
			if (canShootRifle(x-1, y,form)) {
				shootGrid.put((x-1)+"_"+y, new Coordinates(x-1, y));
				buildRifleShootLine(x-1, y,form);
			}
		if (!exploredGrid.containsKey(x+"_"+(y+1)))
			if (canShootRifle(x, y+1,form)) {
				shootGrid.put(x+"_"+(y+1), new Coordinates(x, y+1));
				buildRifleShootLine(x, y+1,form);
			}
		if (!exploredGrid.containsKey(x+"_"+(y-1)))
			if (canShootRifle(x, y-1,form)) {
				shootGrid.put(x+"_"+(y-1), new Coordinates(x, y-1));
				buildRifleShootLine(x, y-1,form);
			}
		return (true);
	}
	
	/**
	 * verifie si une case est "traversable"
	 * @param c
	 * @return
	 */
	private boolean canShootOver(Coordinates c) {
		StarshipMap map = StarshipMap.getInstance();
		CurrentMission mission = CurrentMission.getInstance();
		short relief = map.getRelief(c.getX(), c.getY());
		
		if (null != mission.getLifeFormOnMap(c)) {
			// shooting line stops on lifeform but lifeform is "hitable" so add it in a list
            mission.addTargetableLifeForm(mission.getLifeFormOnMap(c));
			return (false);
		}
		if ((relief == StarshipMap.FLOOR) || (relief == StarshipMap.START)) {
			return (true);
		}
		if (relief == StarshipMap.DOOR) {
			Door d = mission.getDoor(c);
			return (d.isOpen());
		}
		return (false);
	}
	
}
