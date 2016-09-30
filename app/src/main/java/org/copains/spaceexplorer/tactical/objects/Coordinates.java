package org.copains.spaceexplorer.tactical.objects;

public class Coordinates {

	private int x;
	private int y;
	
	private int boundsX = -1;
	private int boundsY = -1;
	
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinates() {

	}

	public void setBounds(int x, int y) {
		boundsX = x;
		boundsY = y;
	}
	
	public void add(int x, int y) {
		this.x += x;
		this.y += y;
		if (this.x < 0)
			this.x = 0;
		if (this.y < 0)
			this.y = 0;
		if (boundsX != -1)
			if (this.x > boundsX)
				this.x = boundsX;
		if (boundsY != -1)
			if (this.y > boundsY)
				this.y = boundsY;
				
	}
	
	public boolean isInside(Coordinates start, Coordinates end) {
		return ((x >= start.getX()) && (x <= end.getX()) && (y >= start.getY()) && (y <= end.getY()));
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}


	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
