package org.copains.spaceexplorer.tactical.tools;

import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.StarshipMap;
import org.copains.spaceexplorer.tactical.views.MapView;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

public class MapViewHelper {

	private static MapViewHelper instance = null;
	
	private int horizontalTilesCount;
	private int verticalTilesCount;
	private StarshipMap map;
	private Coordinates displayAreaStart;
	private Coordinates displayAreaEnd;
	
	private MapViewHelper() {
		
	}
	
	private MapViewHelper(StarshipMap map, Canvas c) {
		this.map = map;
		horizontalTilesCount = c.getWidth() / MapView.TILE_SIZE;
		verticalTilesCount = c.getHeight() / MapView.TILE_SIZE;
		displayAreaStart = new Coordinates(0, 0);
		displayAreaStart.setBounds(map.getSizeX()-horizontalTilesCount, map.getSizeY()-verticalTilesCount);
		computeDisplayAreaStart();
	}
	
	public void reinitCounts(Canvas c) {
		horizontalTilesCount = c.getWidth() / MapView.TILE_SIZE;
		verticalTilesCount = c.getHeight() / MapView.TILE_SIZE;
		computeDisplayAreaStart();
	}
	
	public static MapViewHelper getInstance(StarshipMap map, Canvas c) {
		if (null == instance) {
			instance = new MapViewHelper(map, c);
		}
		//instance.reinitCounts(c);
		return (instance);
	}
	
	public static MapViewHelper getInstance() {
		return (instance);
	}

	private boolean computeDisplayAreaStart() {
		if (map.getSizeY() - map.getHumanTopLeft().getY() < verticalTilesCount) {
			displayAreaStart.setY(map.getSizeY()-verticalTilesCount);
		}
		if (map.getSizeX() - map.getHumanTopLeft().getX() < horizontalTilesCount) {
			displayAreaStart.setX(map.getSizeX()-horizontalTilesCount);
		}
		displayAreaEnd = new Coordinates(displayAreaStart.getX()+horizontalTilesCount,
				displayAreaStart.getY()+verticalTilesCount);
		return (true);
	}
	
	public RectF convertTileToDisplayRect(Coordinates tileCoord) {
		if (!tileCoord.isInside(displayAreaStart, displayAreaEnd)) {
			Log.i("SpaceExplorer", "tile outside display area : " + tileCoord.toString());
			return (null);
		}
		int x = tileCoord.getX()-displayAreaStart.getX(); 
		int y = tileCoord.getY()-displayAreaStart.getY();
		Log.i("SpaceExplorer", "tile coord : " + x + "/" + y);
		RectF r = new RectF((x*MapView.TILE_SIZE), (y*MapView.TILE_SIZE), MapView.TILE_SIZE+(x*MapView.TILE_SIZE), MapView.TILE_SIZE+(y*MapView.TILE_SIZE));
		return (r);
	}
	
	public void move(int x, int y) {
		displayAreaStart.add(x, y);
		displayAreaEnd = new Coordinates(displayAreaStart.getX()+horizontalTilesCount,
				displayAreaStart.getY()+verticalTilesCount);
	}
	
	public static int getHorizontalTilesCount(Canvas c) {
		return (c.getWidth() / MapView.TILE_SIZE);
	}
	
	public static int getVerticalTilesCount(Canvas c) {
		return (c.getHeight() / MapView.TILE_SIZE);		
	}

	/**
	 * @return the horizontalTilesCount
	 */
	public int getHorizontalTilesCount() {
		return horizontalTilesCount;
	}

	/**
	 * @param horizontalTilesCount the horizontalTilesCount to set
	 */
	public void setHorizontalTilesCount(int horizontalTilesCount) {
		this.horizontalTilesCount = horizontalTilesCount;
	}

	/**
	 * @return the verticalTilesCount
	 */
	public int getVerticalTilesCount() {
		return verticalTilesCount;
	}

	/**
	 * @param verticalTilesCount the verticalTilesCount to set
	 */
	public void setVerticalTilesCount(int verticalTilesCount) {
		this.verticalTilesCount = verticalTilesCount;
	}

	/**
	 * @return the map
	 */
	public StarshipMap getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(StarshipMap map) {
		this.map = map;
	}

	/**
	 * @return the displayAreaStart
	 */
	public Coordinates getDisplayAreaStart() {
		return displayAreaStart;
	}

	/**
	 * @param displayAreaStart the displayAreaStart to set
	 */
	public void setDisplayAreaStart(Coordinates displayAreaStart) {
		this.displayAreaStart = displayAreaStart;
	}
}
