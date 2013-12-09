package restaurant;

import restaurant.gui.CustomerGui;
import restaurant.gui.DannyRestaurantAnimationPanel;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import SimCity.Base.Role;
import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_DannyRestaurant;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class DannyCustomer extends Role implements Customer {
	private String name;
	private String myChoice;
	private double cash = 10;
	private double duesOwed;
	private double debt = 0;
	private int hungerLevel = 12; // determines length of meal
	private final int decisionTime = 2000;
	Timer timer = new Timer();

	private CustomerGui customerGui;

	private Semaphore goingToCashier = new Semaphore(0, true);

	// agent correspondents
	private DannyHost host;
	private Waiter waiter;
	private DannyCashier cashier;

	private Menu menu;

	private int tableNumber;

	// private boolean isHungry = false; //hack for gui
	public enum AgentState {
		DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, WaitingForOrder, Eating, DoneEating, Paying, DonePaying, Leaving
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {
		none, gotHungry, followWaiter, seated, decided, foodDelivered, doneEating, billDelivered, doneLeaving
	};

	AgentEvent event = AgentEvent.none;

	public enum CashierEvent {
		none, billDelivered, changeReceived
	};

	CashierEvent cashierEvent = CashierEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 * 
	 * @param name
	 *            name of the customer
	 * @param gui
	 *            reference to the customergui so the customer can send it
	 *            messages
	 */
	public DannyCustomer() {
		super();
		// the customer will start with a random amount
		// of money between $9 and $20 ,(max - min + 1)
		cash = 9 + (int) (Math.random() * (12));
	}

	public void setName(String name) {
		this.name = name;
		if (name.equals("Flake")) {
			cash = 0;
		}
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(DannyHost host) {
		this.host = host;
	}

	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	public String getCustomerName() {
		return name;
	}

	// Messages

	public void gotHungry() {// from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		// if a customer is in debt
		// he will have enough money to pay
		// his debt when he next comes in
		cash += debt;
		if (debt > 0) {
			cash += 10;
		}
		print("Initial Cash : " + cash);
		stateChanged();
	}

	public void msgFollowMe(Waiter waiter, Menu menu, int tableNumber) {
		print("MESSAGE 3 : Waiter -> Customer : FollowMe");
		this.waiter = waiter;
		this.menu = menu;
		this.tableNumber = tableNumber;
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	public void msgWhatDoYouWant() {
		print("MESSAGE 5 : Waiter -> Customer : WhatDoYouWant");
		state = AgentState.Ordering;
		makeDecision();
		stateChanged();
	}

	public void msgOutOfChoice(Menu menu) {
		print("MESSAGE 9 Non-Normative : Waiter -> Customer : OutOfChoice");
		this.menu = menu;
		state = AgentState.Ordering;
		makeDecision();
		stateChanged();
	}

	public void msgHereIsYourFood(String choice) {
		if (myChoice == choice) {
			print("MESSAGE 9 : Waiter -> Customer : HereIsYourFood");
			state = AgentState.Eating;
			event = AgentEvent.foodDelivered;
			stateChanged();
		}
	}

	public void msgHereIsYourBill(double dues) {
		print("MESSAGE 12 : Waiter -> Customer : HereIsYourBill");
		cashierEvent = CashierEvent.billDelivered;
		customerGui.DoPrintBillDelivered();
		duesOwed = dues;
		stateChanged();
	}

	public void msgHereIsYourChange(double change) {
		print("MESSAGE 14 : Cashier -> Customer : HereIsYourChange");
		cashierEvent = CashierEvent.changeReceived;
		cash = change;
		print("Final cash : " + cash);
		stateChanged();
	}

	public void msgInDebt(double debt) {
		print("In Debt : " + debt);
		this.debt = debt;
	}

	public void msgNotInDebt() {
		debt = 0;
	}

	/*
	 * Message from Animation : the Customer has Arrived at the Seat
	 */
	public void msgAnimationFinishedGoToSeat() {
		// from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	/*
	 * Message from Animation : the Customer has Arrived at the Cashier
	 */
	public void msgAnimationFinishedGoToCashier() {
		goingToCashier.release();
	}

	/*
	 * Message from Animation : the Customer has Left the Restaurant
	 */
	public void msgAnimationFinishedLeaveRestaurant() {
		// from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		// CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant
				&& event == AgentEvent.followWaiter) {
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			state = AgentState.Seated;
			CallWaiter();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.decided) {
			state = AgentState.WaitingForOrder;
			GiveOrder();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.foodDelivered) {
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			state = AgentState.DoneEating;
			stateChanged();
			return true;
		}
		if (state == AgentState.DoneEating
				&& cashierEvent == CashierEvent.billDelivered) {
			payCashier();
			return true;
		}
		if (state == AgentState.Paying
				&& cashierEvent == CashierEvent.changeReceived) {
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			// no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");

		host.msgIWantFood(this);// send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoPrintGoingToSeat();
		customerGui.DoGoToSeat(1, tableNumber);// hack; only one table
	}

	private void CallWaiter() {
		// first check menu prices
		boolean hasEnoughMoney = false;
		for (int i = 0; i < menu.size(); i++) {
			if (cash > menu.prices.get(i)) {
				hasEnoughMoney = true;
			}
		}
		if (!hasEnoughMoney && !name.equals("Flake")) {
			// msgDoneAndPaying, but actually just done
			// not paying, since did not order
			print("Not enough money to buy anything.");
			waiter.msgDoneAndPaying(this);
			leaveRestaurant();
		} else {
			timer.schedule(new TimerTask() {
				public void run() {
					waiter.msgReadyToOrder(DannyCustomer.this);
				}
			}, decisionTime);
		}
	}

	private void GiveOrder() {
		customerGui.DoPrintWaitingForOrder(myChoice);
		print(myChoice);
		waiter.msgHereIsMyOrder(myChoice, this);
	}

	private void EatFood() {
		event = AgentEvent.none;
		Do("Eating Food");
		customerGui.DoPrintEatingFood(myChoice);
		// This next complicated line creates and starts a timer thread.
		// We schedule a deadline of getHungerLevel()*1000 milliseconds.
		// When that time elapses, it will call back to the run routine
		// located in the anonymous class created right there inline:
		// TimerTask is an interface that we implement right there inline.
		// Since Java does not all us to pass functions, only objects.
		// So, we use Java syntactic mechanism to create an
		// anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating");
				event = AgentEvent.doneEating;
				// isHungry = false;
				stateChanged();
			}
		}, getHungerLevel() * 1000); // how long to wait before running task
	}

	private void leaveRestaurant() {
		Do("Leaving.");
		state = AgentState.Leaving;
		cashierEvent = CashierEvent.none;
		if (customerGui != null) {
			customerGui.DoExitRestaurant();
		} else {
			myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
			exitRestaurant();
		}
	}

	private void makeDecision() {
		for (String type : menu.foods) {
			if (getName().equals(type)) {
				myChoice = type;
				event = AgentEvent.decided;
				return;
			}
		}

		boolean cantAffordAnything = true;

		// if customer's name does not match a food type
		// then choose a food at random
		int decision = (int) (Math.random() * menu.size());
		if (name.equals("Flake")) {
			cantAffordAnything = false;
		} else {
			for (int i = 0; i < menu.size(); i++) {
				if (cash < menu.prices.get(decision)) {
					decision = (int) (Math.random() * menu.size());
				} else {
					cantAffordAnything = false;
					break;
				}
			}
		}
		if (cantAffordAnything) {
			print("Can't afford anything else.");
			waiter.msgCantAffordAnythingElse(this);
			leaveRestaurant();
		} else {
			myChoice = menu.at(decision);
			event = AgentEvent.decided;
		}
	}

	private void payCashier() {
		Do("Going To Cashier");
		customerGui.DoGoToCashier();
		customerGui.DoPrintGoingToCashier();
		try {
			goingToCashier.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		state = AgentState.Paying;
		waiter.msgDoneAndPaying(this);
		cashier.msgPayment(this, cash);
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		// could be a state change. Maybe you don't
		// need to eat until hunger lever is > 5?
	}

	@Override
	public String toString() {
		return "Danny Customer";
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	public void setDebt(double debt) {
		this.debt = debt;
	}

	private void print(String string) {
		System.out.println(string);
	}

	public void exitRestaurant() {
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson
				.getBuilding().getPanel();
		customerGui.setPresent(false);
		ap.removeGui(customerGui);
		exitBuilding(myPerson);
	}

	@Override
	protected void enterBuilding() {
		System.out.println("DannyCustomer entered building");

		B_DannyRestaurant rest = (B_DannyRestaurant) (myPerson.getBuilding());
		host = rest.hostRole;
		cashier = rest.cashierRole;

		CustomerGui cg = new CustomerGui(this);
		customerGui = cg;

		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson
				.getBuilding().getPanel();
		ap.addGui(customerGui);

		if (!myPerson.building.getOpen()) {
			leaveRestaurant();
			return;
		}
		customerGui.setHungry();
	}

	@Override
	public void workOver() {
		//leaveRestaurant();
	}

	@Override
	public void msgIsHungry() {
		customerGui.setHungry();
	}
}
