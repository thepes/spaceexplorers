package org.copains.spaceexplorer.game.objects;

import java.util.ArrayList;
import java.util.List;

import org.copains.spaceexplorer.tactical.objects.Coordinates;

import android.util.Log;

public class Door {

	private Coordinates topLeft;
	private Coordinates bottomRight;

	private List<Room> adjacentRooms;
	
	private boolean open = false;
	
	public Door() {
		
	}

	public Door(Coordinates start, Coordinates stop) {
		topLeft = start;
		bottomRight = stop;
	}

	public List<Coordinates> getAllTiles() {
		ArrayList<Coordinates> tiles = new ArrayList<>();
		for (int x = topLeft.getX() ; x <= bottomRight.getX() ; x++) {
			for (int y = topLeft.getY() ; y <= bottomRight.getY() ; y++) {
				Log.i("SpaceExplorer", "Adding tile for door : " + x + "/" + y);
				tiles.add(new Coordinates(x, y));
			}
		}
		return (tiles);
	}
	
	/**
	 * @return the topLeft
	 */
	public Coordinates getTopLeft() {
		return topLeft;
	}

	/**
	 * @param topLeft the topLeft to set
	 */
	public void setTopLeft(Coordinates topLeft) {
		this.topLeft = topLeft;
	}

	/**
	 * @return the bottomRight
	 */
	public Coordinates getBottomRight() {
		return bottomRight;
	}

	/**
	 * @param bottomRight the bottomRight to set
	 */
	public void setBottomRight(Coordinates bottomRight) {
		this.bottomRight = bottomRight;
	}

	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
		if (null != adjacentRooms) {
			for (Room r : adjacentRooms) {
                r.setOpen(true);
            }
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Door={start=").append(topLeft.getX()).append("/").append(topLeft.getY());
		sb.append(",stop=").append(bottomRight.getX()).append("/").append(bottomRight.getY());
		sb.append(",opened=").append(open).append("}");
		return (sb.toString());
	}

	public void addAdjacentRoom(Room room) {
		if (null == adjacentRooms){
			adjacentRooms = new ArrayList<>();
		}
		adjacentRooms.add(room);
	}
}
