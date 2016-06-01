package org.copains.spaceexplorer.tactical.events;

import org.copains.spaceexplorer.tactical.objects.Coordinates;

public class VisibleMapBlock extends EventBlock {

	private Coordinates mapPosition;
	
	public VisibleMapBlock(int startX, int startY, int endX, int endY) {
		super(startX, startY, endX, endY);
	}
	
	public void setMapPosition(Coordinates c) {
		mapPosition = c;
	}

	/**
	 * @return the mapPosition
	 */
	public Coordinates getMapPosition() {
		return mapPosition;
	}

}
