/**
 * 
 */
package SimCity.Base;


/**
 * @author Brian Chen
 *
 */
public abstract class Role {
	protected boolean active = false;
	protected Person myPerson;
	
	public void setActive(boolean activate){
		active = activate;
	}
	
	protected abstract boolean pickAndExecuteAnAction();
	
	protected abstract void enterBuilding(); //what do you do as soon as you enter the building.
	
	public abstract void workOver(); //what do you do when the work is over.
	
	public void setPerson(Person person){
		myPerson = person;
	}
	
	protected void stateChanged(){
		myPerson.stateChanged();
	}
	
}

