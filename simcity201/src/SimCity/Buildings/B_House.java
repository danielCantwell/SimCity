package SimCity.Buildings;

import housing.roles.OwnerRole;

import javax.swing.JPanel;

import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
/**
 * @author Brian
 *
 */
public class B_House extends Building{

	OwnerRole owner;
	
	public void setOwner(OwnerRole o){
		owner = o;
	}
	public OwnerRole getOwner(){
		return owner;
	}
	
	public B_House(int id, JPanel jp) {
		super(id, jp);
		tag = "B_House";
	}
	
	public B_House(JPanel jp){
		
	}
	
	public B_House(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_House";
	}

	@Override
	public String getManagerString() {
		// TODO Auto-generated method stub
		return "housing.roles.OwnerRole";
	}

	@Override
	public String getCustomerString() {
		// TODO Auto-generated method stub
		return "housing.roles.TenantRole";
	}
	@Override
	public boolean areAllNeededRolesFilled() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void fillNeededRoles(Person p, Role r) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void ExitBuilding(Person person) {
		// TODO Auto-generated method stub
		
	}

	

}