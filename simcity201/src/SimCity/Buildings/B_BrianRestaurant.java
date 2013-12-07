package SimCity.Buildings;

import javax.swing.JPanel;

import brianRest.BrianCashierRole;
import brianRest.BrianCookRole;
import brianRest.BrianCustomerRole;
import brianRest.BrianHostRole;
import brianRest.BrianWaiterRole;
import brianRest.gui.BrianAnimationPanel;
import brianRest.interfaces.BrianCashier;
import brianRest.interfaces.BrianHost;
import restaurant.DannyCashier;
import restaurant.DannyCustomer;
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
public class B_BrianRestaurant extends Building{
	
	public BrianHostRole hostRole = new BrianHostRole("Host");
	public BrianCashierRole cashierRole = new BrianCashierRole("Cashier");
	public BrianCookRole cookRole = new BrianCookRole("Cook");
	public int numberOfWaiters = 0;
	
	public boolean hostFilled = false, cashierFilled = false, cookFilled = false;

	public B_BrianRestaurant(int id, JPanel jp) {
		super(id, jp);
		
	}
	
	public B_BrianRestaurant(int id, JPanel jp, int xCoord, int yCoord){
		this.id = id;
		buildingPanel = jp;
		BrianAnimationPanel bap = (BrianAnimationPanel)jp;
		bap.setRestaurant(this);
		x = xCoord;
		y = yCoord;
		tag = "B_Restaurant";
	}

	@Override
	public String getManagerString() {
		return "brianRest.BrianCustomerRole";
	}
	
	public BrianCashier getCashier(){return cashierRole;}

	@Override
	public String getCustomerString() {
		// TODO Auto-generated method stub
		return "brianRest.BrianCustomerRole";
	}

	@Override
	public boolean areAllNeededRolesFilled() {
		// TODO Auto-generated method stub
		System.out.println("ALL ROLES NEEDED BRIAN REST: " + hostFilled); 
		return hostFilled && cashierFilled && cookFilled && numberOfWaiters > 0;
	}

	@Override
	protected void fillNeededRoles(Person p, Role r) {
			//Should be handled by the roles themselves right now.
	}

	@Override
	public void ExitBuilding(Person person) {
		person.resetActiveRoles();
    	person.msgExitBuilding();
	}
	
	@Override
	public void EnterBuilding(Person person, String job){
		Role newRole = null;
		try {
			if (job.equals("brianRest.BrianCustomerRole")) {
				newRole = new BrianCustomerRole("Customer");  
				System.out.println("#cust created");}
			else if (job.equals("brianRest.BrianHostRole")) {
				newRole = hostRole; 
				hostFilled = true;
				setOpen(areAllNeededRolesFilled());}
			else if (job.equals("brianRest.BrianWaiterRole")){ 
				numberOfWaiters++; 
				newRole = new BrianWaiterRole("Waiter", hostRole, cookRole, cashierRole, numberOfWaiters); 
				hostRole.addWaiter((BrianWaiterRole)newRole);
				setOpen(areAllNeededRolesFilled());}
			else if (job.equals("brianRest.BrianCookRole")) { 
				newRole = cookRole; 
				cookFilled = true;
				setOpen(areAllNeededRolesFilled());}
			else if (job.equals("brianRest.BrianCashierRole")) { 
				newRole = cashierRole; 
				cashierFilled = true;
				setOpen(areAllNeededRolesFilled());}
			newRole.setActive(true);
			newRole.setPerson(person);
			person.msgCreateRole(newRole, true);
			person.msgEnterBuilding(this);
		} catch(Exception e){
			e.printStackTrace();
			System.out.println ("Building: no class found");
		}
}



}