package housing.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import housing.gui.HousingAnimation;
import housing.gui.TenantGui;
import housing.interfaces.Owner;
import housing.interfaces.Tenant;
import housing.roles.OwnerRole.Appliance;
import housing.roles.OwnerRole.ApplianceState;
import SimCity.Base.God;
import SimCity.Base.Person.Intent;
import SimCity.Base.Role;
import SimCity.Globals.Money;

/**
 * @author Daniel
 * 
 */
public class TenantRole extends Role implements Tenant {

	public TenantRole() {

	}

	// -------------------------------------DATA-------------------------------------

	public Money rentOwed = new Money(0, 0);
	public Owner owner;

	TenantGui gui = new TenantGui(this);

	public enum Time {
		msgSleep, sleeping, awake, work
	};

	public Time time = Time.awake;

	public List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>());

	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atBed = new Semaphore(0, true);
	private Semaphore atFridge = new Semaphore(0, true);
	private Semaphore atStove = new Semaphore(0, true);
	private Semaphore atMail = new Semaphore(0, true);
	private Semaphore atDoor = new Semaphore(0, true);

	// -----------------------------------MESSAGES-----------------------------------

	public void msgHereAreAppliances(List<Appliance> a) {
		appliances = a;
	}

	public void msgPayRent(Money m) {
		System.out.println("Tenant owes rent");
		rentOwed = m;
		stateChanged();
	}

	public void msgEvictionNotice() {
		System.out.println("Tenant is evicted");
		myPerson.setHomeType("None");
		stateChanged();
	}

	public void msgMorning() {
		System.out.println("Tenant is waking up");
		time = Time.awake;
		stateChanged();
	}

	public void msgGoToWork() {
		time = Time.work;
		stateChanged();
	}

	public void msgSleeping() {
		System.out.println("Tenant is going to sleep");
		time = Time.msgSleep;
		stateChanged();
	}

	// MESSAGES from GUI

	public void msgAtTable() {
		System.out.println("Tenant is at table");
		atTable.release();
	}

	public void msgAtBed() {
		System.out.println("Tenant is at bed");
		atBed.release();
	}

	public void msgAtFridge() {
		System.out.println("Tenant is at fridge");
		atFridge.release();
	}

	public void msgAtStove() {
		System.out.println("Tenant is at stove");
		atStove.release();
	}

	public void msgAtMail() {
		System.out.println("Tenant is at mail");
		atStove.release();
	}

	public void msgAtDoor() {
		System.out.println("Tenant is at door");
		atDoor.release();
	}

	// -----------------------------------SCHEDULER-----------------------------------

	public boolean pickAndExecuteAnAction() {

		if (time == Time.sleeping) {
			return false;
		}

		if (!rentOwed.isZero()) {
			tryToPayRent();
			return true;
		}

		if (myPerson.getHungerLevel() <= myPerson.getHungerThreshold()) {
			getFood();
			return true;
		}

		if (time == Time.msgSleep) {
			sleep();
			return true;
		}

		if (time == Time.work) {
			goToWork();
			return true;
		}

		synchronized (appliances) {
			for (Appliance a : appliances) {
				if (a.state == ApplianceState.NeedsFixing) {
					a.fixAppliance();
					return true;
				}
			}
		}

		return false;
	}

	// ------------------------------------ACTIONS------------------------------------

	private void tryToPayRent() {
		System.out.println("Tenant is trying to pay rent");
		gui.DoGoToMailbox();
		try {
			atMail.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (myPerson.getMoney().isGreaterThan(rentOwed)) {
			myPerson.getMoney().subtract(rentOwed);
			owner.msgHereIsRent(this, rentOwed);
		} else {
			owner.msgCannotPayRent(this);
		}
	}

	private void getFood() {
		System.out.println("Tenant is getting food");
		// If the customer is hungry, but willing to wait a bit, and has enough
		// cash
		if (rentOwed.isZero()
				&& myPerson.getMoney().isGreaterThan(
						myPerson.getMoneyThreshold()) && God.Get().hour < 21) {
			// Leave house to go to Restaurant
			System.out.println("Tenant is going to a restaurant");
			gui.DoLeaveHouse();
			try {
				atDoor.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			myPerson.msgGoToBuilding(God.Get().findRandomRestaurant(),
					Intent.customer);
			
			HousingAnimation myPanel = (HousingAnimation)myPerson.myHouse.getPanel();

			myPanel.addGui(gui);
			exitBuilding(myPerson);
			setActive(false);
		} else {
			System.out.println("Tenant is going to cook food");
			gui.DoGoToFridge();
			try {
				atFridge.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			useAppliance("Fridge");
			gui.DoGoToStove();
			try {
				atStove.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			useAppliance("Stove");
			gui.DoGoToTable();
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			useAppliance("Table");
			myPerson.setHungerLevel(10);
		}
	}

	private void sleep() {
		System.out.println("Tenant is going to bed");
		gui.DoGoToBed();
		try {
			atBed.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		useAppliance("Bed");
		time = Time.sleeping;
	}

	private void goToWork() {
		System.out.println("Tenant is going to work");
		gui.DoLeaveHouse();
		try {
			atDoor.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myPerson.msgGoToBuilding(myPerson.getWorkPlace(), Intent.work);
		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();
		myPanel.addGui(gui);
		exitBuilding(myPerson);

		System.out.println(myPerson.actions.get(0).getGoAction().toString());
		setActive(false);
	}

	private void useAppliance(String type) {
		for (Appliance a : appliances) {
			if (a.type == type) {
				System.out.println("Tenant is using appliance: " + type);
				a.useAppliance();
				if (a.durability <= 0) {
					owner.msgApplianceBroken(this, a);
					a.state = ApplianceState.NeedsFixing;
				}
			}
		}
	}

	public TenantGui getGui() {
		return gui;
	}

	public void setGui(TenantGui gui) {
		this.gui = gui;
	}

	@Override
	public void workOver() {
		// Do Nothing
	}

	@Override
	protected void enterBuilding() {
		System.out.println("Tenant is entering building");
		time = Time.awake;
		HousingAnimation myPanel = (HousingAnimation)myPerson.myHouse.getPanel();
		if (myPanel.getGuis().contains(gui)){
			gui.setPresent(true);
		}
		else{
			myPanel.addGui(gui);
		}
		gui.DoGoToTable();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stateChanged();
	}
}
