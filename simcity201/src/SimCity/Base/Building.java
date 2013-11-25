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

	protected int x,y; //x and y position in the world.
	protected boolean isOpen = false; //if the building is closed, then a person cannot go into the building.
	protected JPanel buildingPanel; //The card layout that is associated with the building.
	
	protected String tag = "";
	public String getTag(){return tag;}
	public void setTag(String tag){this.tag = tag;}
	
	//Use if no jpanel but want to test building assigning ids.
	public Building(){
		super();
	}
	
	//Obsolete constructor.
	public Building(int id){
		this.id = id;
	}
	
	//!!!!USE THIS CONSTRUCTOR.
	public Building(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
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
	
	
	//BUILDING MEDIATOR STUFF
    public void EnterBuilding(Person person, String job){
    		Role newRole;
			try {
				newRole = (Role)Class.forName(job).newInstance();
				newRole.setActive(true);
				newRole.setPerson(person);
				person.msgCreateRole(newRole, true);
			} catch(Exception e){
				e.printStackTrace();
				System.out.println ("God: no class found");
			}
			person.msgEnterBuilding(this);
    }
    
    public void ExitBuilding(Person person){
    	person.resetActiveRoles();
    	person.msgExitBuilding();
    }
    
	
}

