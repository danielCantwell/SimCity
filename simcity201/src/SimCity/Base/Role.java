/**
 * 
 */
package SimCity.Base;

import SimCity.gui.Gui;

/**
 * @author Brian
 *
 */
public abstract class Role {
	protected boolean active = false;
	public Person myPerson;
	
	public void setActive(boolean activate){active = activate;}
	public boolean getActive(){return active;}
	
	protected abstract boolean pickAndExecuteAnAction();
	
	protected abstract void enterBuilding(); //what do you do as soon as you enter the building.
	protected void exitBuilding(Person p){
		p.building.ExitBuilding(p);
	}
	
	public abstract void workOver(); //what do you do when the work is over.
	
	public void setPerson(Person person){
		myPerson = person;
	}
	
	protected void stateChanged(){
		myPerson.stateChanged();
	}
	
	protected void Do(String s){
		System.out.println(this.getClass().toString() + s);
	}
}

