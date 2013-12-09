package SimCity.Buildings;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import restaurant.DannyCashier;
import restaurant.DannyCook;
import restaurant.DannyCustomer;
import restaurant.DannyHost;
import restaurant.DannyWaiter;
import restaurant.gui.DannyRestaurantAnimationPanel;
import SimCity.Base.Building;
import SimCity.Base.Person;
import SimCity.Base.Role;

/**
 * @author Brian
 * 
 */
public class B_DannyRestaurant extends Building {

	public DannyHost hostRole = new DannyHost();
	public DannyCashier cashierRole = new DannyCashier();
	public DannyCook cookRole = new DannyCook();

	public int numWaiters = 0;
	public int numCustomers = 0;

	public boolean hostFilled = false;
	public boolean cookFilled = false;
	public boolean cashierFilled = false;

	public B_DannyRestaurant(int id, JPanel jp) {
		super(id, jp);
	}

	public B_DannyRestaurant(int id, JPanel jp, int xCoord, int yCoord) {
		this.id = id;
		buildingPanel = jp;
		System.out.println("Danny Rest Building Panel " + buildingPanel.hashCode());
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

	}

	@Override
	public void ExitBuilding(Person person) {
		person.resetActiveRoles();
		person.msgExitBuilding();
		if (person.getMainRole() instanceof DannyCustomer) {
			numCustomers--;
		}
	}

	public void EnterBuilding(Person person, String job) {
		Role newRole = null;
		try {
			if (job.equals("restaurant.DannyCustomer")) {
				//newRole = new DannyCustomer();
				newRole = (DannyCustomer) person.getMainRole();
				((DannyCustomer) newRole).setHost(hostRole);
				numCustomers++;
			} else if (job.equals("restaurant.DannyHost")) {
				//newRole = hostRole;
				newRole = (DannyHost) person.getMainRole();
				hostRole = (DannyHost) newRole;
				hostFilled = true;
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			} else if (job.equals("restaurant.DannyWaiter")) {
				numWaiters++;
				//newRole = new DannyWaiter();
				newRole = (DannyWaiter) person.getMainRole();
				hostRole.addWaiter((DannyWaiter) newRole);
				((DannyWaiter) newRole).setNum(numWaiters);
				((DannyWaiter) newRole).setHost(hostRole);
				((DannyWaiter) newRole).setCook(cookRole);
				((DannyWaiter) newRole).setCashier(cashierRole);
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			} else if (job.equals("restaurant.DannyCook")) {
				//newRole = cookRole;
				newRole = (DannyCook) person.getMainRole();
				cookRole = (DannyCook) newRole;
				cookFilled = true;
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			} else if (job.equals("restaurant.DannyCashier")) {
				//newRole = cashierRole;
				newRole = (DannyCashier) person.getMainRole();
				cashierRole = (DannyCashier) person.getMainRole();
				cashierFilled = true;
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			}

			newRole.setActive(true);
			newRole.setPerson(person);

			person.msgCreateRole(newRole, true);
			fillNeededRoles(person, newRole);
			person.msgEnterBuilding(this);

			if (newRole instanceof DannyHost)
				((DannyHost) newRole).setName(newRole.myPerson.name);
			if (newRole instanceof DannyWaiter)
				((DannyWaiter) newRole).setName(newRole.myPerson.name);
			if (newRole instanceof DannyCashier)
				((DannyCashier) newRole).setName(newRole.myPerson.name);
			if (newRole instanceof DannyCook)
				((DannyCook) newRole).setName(newRole.myPerson.name);
			if (newRole instanceof DannyCustomer)
				((DannyCustomer) newRole).setName(newRole.myPerson.name);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Building: no class found");
		}
	}

}