package org.copains.spaceexplorer.tactical.events;

import org.copains.spaceexplorer.tactical.objects.Coordinates;
import org.copains.spaceexplorer.tactical.objects.StarshipMap;

public class VisibleMapBlock extends EventBlock {

	private Coordinates mapPosition;
	
	public VisibleMapBlock(int startX, int startY, int endX, int endY) {
		super(startX, startY, endX, endY);
	}

	public boolean isHumanStartZone() {
		StarshipMap map = StarshipMap.getInstance();
        if (map.getRelief(mapPosition.getX(),mapPosition.getY()) == StarshipMap.START)
            return true;
        return false;
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
