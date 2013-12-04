package SimCity.Buildings;
import javax.swing.JPanel;

import EricRestaurant.EricCashier;
import EricRestaurant.EricCook;
import EricRestaurant.EricHost;
import EricRestaurant.EricWaiter;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
import EricRestaurant.interfaces.*;

public class B_EricRestaurant extends Building {
	
	public Host host;
	public Waiter waiter;
	public Cashier cashier;
	
	Person pHost = null;
	Person pWaiter = null;
	Person pCashier = null;
	
	public B_EricRestaurant(int id, JPanel jp) {
		super(id, jp);
		// TODO Auto-generated constructor stub
	}
	
	public B_EricRestaurant(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		x = xCoord;
		y = yCoord;
		tag = "B_EricRestaurant";
	}

	public void EnterBuilding(Person person, String role) {
		
	}
	@Override
	public boolean areAllNeededRolesFilled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getManagerString() {
		// TODO Auto-generated method stub
		return "EricRestaurant.EricHost";
	}

	@Override
	public String getCustomerString() {
		// TODO Auto-generated method stub
		return "EricRestaurant.EricCustomer";
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
