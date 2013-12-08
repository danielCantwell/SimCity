package SimCity.Buildings;
import javax.swing.JPanel;

import EricRestaurant.EricCashier;
import EricRestaurant.EricCook;
import EricRestaurant.EricCustomer;
import EricRestaurant.EricHost;
import EricRestaurant.EricWaiter;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;
import EricRestaurant.interfaces.*;
/***
 * 
 * @author Eric
 *
 */
public class B_EricRestaurant extends Building {
	public int B_EricAccNum = 1000;
	public EricHost host = new EricHost("Host");
	public EricCook cook = new EricCook("Cook");
	public EricCashier cashier = new EricCashier();
	public int numWaiter = 0;
	
//	Person pHost = null;
//	Person pWaiter = null;
//	Person pCashier = null;
	
	public boolean hostFilled = false, cashierFilled = false, cookFilled = false;

	
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


	@Override
	public boolean areAllNeededRolesFilled() {
		System.out.println("Eric Restaurant Roles Filled? Host: "+hostFilled+"  Cook: "+cookFilled+"  Cashier: "+cashierFilled+"  Waiters: "+numWaiter);
		return hostFilled && cashierFilled && cookFilled && numWaiter > 0;
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

	public EricCashier getCashier() {
		return cashier;
	}
	@Override
	protected void fillNeededRoles(Person p, Role r) {
		// TODO Auto-generated method stub

	}
	@Override
	public void EnterBuilding(Person person, String role) {
		Role newRole = null;
		try {
			if(role.equals("EricRestaurant.EricHost")) { 
				newRole = host;
				hostFilled = true;
				setOpen(areAllNeededRolesFilled());
				}
			else if(role.equals("EricRestaurant.EricWaiter")) {
				newRole = new EricWaiter("Waiter", host, cashier);
				host.newWaiter((EricWaiter) newRole);
				numWaiter++;
				setOpen(areAllNeededRolesFilled());
			}
			else if(role.equals("EricRestaurant.EricCook")) { 
				newRole = cook;
				host.setCook(cook);
				cookFilled = true;
				setOpen(areAllNeededRolesFilled());
			}
			else if(role.equals("EricRestaurant.EricCashier")) {
				newRole = cashier;
				cashierFilled = true;
				setOpen(areAllNeededRolesFilled());
			}
			else if(role.equals("EricRestaurant.EricCustomer")) {
				newRole = new EricCustomer("Customer");
				System.out.println("Customer Made");
			}
			newRole.setActive(true);
			newRole.setPerson(person);
			person.msgCreateRole(newRole, true);
			fillNeededRoles(person, newRole);
			person.msgEnterBuilding(this);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println ("Building: no class found");
		}
	}

	@Override
	public void ExitBuilding(Person person) {
		person.resetActiveRoles();
    	person.msgExitBuilding();
	}

}
