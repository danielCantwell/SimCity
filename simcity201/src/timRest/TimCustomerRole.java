package timRest;

import restaurant.gui.CustomerGui;
import timRest.gui.TimCustomerGui;
import timRest.interfaces.TimCashier;
import timRest.interfaces.TimCustomer;
import timRest.interfaces.TimWaiter;
import SimCity.Base.Role;
import agent.Agent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class TimCustomerRole extends Role implements TimCustomer{
	
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private TimCustomerGui customerGui = new TimCustomerGui(this);
	
	private HashMap<String, Double> choices;
	private double cash;
	private double amountOwed;
	private Random rand = new Random();
	private Point waitingPos;
	private Point tablePos;

	// agent correspondents
	private TimHostRole host;
	private TimWaiter waiter;
	private TimCashier cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{GotHungry, DoingNothing, GoToWait, WaitingInRestaurant, BeingSeated, Sitting, Deciding, ReadyToOrder, Ordering, ReceivedOrder, Eating, DoneEating, Leaving, 
		WaitingToPay, GoPay, GoingToCashier, PayingCheck, DonePaying, NoFood, WaitTooLong};
	private AgentState state = AgentState.DoingNothing;//The start state

	/**
	 * Constructor for Customer class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public TimCustomerRole(){
		super();
		state = AgentState.GotHungry;
		choices = null;
		waitingPos = new Point(-1, -1);
		amountOwed = 0.0d;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(TimHostRole host) {
		this.host = host;
	}
	
	public void setCashier(TimCashier cashier)
	{
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return getName();
	}
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		state = AgentState.GotHungry;
		cash += 3.0d + (double)rand.nextInt(15);
		if (cash >= 20.0d)
		{
			// cap money at $20
			cash = 20.0d;
		}
		// test names
		if (getName().equals("_Stalin"))
		{
			cash = 4.00d;
		}
		else if (getName().equals("_Plain_Salad"))
		{
			cash = 5.99d;
		}
		else if (getName().equals("_Rockefeller"))
		{
			cash = 20.0d;
		}
		if (amountOwed > 0.0d)
		{
			Do("I have $" + cash + " and I owe this restaurant $" + amountOwed + ".");
		}
		else
		{
			Do("I have $" + cash + ".");
		}
		stateChanged();
	}
	
	public void msgPleaseSit(Point pos)
	{
		waitingPos = pos;
		Do("Sitting in waiting area.");
		stateChanged();
	}
	
	public void msgWeAreFull()
	{
		state = AgentState.WaitTooLong;
		stateChanged();
	}
	
	public void msgFollowMeToTable(TimWaiter waiter)
	{
		this.waiter = waiter;
		state = AgentState.BeingSeated;
		stateChanged();
	}

	public void msgSitAtTable(Point tablePos, HashMap<String, Double> choices) 
	{
		//print("Received msgSitAtTable");
		this.tablePos = tablePos;
		state = AgentState.Sitting;
		this.choices = choices;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() 
	{
		state = AgentState.Ordering;
		stateChanged();
	}
	
	public void msgWeAreOut(HashMap<String, Double> newMenu)
	{
		state = AgentState.Deciding;
		choices = newMenu;
		stateChanged();
	}

	public void msgNoMoreFood()
	{
		state = AgentState.NoFood;
		choices = new HashMap<String, Double>();
		stateChanged();
	}
	
	public void msgHereIsYourOrder(String choice) 
	{
		Do("Recieved order of " + choice);
		state = AgentState.ReceivedOrder;
		stateChanged();
	}
	
	public void msgHereIsTheCheck(double price)
	{
		amountOwed += price;
		state = AgentState.GoPay;
		stateChanged();
	}

	public void msgAnimationFinishedGoToWait() {
		//from animation
		state = AgentState.WaitingInRestaurant;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		state = AgentState.Deciding;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToCashier()
	{
		state = AgentState.PayingCheck;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	Customer is a finite state machine
		if (state == AgentState.NoFood)
		{
			//leave
			LeaveTable();
			LeaveRestaurant();
		}
		if (waitingPos.x != -1 && waitingPos.y != -1 && state == AgentState.GoToWait)
		{
			customerGui.DoGoToWaiting(waitingPos);
			return true;
		}
		if (state == AgentState.GotHungry )
		{
			state = AgentState.GoToWait;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.BeingSeated)
		{
			Follow(waiter);
			return true;
		}
		if (state == AgentState.Sitting)
		{
			SitDown();
			return true;
		}
		if (state == AgentState.Deciding)
		{
			if (choices.isEmpty())
			{
				state = AgentState.Leaving;
				LeaveTable();
			}
			else
			{
				Decide();
			}
			return true;
		}
		if (state == AgentState.ReadyToOrder)
		{
			TellWaiterReady();
			return true;
		}
		if (state == AgentState.Ordering)
		{
			Order();
			return true;
		}
		if (state == AgentState.ReceivedOrder)
		{
			EatFood();
			return true;
		}
		if (state == AgentState.DoneEating)
		{
			// pay
			AskForCheck();
			return true;
		}
		if (state == AgentState.GoPay)
		{
			LeaveTable();
			GoToCashier();
			return true;
		}
		if (state == AgentState.PayingCheck)
		{
			PayCheck();
			return true;
		}
		if (state == AgentState.DonePaying)
		{
			LeaveRestaurant();
			return true;
		}
		if (state == AgentState.WaitTooLong)
		{
			WaitedTooLong();
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
		
		//will leave if waiting for too long
		//start timer
		timer.schedule(new TimerTask() {
			public void run() {
				if (state == AgentState.WaitingInRestaurant)
				{
					state = AgentState.WaitTooLong;
					stateChanged();
				}
			}
		}, 60000);
	}
	
	private void Follow(TimWaiter waiter)
	{
		// follow waiter
		if (state == AgentState.BeingSeated)
		{
			if (waiter instanceof TimWaiterRole)
			{
				customerGui.DoFollowWaiter((TimWaiterRole)waiter);
			}
		}
	}

	private void SitDown()
	{
		state = AgentState.DoingNothing;
		customerGui.DoGoToSeat(tablePos);//hack; only one seat
	}
	
	private void Decide()
	{
		Do("Deciding what to eat.");
		state = AgentState.DoingNothing;
		// decide what to order
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done choosing.");
				state = AgentState.ReadyToOrder;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void TellWaiterReady()
	{
		Do("Hailing waiter. (" + waiter.getName() + ")");
		waiter.msgImReadyToOrder(this);
		state = AgentState.DoingNothing;
	}
	
	private void Order()
	{
		ArrayList<String> items = new ArrayList<String>(choices.keySet());
		for (int i = choices.size()-1; i >= 0; i--)
		{
			// if it is too expensive, remove the option of choosing it
			if (choices.get(items.get(i)) > cash - amountOwed)
			{
				items.remove(i);
			}
		}
		String choice = null;
		if (!items.isEmpty())
		{
			choice = items.get(rand.nextInt(items.size()));
		}
		// test names
		if (getName().equals("_Steak"))
		{
			choice = "Steak";
		}
		else if (getName().equals("_Chicken"))
		{
			choice = "Chicken";
		}
		else if (getName().equals("_Salad"))
		{
			choice = "Salad";
		}
		else if (getName().equals("_Pizza"))
		{
			choice = "Pizza";
		}
		if (choice != null)
		{
			waiter.msgIWouldLike(this, choice);
			state = AgentState.DoingNothing;
		}
		else
		{
			// 1 in 10 chance per item of the person being bad and ordering something the person can't afford
			if (rand.nextDouble() >= 0.9d)
			{
				ArrayList<String> badChoices = new ArrayList<String>(choices.keySet());
				waiter.msgIWouldLike(this, badChoices.get(rand.nextInt(badChoices.size())));
				state = AgentState.DoingNothing;
			}
			else
			{
				// cannot choose anything
				Do("I can't afford this...");
				LeaveTable();
				LeaveRestaurant();
			}
		}
	}

	private void EatFood() {
		Do("Eating Food");
		state = AgentState.Eating;
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating.");
				state = AgentState.DoneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		10000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	

	private void AskForCheck()
	{
		Do("Can I get the check, please?");
		state = AgentState.WaitingToPay;
		waiter.msgCanIHaveCheck(this);
	}

	private void GoToCashier()
	{
		customerGui.DoGoToCashier();
		state = AgentState.GoingToCashier;
	}
	
	private void PayCheck()
	{
		// if enough money, pay
		if (amountOwed <= cash)
		{
			cashier.msgHereIsTheMoney(amountOwed);
			cash -= amountOwed;
			amountOwed = 0.0d;
		}
		// not enough money
		else
		{
			cashier.msgHereIsPartialMoney(cash, amountOwed);
			amountOwed -= cash;
			cash = 0.0d;
		}
		state = AgentState.DonePaying;
		stateChanged();
	}
	
	private void LeaveTable() {
		Do("Leaving table.");
		waiter.msgLeavingTable(this);
	}

	private void WaitedTooLong()
	{
		Do("This wait is too long.");
		host.msgImLeaving(this);
		LeaveRestaurant();
	}
	
	private void LeaveRestaurant()
	{
		Do("Bye!");
		customerGui.DoExitRestaurant();
		state = AgentState.DoingNothing;
		choices = null;
	}

	// Accessors, etc.

	public String getName() {
		return myPerson.name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(TimCustomerGui g) {
		customerGui = g;
	}

	public TimCustomerGui getGui() {
		return customerGui;
	}

	public void print(String string) {
		System.out.println(string);
	}


	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
        myPerson.Do("The restaurant is closing...");
        exitBuilding(myPerson);
	}
}

