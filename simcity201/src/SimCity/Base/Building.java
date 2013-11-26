/**
 * 
 */
package SimCity.Base;

import housing.gui.HousingAnimation;
import housing.gui.TenantGui;

import javax.swing.JPanel;

import restaurant.gui.DannyRestaurantAnimationPanel;
import market.gui.MarketAnimationPanel;
import SimCity.Buildings.B_House;
import SimCity.gui.Gui;
/**
 * @author Brian
 *
 */
public abstract class Building extends SimObject {

//Superclass of all buildings in our simulation.

	protected int x,y; //x and y position in the world.
	protected boolean isOpen = false; //if the building is closed, then a person cannot go into the building.
	protected JPanel buildingPanel; //The card layout that is associated with the building.
	
	public JPanel getPanel() {return buildingPanel;}
	
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
	
	public abstract boolean areAllNeedeRolesFilled();
	
	public abstract String getManagerString();
	public abstract String getCustomerString();
	
	
	//BUILDING MEDIATOR STUFF
    public void EnterBuilding(Person person, String job){
    		Role newRole = null;
			try {
				newRole = (Role)Class.forName(job).newInstance();
				newRole.setActive(true);
				newRole.setPerson(person);
				person.msgCreateRole(newRole, true);
				fillNeededRoles(person, newRole);
				person.msgEnterBuilding(this);
			} catch(Exception e){
				e.printStackTrace();
				System.out.println ("God: no class found");
			}		
    }
    
    protected abstract void fillNeededRoles(Person p, Role r);
    
    public abstract void ExitBuilding(Person person);

}

