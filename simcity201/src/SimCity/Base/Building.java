/**
 * 
 */
package SimCity.Base;

import javax.swing.JPanel;
/**
 * @author Brian
 *
 */
public abstract class Building extends SimObject {

//Superclass of all buildings in our simulation.

	protected int x,y;
	protected boolean isOpen = false;
	protected JPanel buildingPanel;
	
	//Use if no jpanel but want to test building assigning ids.
	public Building(){
		super();
	}
	
	//Obsolete constructor.
	public Building(int id){
		this.id = id;
	}
	
	//!!!!USE THIS CONSTRUCTOR.
	public Building(JPanel jp){
		super();
		buildingPanel = jp;
	}
	//Obsolete constructor
	public Building (int id, JPanel jp){
		this.id = id;
		buildingPanel = jp;
	}
	
	public void setOpen(boolean open){
		isOpen = open;
	}
	
	public abstract String getManagerString();
	public abstract String getCustomerString();
	
}

