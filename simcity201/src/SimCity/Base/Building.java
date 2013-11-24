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
	
	public Building(){
		
	}
	
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

