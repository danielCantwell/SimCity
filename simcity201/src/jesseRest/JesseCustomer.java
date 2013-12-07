package jesseRest;

import SimCity.Base.Role;
import SimCity.Buildings.B_EricRestaurant;
import SimCity.Buildings.B_JesseRestaurant;
import jesseRest.Check;
import jesseRest.Menu;

import java.util.Timer;
import java.util.TimerTask;

import brianRest.gui.BrianAnimationPanel;
import jesseRest.gui.CustomerGui;
import jesseRest.interfaces.Customer;

/**
 * Restaurant customer agent.
 */

public class JesseCustomer extends Role implements Customer {
	private String name;
	private int hungerLevel = 5;
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private int currentTable;

	private JesseHost host;
	private JesseWaiter waiter;
	private JesseCashier cashier;
	public Menu menu;
	public int position;
	private String choice = "";
	private Check mycheck;
	
	public boolean ordersFoodWhenCantAfford = true;
	
	// Customers start with $15.
	public double money = 15;

	public enum AgentState {DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Deciding, Ordering, DoneOrdering, GivingOrder, Eating, DoneEating, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;
	public enum AgentEvent {none, gotHungry, followHost, seated, ordering, doneOrdering, givingOrder, eating, doneEating, paying, doneLeaving, gotImpatient};
	AgentEvent event = AgentEvent.none;

	public JesseCustomer(String name){
		super();
		this.name = name;
	}

	public void setHost(JesseHost host) {
		this.host = host;
	}
	
	public void setCashier(JesseCashier c) {
		cashier = c;
	}

	public void setCurrentTable(int table) {
		this.currentTable = table;
	}
	
	public String getCustomerName() {
		return name;
	}
	
	/**
	 * MESSAGES =====================================================
	 */

	public void msgLeavingBecauseImpatient() {
		if (state == AgentState.WaitingInRestaurant) {
			print("Customer is feeling impatient!");
			event = AgentEvent.gotImpatient;
			stateChanged();
		}
	}
	
	public void gotHungry() {
		print("Customer created.");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	private void print(String string) {
		System.out.println(string);
	}

	public void msgFollowMeToTable(Menu _menu, JesseWaiter _waiter) {
		event = AgentEvent.followHost;
		menu = _menu;
		waiter = _waiter;
		stateChanged();
	}
	
	public void msgPleaseReorder(Menu _menu) {
		menu = _menu;
		event = AgentEvent.seated;
		state = AgentState.BeingSeated;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		// Handles Waiter message about what do you want
		event = AgentEvent.doneOrdering;
		stateChanged();
	}
	
	public void msgHereIsYourFood(String choice) {
		event = AgentEvent.eating;
		stateChanged();
	}
	
	public void msgTakeCheck(Check c) {
		mycheck = c;
		stateChanged();
	}
	
	public void msgHereIsCustomerCopyCheck(Check c) {
		print("Customer recieved change from Cashier.");
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToCashier() {
		//from animation
		event = AgentEvent.paying;
		stateChanged();
	}

	/**
	 * SCHEDULER ====================================================
	 */
	
	// Implementation of FSM with proper states and events
	public boolean pickAndExecuteAnAction() {
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.gotImpatient) {
			state = AgentState.Leaving;
			ExitRestaurantLine();
			return true;
		}
		
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Deciding;
			DecideOrder();
			return true;
		}
		if (state == AgentState.Deciding && event == AgentEvent.ordering) {
			state = AgentState.Ordering;
			CallWaiter();
			return true;
		}
		if (state == AgentState.Ordering && event == AgentEvent.doneOrdering) {
			state = AgentState.DoneOrdering;
			GiveOrder();
			return true;
		}
		if (state == AgentState.DoneOrdering && event == AgentEvent.givingOrder) {
			state = AgentState.GivingOrder;
			OrderGiven();
			return true;
		}
		if (state == AgentState.GivingOrder && event == AgentEvent.eating) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating && mycheck != null){
			state = AgentState.Paying;
			LeaveTableToPay();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.paying){
			state = AgentState.Leaving;
			PayCashier();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			return true;
		}
		return false;
	}

	/**
	 * ACTIONS  ====================================================
	 */

	private void ExitRestaurantLine() {
		customerGui.DoExitRestaurant();
		print("Customer has left.");
	}
	private void goToRestaurant() {
		// Handles hungry message and tells host
		Do("Message 1: Sending IWantToEat from Customer to Host.");
		host.msgIWantToEat(this);
	}

