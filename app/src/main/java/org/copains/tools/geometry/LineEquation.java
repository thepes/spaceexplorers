package org.copains.tools.geometry;

/**
 * cette classe est un helper permettant de calculer des equations de droites
 * 
 * Pour rappel une equation de droite est de la forme y=ax+b
 * 
 * @author pes
 *
 */
public class LineEquation {

	private float coefA;
	private float coefB;
	
	public LineEquation(int x1, int y1, int x2, int y2) {
		if (x1 == x2)
			return;
		if (y1 == y2) {
			coefA = 0;
			coefB = y1;
		}
		coefA = (y2-y1) / (x2-x1);
		coefB = y1 - coefA * x1;
	}
	
	public int getX(int y) {
		return Math.round((y-coefB)/coefA);
	}
	
	public int getY(int x) {
		return Math.round(coefA*x+coefB);
	}
	
}
