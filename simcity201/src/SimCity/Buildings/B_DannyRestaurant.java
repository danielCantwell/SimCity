package SimCity.Buildings;

import javax.swing.JPanel;

import market.MarketDeliveryPersonRole;
import restaurant.DannyAbstractWaiter;
import restaurant.DannyCashier;
import restaurant.DannyCook;
import restaurant.DannyCustomer;
import restaurant.DannyHost;
import restaurant.DannyPCWaiter;
import restaurant.DannyWaiter;
import restaurant.OrderStand;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.God.BuildingType;
import SimCity.Base.Person.Intent;
import SimCity.Base.Role;
import SimCity.Globals.Money;

/**
 * @author Danny
 * 
 */
public class B_DannyRestaurant extends Building {
	public int B_DannyAccNum = 4000;
	Money DRestMoney = new Money(700, 0);
	public DannyHost hostRole = new DannyHost();
	public DannyCashier cashierRole = new DannyCashier();
	public DannyCook cookRole = new DannyCook();

	public int numWaiters = 0;
	public int numCustomers = 0;

	public boolean hostFilled = false;
	public boolean cookFilled = false;
	public boolean cashierFilled = false;

	public OrderStand orderStand;

	public B_DannyRestaurant(int id, JPanel jp) {
		super(id, jp);
	}

	public B_DannyRestaurant(int id, JPanel jp, int xCoord, int yCoord) {
		this.id = id;
		buildingPanel = jp;
		System.out.println("Danny Rest Building Panel "
				+ buildingPanel.hashCode());
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
		if (person.getMainRole() instanceof DannyHost) {
			DRestMoney = hostRole.getMoney();
		}
		person.resetActiveRoles();
		person.msgExitBuilding();
		if (person.getMainRole() instanceof DannyCustomer) {
			numCustomers--;
		}
	}

	public void EnterBuilding(Person person, String job) {
		Role newRole = null;
		try {
			if (forceClose) {
				person.msgGoHome();
				return;
			}
			if (job.equals("usto")) {
				person.msgGoToBuilding(
						God.Get().findBuildingOfType(BuildingType.Market),
						Intent.work);
				return;
			}

			if (job.equals("restaurant.DannyCustomer")) {
				// newRole = new DannyCustomer();
				newRole = (DannyCustomer) person.getMainRole();
				((DannyCustomer) newRole).setHost(hostRole);
				numCustomers++;
			} else if (job.equals("restaurant.DannyHost")) {
				// newRole = hostRole;
				newRole = (DannyHost) person.getMainRole();
				hostRole = (DannyHost) newRole;
				hostRole.setMoney(DRestMoney);
				hostRole.setAcc(B_DannyAccNum);
				hostFilled = true;
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			} else if (job.equals("restaurant.DannyWaiter")) {
				numWaiters++;
				// newRole = new DannyWaiter();
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
				System.out.println("WAITER : COOK = " + cookRole.hashCode());
			} else if (job.equals("restaurant.DannyPCWaiter")) {
				numWaiters++;
				// newRole = new DannyWaiter();
				newRole = (DannyPCWaiter) person.getMainRole();
				hostRole.addWaiter((DannyPCWaiter) newRole);
				((DannyPCWaiter) newRole).setNum(numWaiters);
				((DannyPCWaiter) newRole).setHost(hostRole);
				((DannyPCWaiter) newRole).setCook(cookRole);
				((DannyPCWaiter) newRole).setCashier(cashierRole);
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
				System.out.println("PCWAITER : COOK = " + cookRole.hashCode());
			} else if (job.equals("restaurant.DannyCook")) {
				// newRole = cookRole;
				newRole = (DannyCook) person.getMainRole();
				cookRole = (DannyCook) newRole;
				cookFilled = true;
				setOpen(areAllNeededRolesFilled());
				orderStand = new OrderStand(this, cookRole);
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
				System.out.println("COOK = " + cookRole.hashCode());
			} else if (job.equals("restaurant.DannyCashier")) {
				// newRole = cashierRole;
				newRole = (DannyCashier) person.getMainRole();
				cashierRole = (DannyCashier) person.getMainRole();
				cashierFilled = true;
				cashierRole.setMoney(hostRole.getMoney());
				setOpen(areAllNeededRolesFilled());
				System.out
						.println("All roles needed Danny Restaurant : "
								+ (hostFilled && cookFilled && cashierFilled && numWaiters > 0));
			} else if (job.equals("market.MarketDeliveryPersonRole")) {
				MarketDeliveryPersonRole restaurantRole = (MarketDeliveryPersonRole) person.mainRole;
				restaurantRole.msgGuiArrivedAtDestination();
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
			if (newRole instanceof DannyPCWaiter)
				((DannyPCWaiter) newRole).setName(newRole.myPerson.name);
			if (newRole instanceof DannyCashier)
				((DannyCashier) newRole).setName(newRole.myPerson.name);
			if (newRole instanceof DannyCook) {
				((DannyCook) newRole).setName(newRole.myPerson.name);
				System.out.println("COOK PERSON = "
						+ cookRole.myPerson.hashCode());
			}
			if (newRole instanceof DannyCustomer)
				((DannyCustomer) newRole).setName(newRole.myPerson.name);

		} catch (Exception e) {
			// e.printStackTrace();
			int goTo = person.getBuilding().getID() + 1;
			if (goTo > God.Get().buildings.size() - 1) {
				goTo = 0;
			}
			person.msgGoToBuilding(
					God.Get().findBuildingOfType(BuildingType.Market),
					Intent.work);
			ExitBuilding(person);

			// System.out.println("Building: no class found");
		}
	}

	@Override
	public Role getManagerRole() {
		return hostRole;
	}

	public OrderStand getOrderStand() {
		return orderStand;
	}

}