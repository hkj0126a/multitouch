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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StateMachine<EventType, Event> {
	
	protected State current = null;
	protected State first = null;

	public void handleEvent(EventType type, Event evt) {
		current.handleEvent(type, evt);
	}
	
	private void goTo(State s) {
		current.leave();
		current = s;
		current.enter();
	}	

	public class State {

		Map<EventType, Vector<Transition> > transitionsPerType = new HashMap<EventType, Vector<Transition>>();
		
		public State() {
			// first state is the initial state
			if (first==null) { first = this; current = first; }
		}				
		//public State(boolean notfirst) {}
		
		protected void enter() {}
		protected void leave() {}
		
		public class Transition {
			//EventType eventType;
			public Transition(EventType eventType) {
				//eventType = evt;
				// register transition in state
				Transition t = this;
				Vector<Transition> ts = transitionsPerType.get(eventType);
				if (ts==null) {
					ts=new Vector<Transition>();
					ts.add(t);
					transitionsPerType.put(eventType, ts);
				} else {
					ts.add(t);
				}
			}
			protected boolean guard(Event evt) { return true; }			
			protected void action(Event evt) {}
			protected State goTo() { return current; }		
		}

		public void handleEvent(EventType type, Event evt) {
			Vector<Transition> ts = transitionsPerType.get(type);
			if (ts==null) { return; }
			for (Transition t : ts) {
				if(t.guard(evt)) {
					t.action(evt);
					StateMachine.this.goTo(t.goTo());
					break;
				}
			}					
		}	
	}

	//public State finished = new State(true) {};
	//public boolean isFinished() { return current==finished; }
}


