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
	
	public Building (int id){
		this.id = id;
	}
	
	public abstract boolean canAdd(String role);
}

