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
import SimCity.Base.God.BuildingType;
import SimCity.Base.Person.GroceryList;
import SimCity.Base.Person.Intent;
import SimCity.Base.Role;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;

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
	public int tenantNumber;
	public int foodCount = 4;

	TenantGui gui = new TenantGui(this);

	public enum Time {
		msgSleep, sleeping, getReady, awake, work
	};

	public enum State {
		none, needFood, needMoney, owesRent
	};

	public Time time = Time.awake;
	public State state = State.none;

	public List<Appliance> appliances = Collections
			.synchronizedList(new ArrayList<Appliance>());

	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atBed = new Semaphore(0, true);
	private Semaphore atFridge = new Semaphore(0, true);
	private Semaphore atStove = new Semaphore(0, true);
	private Semaphore atMail = new Semaphore(0, true);
	private Semaphore atDoor = new Semaphore(0, true);

	// -----------------------------------MESSAGES-----------------------------------

	public void msgHouseInfo(List<Appliance> a, int tenantNumber) {
		appliances = a;
		this.tenantNumber = tenantNumber;
	}

	public void msgPayRent(Money m) {
		Do(AlertTag.House, myPerson.getName() + " Tenant owes rent");
		rentOwed = m;
		state = State.owesRent;
		stateChanged();
	}

	public void msgEvictionNotice() {
		Do(AlertTag.House,"Tenant is evicted");
		myPerson.setHomeType("None");
		stateChanged();
	}

	// MESSAGES from God

	public void msgMorning() {
		Do(AlertTag.House,myPerson.getName() + " Tenant should be waking up");
		time = Time.getReady;
		stateChanged();
	}

	public void msgGoToWork() {
		Do(AlertTag.House,myPerson.getName()
				+ " Tenant should be going to work");
		time = Time.work;
		stateChanged();
	}

	public void msgSleeping() {
		Do(AlertTag.House,myPerson.getName()
				+ " Tenant should be going to sleep");
		time = Time.msgSleep;
		stateChanged();
	}

	// MESSAGES from GUI

	public void msgAtTable() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is at table");
		atTable.release();
	}

	public void msgAtBed() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is at bed");
		atBed.release();
	}

	public void msgAtFridge() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is at fridge");
		atFridge.release();
	}

	public void msgAtStove() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is at stove");
		atStove.release();
	}

	public void msgAtMail() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is at mailbox");
		atStove.release();
	}

	public void msgAtDoor() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is at door");
		atDoor.release();
	}

	// -----------------------------------SCHEDULER-----------------------------------

	public boolean pickAndExecuteAnAction() {

		if (time == Time.sleeping) {
			return false;
		}
		/*
		 * synchronized (appliances) { for (Appliance a : appliances) { if
		 * (a.state == ApplianceState.NeedsFixing) { a.fixAppliance(); return
		 * true; } } }
		 */
		if (state == State.owesRent) {
			tryToPayRent();
			return true;
		}

		if (state == State.needMoney && !God.Get().banksClosed
				&& (God.Get().getHour() < 21)) {
			goToBank();
			return true;
		}

		if (state == State.needFood && (God.Get().getHour() < 21)) {
			goToMarket();
			return true;
		}

		if (myPerson.getHungerLevel() <= myPerson.getHungerThreshold()) {
			getFood();
			return true;
		}

		if (time == Time.getReady) {
			getReady();
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

		return false;
	}

	// ------------------------------------ACTIONS------------------------------------

	private void tryToPayRent() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is trying to pay rent");
		// go to the mailbox
		gui.DoGoToMailbox();
		try {
			atMail.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (myPerson.getMoney().isGreaterThan(rentOwed)) {
			// if the tenant has enough money, pay rent
			owner.msgHereIsRent(this, rentOwed);
			myPerson.getMoney().subtract(rentOwed);
		} else {
			// if the tenant does not have enough money
			// tell the owner and change state to needMoney
			owner.msgCannotPayRent(this);
			state = State.needMoney;
			stateChanged();
		}
	}

	private void goToBank() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is going to the bank");
		DoGoToBank();
	}

	private void goToMarket() {
		Do(AlertTag.House,myPerson.getName()
				+ " Tenant is going to the market");
		DoGoToMarket();
	}

	private void getReady() {
		Do(AlertTag.House,myPerson.getName()
				+ " Tenant has woken up and is getting ready");
		if (myPerson.getMainRoleString().contains("usto")) {
			// if the person is a dedicated customer
			DoCookFood();
		} else {
			gui.DoGoToTable(tenantNumber);
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		time = Time.awake;
	}

	private void getFood() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is getting food");
		if (tenantShouldEatOut()) {
			DoGoToRestaurant();
		} else if (foodCount > 0) {
			DoCookFood();
		} else {
			goToMarket();
		}
	}

	private void sleep() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is going to bed");
		gui.DoGoToBed(tenantNumber);
		try {
			atBed.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		useAppliance("Bed");
		time = Time.sleeping;
	}

	private void goToWork() {
		Do(AlertTag.House,myPerson.getName() + " Tenant is going to work");
		DoLeaveHouse();
		myPerson.msgGoToBuilding(myPerson.getWorkPlace(), Intent.work);
		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();
		myPanel.addGui(gui);
		exitBuilding(myPerson);

		// Do(AlertTag.House,myPerson.actions.get(0).getGoAction().toString());
	}

	public void useAppliance(String type) {
		for (Appliance a : appliances) {
			if (a.type == type) {
				Do(AlertTag.House,myPerson.getName()
						+ " Tenant is using appliance: " + type);
				a.useAppliance();
				if (a.durability <= 0) {
					owner.msgApplianceBroken(this, a);
					a.durability = 20;
				}
			}
		}
	}

	// -----------------------------Other Functions-----------------------------

	public boolean tenantShouldEatOut() {
		return (rentOwed.isZero()
				&& myPerson.getMoney().isGreaterThan(
						myPerson.getMoneyThreshold()) && God.Get().hour > 7 && God.Get().hour < 21);
	}

	public void DoGoToRestaurant() {
		// Leave house to go to Restaurant
		Do(AlertTag.House,myPerson.getName()
				+ " Tenant is going to a restaurant to eat");
		DoLeaveHouse();
		// God selects a random restaurant for Tenant to go to
		int r = (int)(Math.round((Math.random() * 4)));
		String craving = "";
		switch (r){
		case 0: craving = "Brian";break; //Brian
    	case 1: craving = "Jesse";break; // Jesse
    	case 2: craving = "Danny";break; //Danny
    	case 3:craving = "Tim";break; //Tim
    	case 4: craving = "Eric";break; //Eric
    	default : craving = "Brian";break;
		}
		myPerson.msgGoToBuilding(God.Get().findRandomRestaurant(craving),
				Intent.customer);

		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();

		myPanel.addGui(gui);
		exitBuilding(myPerson);
	}

	public void DoCookFood() {
		System.out
				.println(myPerson.getName() + " Tenant is going to cook food");
		// Get food from fridge
		gui.DoGoToFridge();
		try {
			atFridge.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		useAppliance("Fridge");
		// Cook food on stove
		gui.DoGoToStove();
		try {
			atStove.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		useAppliance("Stove");
		// Eat food at table
		gui.DoGoToTable(tenantNumber);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		useAppliance("Table");
		// Reset hunger level
		myPerson.hungerLevel += 10;
	}

	public void DoGoToBank() {
		DoLeaveHouse();
		myPerson.msgGoToBuilding(God.Get()
				.findBuildingOfType(BuildingType.Bank), Intent.customer);
		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();
		myPanel.addGui(gui);
		exitBuilding(myPerson);
	}

	public void DoGoToMarket() {
		foodCount = 4;
		DoLeaveHouse();
		myPerson.groceryList.add(myPerson.new GroceryList("Groceries", 8));
		myPerson.msgGoToBuilding(
				God.Get().findBuildingOfType(BuildingType.Market),
				Intent.customer);
		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();
		myPanel.addGui(gui);
		exitBuilding(myPerson);
	}

	public void DoLeaveHouse() {
		gui.DoLeaveHouse();
		try {
			atDoor.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
		Do(AlertTag.House,myPerson.getName() + " Tenant is entering building");

		owner = ((B_House) myPerson.building).getOwner();
		owner.msgAddTenant(this);

		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();
		if (myPanel.getGuis().contains(gui)) {
			gui.setPresent(true);
		} else {
			myPanel.addGui(gui);
		}
		if (God.Get().getHour() < 5)
			msgSleeping();
		else {
			time = Time.awake;
			gui.DoGoToTable(tenantNumber);
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stateChanged();
	}

	@Override
	public String toString() {
		return "HTen";
	}
}
