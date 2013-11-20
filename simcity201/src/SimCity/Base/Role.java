/**
 * 
 */
package SimCity.Base;


/**
 * @author Brian Chen
 *
 */
public abstract class Role {
	protected boolean active;
	protected Person myPerson;
	
	
	protected abstract boolean pickAndExecuteAnAction();
	
	protected abstract void enterBuilding(); //what do you do as soon as you enter the building.
	
	public abstract void workOver(); //what do you do when the work is over.
	
	protected void stateChanged(){
		myPerson.stateChanged();
	}
}

