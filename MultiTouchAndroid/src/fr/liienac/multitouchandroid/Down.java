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

public class Down extends CanvasEvent {
	public Down(int cursorid_, Point p_, Shape s_, float angRad) { super(/*Type.Down,*/ cursorid_, p_, s_, angRad); }	
}