package housing.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class TenantRole extends Role implements Tenant{
	
	public TenantRole() {
		
	}
	
	//-------------------------------------DATA-------------------------------------
	
	private Money rentOwed = new Money(0, 0);
	private Owner owner;
	
	TenantGui gui = new TenantGui(this);
	enum Time { msgSleep, sleeping, awake};
	Time sleepTime = Time.awake;
	
	public List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>());
	
	
	//-----------------------------------MESSAGES-----------------------------------
	
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
		sleepTime = Time.awake;
		stateChanged();
	}
	
	public void msgSleeping(){
		System.out.println("Tenant is going to sleep");
		sleepTime = Time.msgSleep;
		stateChanged();
	}
	
	//-----------------------------------SCHEDULER-----------------------------------
	
	public boolean pickAndExecuteAnAction() {
		
		if (sleepTime == Time.sleeping){
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
		
		if (sleepTime == Time.msgSleep) {
			sleep();
			return true;
		}
		
		synchronized(appliances) {
			for (Appliance a : appliances) {
				if (a.state == ApplianceState.NeedsFixing) {
					a.fixAppliance();
					return true;
				}
			}
		}
		
		return false;
	}
	
	//------------------------------------ACTIONS------------------------------------
	
	private void tryToPayRent() {
		System.out.println("Tenant is trying to pay rent");
		gui.DoGoToMailbox();
		if (myPerson.getMoney().isGreaterThan(rentOwed)) {
			myPerson.getMoney().subtract(rentOwed);
			owner.msgHereIsRent(this, rentOwed);
		}
		else {
			owner.msgCannotPayRent(this);
		}
	}
	
	private void getFood() {
		System.out.println("Tenant is getting food");
		// If the customer is hungry, but willing to wait a bit, and has enough cash
		if (rentOwed.isZero() && myPerson.getMoney().isGreaterThan(myPerson.getMoneyThreshold())) {
			// Leave house to go to Restaurant
			System.out.println("Tenant is going to a restaurant");
			gui.DoLeaveHouse();
			myPerson.msgGoToBuilding(God.Get().findRandomRestaurant(), Intent.customer);
			exitBuilding(myPerson);
		}
		else {
			System.out.println("Tenant is going to cook food");
			gui.DoGoToFridge();
			useAppliance("Fridge");
			gui.DoGoToStove();
			useAppliance("Stove");
			gui.DoGoToTable();
			useAppliance("Table");
		}
	}
	
	private void sleep() {
		System.out.println("Tenant is going to bed");
		gui.DoGoToBed();
		useAppliance("Bed");
		sleepTime = Time.sleeping;
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

	@Override
	public void workOver() {
		// Do Nothing
	}

	@Override
	protected void enterBuilding() {
		System.out.println("Tenant is entering building");
		gui.DoGoToTable();
		stateChanged();
	}
}
