package SimCity.Buildings;

import javax.swing.JPanel;

import restaurant.DannyCashier;
import restaurant.DannyCook;
import restaurant.DannyCustomer;
import restaurant.DannyHost;
import restaurant.DannyWaiter;
import restaurant.gui.DannyRestaurantAnimationPanel;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;

/**
 * @author Brian
 * 
 */
public class B_DannyRestaurant extends Building {

	public DannyHost hostRole = new DannyHost("Host");
	public DannyCashier cashierRole = new DannyCashier("Cashier");
	public DannyCook cookRole = new DannyCook("Cook", hostRole);

	public int numWaiters = 0;

	public boolean hostFilled = false;
	public boolean cookFilled = false;
	public boolean cashierFilled = false;

	public B_DannyRestaurant(int id, JPanel jp) {
		super(id, jp);
	}

	public B_DannyRestaurant(int id, JPanel jp, int xCoord, int yCoord) {
		this.id = id;
		buildingPanel = jp;
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) jp;
		ap.setRestaurant(this);
		x = xCoord;
		y = yCoord;
		tag = "B_Restaurant";
	}

	@Override
	public String getManagerString() {
		return "restaurant.DannyHost";
	}

	public DannyCashier getCashier() {
		return cashierRole;
	}

	@Override
	public String getCustomerString() {
		return "restaurant.DannyCustomer";
	}

	@Override
	public boolean areAllNeededRolesFilled() {
		return hostFilled && cookFilled && cashierFilled && numWaiters > 0;
	}

	@Override
	protected void fillNeededRoles(Person p, Role r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ExitBuilding(Person person) {
		person.resetActiveRoles();
		person.msgExitBuilding();
	}

	public void EnterBuilding(Person person, String job) {
		Role newRole = null;
		try {
			if (job.equals("restaurant.DannyCustomer")) {
				newRole = new DannyCustomer("Customer");
			} else if (job.equals("restaurant.DannyHost")) {
				newRole = hostRole;
				setOpen(areAllNeededRolesFilled());
			} else if (job.equals("restaurant.DannyWaiter")) {
				newRole = new DannyWaiter("Waiter");
				setOpen(areAllNeededRolesFilled());
			} else if (job.equals("restaurant.DannyCook")) {
				newRole = cookRole;
				setOpen(areAllNeededRolesFilled());
			} else if (job.equals("restaurant.DannyCashier")) {
				newRole = cashierRole;
				setOpen(areAllNeededRolesFilled());
			}
			
			newRole.setActive(true);
			newRole.setPerson(person);
			person.msgCreateRole(newRole, true);
			fillNeededRoles(person, newRole);
			person.msgEnterBuilding(this);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Building: no class found");
		}
	}

}