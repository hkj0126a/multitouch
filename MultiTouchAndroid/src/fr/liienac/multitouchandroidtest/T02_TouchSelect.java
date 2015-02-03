package fr.liienac.multitouchandroidtest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import fr.liienac.multitouchandroid.CanvasEvent;
import fr.liienac.multitouchandroid.ColorPicking;
import fr.liienac.multitouchandroid.Down;
import fr.liienac.multitouchandroid.Drag;
import fr.liienac.multitouchandroid.Point;
import fr.liienac.multitouchandroid.Shape;
import fr.liienac.multitouchandroid.StateMachineCanvas;
import fr.liienac.multitouchandroid.Up;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.support.v4.view.MotionEventCompat;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

public class T02_TouchSelect extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(new MyView(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public class MyView extends View {

		class Cursor {
			public Point p;
			public long id;
			int r,g,b;
		}
		
		Map<Long, Cursor> cursors = new HashMap<Long, Cursor>();
		
		Collection<Shape> sceneGraph = new Vector<Shape>();
		ColorPicking colorPicking = new ColorPicking();
		Vector<StateMachineCanvas> machines = new Vector<StateMachineCanvas>();	

		public MyView(Context c) {
			super(c);
			
			// cache paints to avoid recreating them at each draw
			paint = new Paint();
			pickingPaint = new Paint();
			pickingPaint.setAntiAlias(false);
			
			StateMachineCanvas machine;
			Shape shape;

			shape = new Shape(0, 0, 300, 300);
			sceneGraph.add(shape);
			machine = new SelectMachine();
			machines.add(machine);
			
			Shape shape2 = new Shape(400, 400, 250, 250);
			sceneGraph.add(shape2);
			StateMachineCanvas machine2 = new SelectMachine();
			machines.add(machine2);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			//colorPicking.onSizeChanged(w,h,oldw,oldh);
			colorPicking.onSizeChanged(w,h);
		}

		// cache paints to avoid recreating them at each draw
		Paint paint;
		Paint pickingPaint;
		
		@Override
		protected void onDraw(Canvas canvas) {

			// "erase" canvas (fill it with white)
			canvas.drawColor(0xFFAAAAAA);
			colorPicking.reset();
			
			// draw scene graph
			for(Shape shape : sceneGraph) {
				// Display view
				canvas.save();
				paint.setARGB(255, shape.r, shape.g, shape.b);
				shape.draw(canvas,  paint);
				canvas.restore();
				
				// Picking view
				colorPicking.canvas.save();
				colorPicking.newColor(shape, pickingPaint);				
				shape.draw(colorPicking.canvas,  pickingPaint);
				colorPicking.canvas.restore();
			}
			
			if (false) { // debug: show picking view
				canvas.drawBitmap(colorPicking.bitmap, 0, 0, paint);
			}

			// draw cursors
			paint.setARGB(100, 0, 0, 0);
			for (Map.Entry<Long, Cursor> entry : cursors.entrySet()) {
				Cursor c = entry.getValue();
				canvas.drawCircle(c.p.x, c.p.y, 50, paint);
				canvas.drawText(""+c.id, c.p.x+30, c.p.y-30, paint);
			}
		}

		class SelectMachine extends StateMachineCanvas {
			Shape shape = null;
			long cursor = -1;
			long cursorId = -1;
			Point depart = null;
			SelectMachine() {}

			public long getCursorId() {
				return cursorId;
			}
			
			public State start = new  State() {
				Transition press = new Transition(Down.class) {
					public boolean guard(CanvasEvent evt) {
						return evt.getShape()!=null;
					}
					public void action(CanvasEvent evt) {
						//System.out.println("yep");
						shape = evt.getShape();
						cursor = evt.getCursorID();
						shape.r=255;
						cursorId = evt.getCursorID();
						depart = new Point(evt.getPoint().x,evt.getPoint().y);
					}
					public State goTo() { return touched; }
				};
				
				
			};

			public State touched = new State() {		    	
				Transition release = new Transition(Up.class) {
					public void action(CanvasEvent evt) {
						//System.out.println("yep");
						shape = evt.getShape();
						cursor = evt.getCursorID();
						if(shape != null) {
							shape.r=0;			
						}
						cursorId = -1;
					}
					public State goTo() { return start; }
				};
				
				Transition drag = new Transition(Drag.class) {
					private Shape savedShape;
					public void action(CanvasEvent evt) {						
						shape = evt.getShape();
						cursor = evt.getCursorID();						
						float distanceX = evt.getPoint().x - depart.x;
						float distanceY = evt.getPoint().y - depart.y;
						shape.translateBy(distanceX, distanceY);
						depart = evt.getPoint();
					}
					public State goTo() { return touched; }
				};
			};
			


		}

		
//-----------------------------------------
		
		private void onTouchDown(Point p, int cursorid) {
			//System.out.println("down "+cursorid + " " + p.x +" "+p.y);
			Shape s = (Shape)colorPicking.pick(p);
			CanvasEvent evt = new Down(cursorid, p, s, 0);
			boolean machineTrouvee = false;
			for (StateMachineCanvas m : machines) {	
				System.out.println("machine " + machines.indexOf(m) + " cursorId = " + ((SelectMachine) m).getCursorId());				
				if(((SelectMachine) m).getCursorId() == -1 && !machineTrouvee) {										
					m.handleEvent(Down.class, evt);
					machineTrouvee = true;
				}
			}
			
			Cursor c = new Cursor();
			c.p=p; c.id=cursorid;
			c.r=(int)Math.floor(Math.random()*100);
			c.g=(int)Math.floor(Math.random()*100);
			c.b=(int)Math.floor(Math.random()*100);
			cursors.put(Long.valueOf(c.id),c);
			invalidate();
		}

		private void onTouchMove(Point p, int cursorid) {
			//System.out.println("move "+cursorid+ " " +p.x+" "+p.y);
			Cursor c = cursors.get(Long.valueOf(cursorid));
			
			if(Point.distance(c.p, p)>0){
				Shape s = (Shape)colorPicking.pick(p);
				CanvasEvent evt = new Drag(cursorid, p, s, 0);
				for (StateMachineCanvas m : machines) {
					if(((SelectMachine) m).getCursorId() == cursorid) {
						m.handleEvent(Drag.class, evt);
					}
				}
				c.p = p;
			}
			
			invalidate();
		}

		private void onTouchUp(Point p, int cursorid) {
			//System.out.println("up "+cursorid);
			Shape s = (Shape)colorPicking.pick(p);
			CanvasEvent evt = new Up(cursorid, p, s, 0);
			for (StateMachineCanvas m : machines) {
				if(((SelectMachine) m).getCursorId() == cursorid) {
					m.handleEvent(Up.class, evt);
				}
			}

			cursors.remove(Long.valueOf(cursorid));
			invalidate();
		}

		
		//Convertie les event android en 
		//évènements pour le picking avec les bon paramètres
		//Crée les curseurs et l'envoie sur la machine (couleur aléatoire pour trouver les faux relachés
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//System.out.println("=> TouchEvent");
			System.out.println("---");
			//Compatibilité pour les multi touch
			int action = MotionEventCompat.getActionMasked(event);
			int index = MotionEventCompat.getActionIndex(event);
			int id = MotionEventCompat.getPointerId(event, index);
			float x,y;

			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				x = MotionEventCompat.getX(event, index);
				y = MotionEventCompat.getY(event, index);
				onTouchDown(new Point(x,y),id);
				break;
			case MotionEvent.ACTION_MOVE:
				//x = MotionEventCompat.getX(event, index);
				//y = MotionEventCompat.getY(event, index);
				//onTouchMove(new Point(x,y),id);
				for (int i=0; i<event.getPointerCount(); ++i) {
					x = MotionEventCompat.getX(event, i);
					y = MotionEventCompat.getY(event, i);
					id = MotionEventCompat.getPointerId(event, i);				
					// event sent though there may be no move for that particular touch
					//System.out.println("------MoveEvent");
					//x=(float)Math.floor(x/15)*15;
					//y=(float)Math.floor(y/15)*15;
					onTouchMove(new Point(x,y),id);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				x = MotionEventCompat.getX(event, index);
				y = MotionEventCompat.getY(event, index);
				onTouchUp(new Point(x,y),id);
				break;
			}
			//System.out.println("<= TouchEvent");
			return true;
		}
		
		@Override
		public boolean onHoverEvent(MotionEvent event) {
			//System.out.println("=> TouchEvent");
			System.out.println("---");
			int action = MotionEventCompat.getActionMasked(event);
			int index = MotionEventCompat.getActionIndex(event);
			int id = MotionEventCompat.getPointerId(event, index);
			float x,y;

			switch (action) {
			case MotionEvent.ACTION_HOVER_ENTER:
				x = MotionEventCompat.getX(event, index);
				y = MotionEventCompat.getY(event, index);
				onTouchDown(new Point(x,y),id);
				break;
			case MotionEvent.ACTION_HOVER_MOVE:
				//x = MotionEventCompat.getX(event, index);
				//y = MotionEventCompat.getY(event, index);
				//onTouchMove(new Point(x,y),id);
				for (int i=0; i<event.getPointerCount(); ++i) {
					x = MotionEventCompat.getX(event, i);
					y = MotionEventCompat.getY(event, i);
					id = MotionEventCompat.getPointerId(event, i);				
					// event sent though there may be no move for that particular touch
					//System.out.println("------MoveEvent");
					//x=(float)Math.floor(x/15)*15;
					//y=(float)Math.floor(y/15)*15;
					onTouchMove(new Point(x,y),id);
				}
				break;
			case MotionEvent.ACTION_HOVER_EXIT:
				x = MotionEventCompat.getX(event, index);
				y = MotionEventCompat.getY(event, index);
				onTouchUp(new Point(x,y),id);
				break;
			}
			//System.out.println("<= TouchEvent");
			return true;
		}

	}	
}


