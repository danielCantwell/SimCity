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
import SimCity.Base.Person.Intent;
import SimCity.Base.Role;
import SimCity.Buildings.B_House;
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
		System.out.println("Tenant owes rent");
		rentOwed = m;
		state = State.owesRent;
		stateChanged();
	}

	public void msgEvictionNotice() {
		System.out.println("Tenant is evicted");
		myPerson.setHomeType("None");
		stateChanged();
	}

	// MESSAGES from God

	public void msgMorning() {
		System.out.println("Tenant should be waking up");
		time = Time.getReady;
		stateChanged();
	}

	public void msgGoToWork() {
		System.out.println("Tenant should be going to work");
		time = Time.work;
		stateChanged();
	}

	public void msgSleeping() {
		System.out.println("Tenant should be going to sleep");
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
		// go to the mailbox
		gui.DoGoToMailbox();
		try {
			atMail.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (myPerson.getMoney().isGreaterThan(rentOwed)) {
			// if the tenant has enough money, pay rent
			myPerson.getMoney().subtract(rentOwed);
			owner.msgHereIsRent(this, rentOwed);
		} else {
			// if the tenant does not have enough money
			// tell the owner and change state to needMoney
			owner.msgCannotPayRent(this);
			state = State.needMoney;
			stateChanged();
		}
	}

	private void goToBank() {
		System.out.println("Tenant is going to the bank");
		DoGoToBank();
	}

	private void goToMarket() {
		System.out.println("Tenant is going to the market");
		DoGoToMarket();
	}

	private void getReady() {
		System.out.println("Tenant has woken up and is getting ready");
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
		System.out.println("Tenant is getting food");
		if (tenantShouldEatOut()) {
			DoGoToRestaurant();
		} else if (foodCount > 0) {
			DoCookFood();
		} else {
			goToMarket();
		}
	}

	private void sleep() {
		System.out.println("Tenant is going to bed");
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
		System.out.println("Tenant is going to work");
		DoLeaveHouse();
		myPerson.msgGoToBuilding(myPerson.getWorkPlace(), Intent.work);
		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();
		myPanel.addGui(gui);
		exitBuilding(myPerson);

		// System.out.println(myPerson.actions.get(0).getGoAction().toString());
	}

	public void useAppliance(String type) {
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

	// -----------------------------Other Functions-----------------------------

	public boolean tenantShouldEatOut() {
		return (rentOwed.isZero()
				&& myPerson.getMoney().isGreaterThan(
						myPerson.getMoneyThreshold()) && God.Get().hour < 21);
	}

	public void DoGoToRestaurant() {
		// Leave house to go to Restaurant
		System.out.println("Tenant is going to a restaurant");
		DoLeaveHouse();
		// God selects a random restaurant for Tenant to go to
		myPerson.msgGoToBuilding(God.Get().findRandomRestaurant(),
				Intent.customer);

		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();

		myPanel.addGui(gui);
		exitBuilding(myPerson);
	}

	public void DoCookFood() {
		System.out.println("Tenant is going to cook food");
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
		System.out.println("Tenant is entering building");

		owner = ((B_House) myPerson.building).getOwner();
		owner.msgAddTenant(this);

		time = Time.awake;
		HousingAnimation myPanel = (HousingAnimation) myPerson.myHouse
				.getPanel();
		if (myPanel.getGuis().contains(gui)) {
			gui.setPresent(true);
		} else {
			myPanel.addGui(gui);
		}
		gui.DoGoToTable(tenantNumber);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stateChanged();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Tenant Role";
	}
}
