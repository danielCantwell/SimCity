package housing.roles;

import housing.gui.TenantGui;
import housing.interfaces.Owner;
import housing.interfaces.Tenant;
import housing.roles.OwnerRole.Appliance;
import SimCity.Base.Person;
import SimCity.Base.Person.PersonState;
import SimCity.Globals.Money;

/**
 * @author Daniel
 *
 */
public class TenantRole implements Tenant{
	
	/*
	 * Data
	 */
	
	private Person person;
	
	private Money rentOwed;
	private Owner owner;
	
	TenantGui gui;
	
	/*
	public List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>());
	*/
	
	/*
	 * Messages
	 */
	
	public void msgPayRent(Money m) {
		rentOwed = m;
	}
	
	public void msgEvictionNotice() {
		person.setHomeType("None");
	}
	
	/*
	 * Scheduler
	 */
	
	protected boolean pickAndExecuteAnAction() {
		
		if (!rentOwed.isZero()) {
			tryToPayRent();
			return true;
		}
		
		if (person.getHungerLevel() >= person.getHungerThreshold()) {
			getFood();
			return true;
		}
		
		if (person.getPersonState() == PersonState.goingToSleep) {
			sleep();
			return true;
		}
		
		return false;
	}
	
	/*
	 * Actions
	 */
	
	private void tryToPayRent() {
		gui.DoGoToMailbox();
		if (person.getMoney().isGreaterThan(rentOwed)) {
			person.getMoney().subtract(rentOwed);
			owner.msgHereIsRent(this, rentOwed);
		}
		else {
			owner.msgCannotPayRent(this);
		}
	}
	
	private void getFood() {
		// If the customer is hungry, but willing to wait a bit, and has enough cash
		if (rentOwed.isZero() && person.getMoney() >= person.getMoneyThreshold()) {
			// Leave house to go to Restaurant
			gui.DoLeaveHouse();
			// TODO decide which restaurant to go to and how to get there
			person.msgGoToBuilding(restaurant);
		}
		else {
			gui.DoGoToFridge();
			useAppliance("Fridge");
			gui.DoGoToStove();
			useAppliance("Stove");
			gui.DoGoToTable();
			useAppliance("Table");
		}
	}
	
	private void sleep() {
		gui.DoGoToBed();
		useAppliance("Bed");
	}
	
	private void useAppliance(String type) {
		for (Appliance a : owner.appliances) { // TODO - hack to get appliances from owner
			if (a.type == type) {
				if (a.useAppliance() <= 0) {
					owner.msgApplianceBroken(this, a);
				}
			}
		}
	}
}
