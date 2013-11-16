package housing.roles;

import housing.gui.TenantGui;
import SimCity.Base.Person;
import SimCity.Globals.Money;

/**
 * @author Daniel
 *
 */
public class TenantRole {
	
	/*
	 * Data
	 */
	
	private Person person;
	
	private Money rentOwed;
	private OwnerRole owner;
	
	enum State {None, Hungry, Tired};
	State state;
	
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
		// TODO - tenant is now homeless
	}
	
	/*
	 * Scheduler
	 */
	
	protected boolean pickAndExecuteAnAction() {
		
		if (!rentOwed.isZero()) {
			tryToPayRent();
			return true;
		}
		
		if (state == State.Hungry) {
			getFood();
			return true;
		}
		
		if (state == State.Tired) {
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
		if (money.isGreaterThan(rentOwed)) {
			money.subtract(rentOwed);
			owner.msgHereIsRent(this, rentOwed);
		}
		else {
			owner.msgCannotPayRent(this);
			}
		}
	}
	
	private void getFood() {
		// If the customer is hungry, but willing to wait a bit, and has enough cash
		if (state == State.Hungry && rentOwed.isZero() &&
				hungerLevel < hungerThreshhold && cash > cashThreshhold) {
			// Leave house to go to Restaurant
			gui.DoLeaveHouse();
		}
		else {
			gui.DoCookFood();
			// use appliance . chance to break appliance
		}
	}
	
	private void sleep() {
		gui.DoGoToBed();
		// sheets need to be cleaned every x # of uses
	}
}
