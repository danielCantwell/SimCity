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

	List<DannyWaiter> waiters = new ArrayList<DannyWaiter>();
	List<DannyCustomer> customers = new ArrayList<DannyCustomer>();

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

	}

	@Override
	public void ExitBuilding(Person person) {
		person.resetActiveRoles();
		person.msgExitBuilding();
		for (DannyWaiter waiter : waiters) {
			if (waiter.myPerson == person) {
				waiters.remove(waiter);
			}
		}
		for (DannyCustomer customer : customers) {
			if (customer.myPerson == person) {
				customers.remove(customer);
			}
		}
	}

	public void EnterBuilding(Person person, String job) {
		Role newRole = null;
		try {
			if (job.equals("restaurant.DannyCustomer")) {
				newRole = new DannyCustomer() {
					{
						setHost(hostRole);
					}
				};
				customers.add((DannyCustomer) newRole);
			} else if (job.equals("restaurant.DannyHost")) {
				newRole = hostRole;
				hostFilled = true;
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			} else if (job.equals("restaurant.DannyWaiter")) {
				numWaiters++;
				newRole = new DannyWaiter();
				((DannyWaiter) newRole).setNum(numWaiters);
				setOpen(areAllNeededRolesFilled());
				waiters.add((DannyWaiter) newRole);
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			} else if (job.equals("restaurant.DannyCook")) {
				newRole = cookRole;
				cookFilled = true;
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			} else if (job.equals("restaurant.DannyCashier")) {
				newRole = cashierRole;
				cashierFilled = true;
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			}

			newRole.setActive(true);
			newRole.setPerson(person);
			
			if (areAllNeededRolesFilled()) {
				for (DannyWaiter waiter : waiters) {
					if (waiter.getHost() != hostRole)
						waiter.setHost(hostRole);
					if (waiter.getCashier() != cashierRole)
						waiter.setCashier(cashierRole);
					if (waiter.getCook() != cookRole)
						waiter.setCook(cookRole);
					hostRole.addWaiter(waiter);
				}
			}

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

			person.msgCreateRole(newRole, true);
			fillNeededRoles(person, newRole);
			person.msgEnterBuilding(this);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Building: no class found");
		}
	}

}