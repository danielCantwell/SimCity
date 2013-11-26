package SimCity.Buildings;

import javax.swing.JPanel;

import restaurant.DannyHost;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
/**
 * @author Brian
 *
 */
public class B_DannyRestaurant extends Building{
	
	public DannyHost hostRole;

	public B_DannyRestaurant(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}
	
	public B_DannyRestaurant(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_Restaurant";
	}

	@Override
	public String getManagerString() {
		return "restaurant.DannyHost";
	}

	@Override
	public String getCustomerString() {
		// TODO Auto-generated method stub
		return "restaurant.DannyCustomer";
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