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

// android
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

// java2d
//import java.awt.Graphics2D;
//import java.awt.geom.*;


public class Shape {
	public float x,y,width,height;
	public int r=0, g=0, b=0;
	public Matrix transform = new Matrix(); // android
//	public AffineTransform transform = new AffineTransform(); // java2d
	
	public Shape(float x_, float y_, float w_, float h_) {
		x=x_; y=y_; width=w_; height=h_;
	} 

 	// android
	public void translateBy(float dx, float dy) {
		transform.postTranslate(dx, dy);
	}
	
	public void rotateBy(float dangle, Point center) {
		Matrix t = transform;
		t.postTranslate(-center.x, -center.y);
		t.postRotate((float)(dangle*180/Math.PI));
		t.postTranslate(center.x, center.y);
		//t.concatenate(transform);
		//transform=t;			
	}
	
	
	public void scaleBy(float ds, Point center) {
		Matrix t = transform;
		t.postTranslate(-center.x, -center.y);
		t.postScale((float)(ds), (float)(ds));
		t.postTranslate(center.x, center.y);
		//t.concatenate(transform);
		//transform=t;
	}
	
	public void applyTransform(Canvas canvas) {
		canvas.concat(transform);		
	}
	
	public void draw(Canvas canvas, Paint paint) {
		applyTransform(canvas);
		canvas.drawRect(x, y, x+width, y+height, paint);
		//canvas.translate(x, y);
		//canvas.drawRect(0, 0, width, height, paint);
	}
	

/*
	// java2D
	
	public void translateBy(float dx, float dy) {
		AffineTransform t = new AffineTransform();
		t.translate(dx, dy);
		t.concatenate(transform);
		transform = t;
	}
	
	public void rotateBy(float dangle, Point center) {
		AffineTransform t = new AffineTransform();
		t.translate(center.x, center.y);
		t.rotate((float)(dangle));
		t.translate(-center.x, -center.y);
		t.concatenate(transform);
		transform=t;			
	}
	
	public void scaleBy(float ds, Point center) {		
		AffineTransform t = new AffineTransform();
		t.translate(center.x, center.y);
		t.scale((float)(ds), (float)(ds));
		t.translate(-center.x, -center.y);
		t.concatenate(transform);
		transform=t;
	}
		
	public void applyTransform(Graphics2D g) {
		g.transform(transform);		
	}
	
	public void draw(Graphics2D g) {
		applyTransform(g);
		g.fillRect((int)x, (int)y, (int)(width), (int)(height));
		//canvas.translate(x, y);
		//canvas.drawRect(0, 0, width, height, paint);
	}
*/
	
}
