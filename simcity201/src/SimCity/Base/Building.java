/**
 * 
 */
package SimCity.Base;


/**
 * @author Brian Chen
 *
 */
public abstract class Building extends SimObject {

//Superclass of all buildings in our simulation.

	int x,y;
	boolean isOpen = false;
	
	public Building (int id){
		this.id = id;
	}
	
	public void setOpen(boolean open){
		isOpen = open;
	}
}

