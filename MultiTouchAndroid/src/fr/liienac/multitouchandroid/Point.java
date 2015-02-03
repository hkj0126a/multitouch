/*
 *	The copyright holders for the contents of this file are:
 *		Ecole Nationale de l'Aviation Civile, France (2013)
 *	See file "license.terms" for the rights and conditions
 *	defined by copyright holders.
 *
 *	Contributors:
 *		Stephane Conversy <stephane.conversy@enac.fr>
 *
 */

package fr.liienac.multitouchandroid;

public class Point {
	public float x,y;
	
	public Point(float x_, float y_) {x=x_;y=y_;}
	public boolean equals(Point p) { return (x==p.x) && (y==p.y);}
	
	static public Point plus(Point p1, Point p2) {
		float dx = (p1.x + p2.x);
		float dy = (p1.y + p2.y);
		return new Point(dx,dy);
	}

	static public Point minus(Point p1, Point p2) {
		float dx = (p1.x - p2.x);
		float dy = (p1.y - p2.y);
		return new Point(dx,dy);
	}

	static public Point middle(Point p1, Point p2) {
		float x = (p1.x + p2.x)/2;
		float y = (p1.y + p2.y)/2;
		return new Point(x,y);
	}

	static public Point div(Point p1, float d) {
		float dx = p1.x/d;
		float dy = p1.y/d;
		return new Point(dx,dy);
	}

	static public float distanceSq(Point p1, Point p2) {
		Point d = minus(p1,p2);
		return d.x*d.x + d.y*d.y;
	}

	static public float distance(Point p1, Point p2) {
		return (float)Math.sqrt(distanceSq(p1,p2));
	}

}
