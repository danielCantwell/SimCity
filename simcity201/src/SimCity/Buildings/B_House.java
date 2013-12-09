package SimCity.Buildings;

import housing.roles.OwnerRole;

import javax.swing.JPanel;

import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Person.Morality;
import SimCity.Base.Person.Vehicle;
import SimCity.Base.Role;
import SimCity.Globals.Money;

/**
 * @author Brian
 * 
 */
public class B_House extends Building {

	public OwnerRole owner;
	public String type;
	
	public int numTenants = 0;

	public void setOwner(OwnerRole o) {
		owner = o;
	}

	public OwnerRole getOwner() {
		return owner;
	}
	
	public void incrementNumTenants() {
		numTenants++;
	}

	public B_House(int id, JPanel jp) {
		super(id, jp);
		tag = "B_House";
	}

	public B_House(JPanel jp) {

	}

	public B_House(String type, int id, JPanel jp, int xCoord, int yCoord) {
		this.type = type;
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_House";

		Person p = new Person("Owner", null, "housing.roles.OwnerRole",
				Vehicle.walk, Morality.good, new Money(100, 0),
				new Money(10, 0), 10, 10, type, this, this, 1);

		owner = new OwnerRole();
		owner.setPerson(p);
		owner.myPerson.setHomeType(type);
		
	}

	@Override
	public String getManagerString() {
		return "housing.roles.OwnerRole";
	}

	@Override
	public String getCustomerString() {
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
		person.resetActiveRoles();
		person.msgExitBuilding();

	}

	@Override
	public Role getManagerRole() {
		// TODO Auto-generated method stub
		return null;
	}

}