	private void SitDown() {
		// Handles Waiter follow-me message and sits at correct table
		customerGui.DoGoToSeat(currentTable); 
	}
	
	private void DecideOrder() {
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.ordering;
				stateChanged();
			}
		},
		3000);
	}
	
	private void CallWaiter() {
		// Messages the waiter when ready to order
		print("Message 4: Sending ImReadyToOrder from Customer to Waiter");
		
		if (menu.foodChoices.size() == 0) {
			print("There is absolutely no food in the restaurant.");
			print("Customer is leaving restaurant.");
			waiter.msgDoneEatingAndPaying(this);
			customerGui.DoExitRestaurant();
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			return;
		}
		// Mechanism to choose what to order (randomly generated)
		if (ordersFoodWhenCantAfford) {
			choice = menu.foodChoices.get((int)Math.floor(Math.random() * menu.foodChoices.size()));
			if (menu.getPrice(choice) > money) {
				print("This customer is a flake! Ordered " + choice + " but only has $" + money);
			}
		} else {
			if (money > menu.getPrice("Steak")) {
				choice = "Steak";
			} else if (money > menu.getPrice("Chicken")) {
				choice = "Chicken";
			} else if (money > menu.getPrice("Pizza")) {
				choice = "Pizza";
			} else if (money > menu.getPrice("Salad")) {
				choice = "Salad";
			} else {
				print("Customer does not have enough money to afford absolutely anything");
				int decisionToLeave = (int)(Math.random() * 10);
				
				print("Decision to leave instead of flake?: " + (decisionToLeave < 5));
				if (decisionToLeave < 5) {
					// Customer leaves because he has no money.
					print("Customer is leaving restaurant.");
					waiter.msgDoneEatingAndPaying(this);
					// Animation of customer leaving.
					customerGui.DoExitRestaurant();
					state = AgentState.DoingNothing;
					event = AgentEvent.none;
					return;
				} else {
					print("Customer is ordering the cheapest thing anyways.");
					choice = "Salad";
				}
			}
		}
		waiter.msgImReadyToOrder(this);
	}
	
	private void GiveOrder() {
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.givingOrder;
				stateChanged();
			}
		},
		3000);
	}
	
	private void OrderGiven() {
		// Messages waiter with order choice
		customerGui.setIcon(choice.substring(0,2) + "?");
		print("Message 6: Sending HereIsMyChoice from Customer to Waiter - " + choice);
		waiter.msgHereIsMyOrder(choice, this);
	}

	private void EatFood() {
		// Handles cooked food delivery and eats food which is managed by a timer
		customerGui.setIcon(choice.substring(0,2));
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		getHungerLevel() * 1000);
	}

	private void LeaveTableToPay() {
		// Leaves restaurant and when done eating, informs Waiter and becomes potentially hungry again
		print("Message 10: Sending DoneEatingAndPaying from Customer to Waiter");
		waiter.msgDoneEatingAndPaying(this);
		customerGui.setIcon("");
		customerGui.DoGoToCashier();
	}
	
	private void PayCashier() {
		print("Message: Sending Paying from Customer to Cashier");
		if (mycheck.amount > money) {
			print("Customer is in debt of the cashier.");
			cashier.msgPaying(mycheck, mycheck.amount - money);
		} else {
			cashier.msgPaying(mycheck, mycheck.amount);
		}
		money -= mycheck.amount;
		customerGui.DoExitRestaurant();
		print("Customer has " + money + " dollars left.");
		print("Customer paid and is leaving restaurant.");
	}

	/**
	 * UTILITIES  ===================================================
	 */

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	@Override
	protected void enterBuilding() {
		System.out.println("Jesse Restaurant Customer entered building");
		B_JesseRestaurant rest = (B_JesseRestaurant)(myPerson.getBuilding()); 
		cashier = rest.cashier;
		host = rest.host;
		jesseRest.gui.CustomerGui c = new jesseRest.gui.CustomerGui(this);
		customerGui = c;
		//BrianAnimationPanel bap = (BrianAnimationPanel)myPerson.building.getPanel();
		jesseRest.gui.AnimationPanel ap = (jesseRest.gui.AnimationPanel)myPerson.building.getPanel();
		ap.addGui(c);
		if (!myPerson.building.getOpen()){
			System.out.println("Customer leaving restaurant");
				customerGui.DoExitRestaurant();
				return;
		}
		gotHungry();
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}


}

