package org.copains.spaceexplorer.tactical.events;

import android.util.Log;

public abstract class EventBlock {

	private int x1,x2;
	private int y1,y2;
	
	public EventBlock(int startX, int startY, int endX, int endY ){
		x1 = startX;
		x2 = endX;
		y1 = startY;
		y2 = endY;
	}
	
	public boolean isTargeted(float x, float y) {
		return ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2));
	}
	
	@Override
	public boolean equals(Object o) {
		Log.i("Space","dans EventBlock Equals");
		EventBlock eb = (EventBlock)o;
		return ((x1 == eb.x1) && (x2 == eb.x2) && (y1 == eb.y1) && (y2 == eb.y2));
	}
	
}
