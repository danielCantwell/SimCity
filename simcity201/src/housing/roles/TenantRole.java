package housing.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import housing.gui.TenantGui;
import housing.interfaces.Owner;
import housing.interfaces.Tenant;
import housing.roles.OwnerRole.Appliance;
import housing.roles.OwnerRole.ApplianceState;
import SimCity.Base.Person.PersonState;
import SimCity.Base.Role;
import SimCity.Globals.Money;

/**
 * @author Daniel
 *
 */
public class TenantRole extends Role implements Tenant{
	
	//-------------------------------------DATA-------------------------------------
	
	private Money rentOwed;
	private Owner owner;
	
	TenantGui gui;
	
	
	public List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>());
	
	
	//-----------------------------------MESSAGES-----------------------------------
	
	public void msgHereAreAppliances(List<Appliance> a) {
		appliances = a;
	}
	
	public void msgPayRent(Money m) {
		rentOwed = m;
	}
	
	public void msgEvictionNotice() {
		myPerson.setHomeType("None");
	}
	
	//-----------------------------------SCHEDULER-----------------------------------
	
	protected boolean pickAndExecuteAnAction() {
		
		if (!rentOwed.isZero()) {
			tryToPayRent();
			return true;
		}
		
		if (myPerson.getHungerLevel() >= myPerson.getHungerThreshold()) {
			getFood();
			return true;
		}
		
		if (myPerson.getPersonState() == PersonState.goingToSleep) {
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
		// If the customer is hungry, but willing to wait a bit, and has enough cash
		if (rentOwed.isZero() && myPerson.getMoney().isGreaterThan(myPerson.getMoneyThreshold())) {
			// Leave house to go to Restaurant
			gui.DoLeaveHouse();
			// TODO decide which restaurant to go to and how to get there
			//person.msgGoToBuilding(restaurant);
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
		for (Appliance a : appliances) {
			if (a.type == type) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}
}
