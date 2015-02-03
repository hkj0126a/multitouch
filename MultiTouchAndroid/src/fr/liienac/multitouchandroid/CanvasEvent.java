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

public class CanvasEvent {
	//static enum Type { Down, Drag, Up };
	//Type type;
	int cursorid;
	Point p;
	Shape shape;
	float orientation;
	CanvasEvent(/*Type t_,*/ int cursorid_, Point p_, Shape s_) { /*type=t_;*/ cursorid=cursorid_; p=p_; shape=s_; orientation=0;}
	CanvasEvent(/*Type t_,*/ int cursorid_, Point p_, Shape s_, float o_) { /*type=t_;*/ cursorid=cursorid_; p=p_; shape=s_; orientation=o_;}
	
	// are they really necessary...
	public Point getPoint() { return p; }
	public Shape getShape() { return shape; }
	public int getCursorID() { return cursorid; }
	public float getOrientation() { return orientation; }
}

