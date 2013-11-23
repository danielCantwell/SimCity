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
	
	public void setActive(boolean activate){active = activate;System.out.println(active);}
	public boolean getActive(){return active;}
	
	protected abstract boolean pickAndExecuteAnAction();
	
	protected abstract void enterBuilding(); //what do you do as soon as you enter the building.
	
	public abstract void workOver(); //what do you do when the work is over.
	
	public void setPerson(Person person){
		myPerson = person;
	}
	
	protected void stateChanged(){
		System.out.println("ROLE: myPerson is " + myPerson.toString());
		System.out.println(myPerson.roles.get(0).toString());
		myPerson.stateChanged();
	}
	
}

