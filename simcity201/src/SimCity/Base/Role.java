/**
 * 
 */
package SimCity.Base;

import SimCity.gui.Gui;
import SimCity.trace.AlertLog;
import SimCity.trace.AlertTag;

/**
 * @author Brian
 *
 */
public abstract class Role {
	protected boolean active = false;
	public Person myPerson;
	
	public Person getPerson(){return myPerson;}
	public void setActive(boolean activate){active = activate;}
	public boolean getActive(){return active;}
	
	protected abstract boolean pickAndExecuteAnAction();
	
	protected abstract void enterBuilding(); //what do you do as soon as you enter the building.
	protected void exitBuilding(Person p){
		p.building.ExitBuilding(p);
	}

	public abstract String toString();
	
	public abstract void workOver(); //what do you do when the work is over.
	
	public void setPerson(Person person){
		myPerson = person;
	}
	
	protected void stateChanged(){
		myPerson.stateChanged();
	}
	
	protected void Do(AlertTag a, String s){
    	AlertLog.getInstance().logMessage(AlertTag.God, myPerson.getName(), toString() + " "+s);

	}
}

