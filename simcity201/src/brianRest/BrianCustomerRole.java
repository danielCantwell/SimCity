package brianRest;


import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import agent.Agent;
import brianRest.gui.CustomerGui;
import brianRest.interfaces.BrianCashier;
import brianRest.interfaces.BrianCustomer;
import brianRest.interfaces.BrianHost;
import brianRest.interfaces.BrianWaiter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;










import javax.swing.Timer;

import restaurant.Menu;

/**
 * Restaurant customer agent.
 */
public class BrianCustomerRole extends Role implements BrianCustomer{
	
	private final int eatingTime = 15000;
	private final int readingMenuTime = 5000;
	
	private String name;
	Timer eatingTimer;
	Timer readMenuTimer;
	private CustomerGui customerGui;

	//Necessary links.
	private BrianHost host;
		public BrianHost getHost(){
			return host;
		}
		public void setHost(BrianHost w){
			host = w;
		}
	private BrianWaiter waiter;	
		public BrianWaiter getWaiter(){
			return waiter;
		}
		public void setWaiter(BrianWaiter w){
			waiter = w;
		}
	private BrianCashier cashier;
		public BrianCashier getCashier(){
			return cashier;
		}
		public void setCashier(BrianCashier w){
			cashier = w;
		}
	private String choice;
	private BrianMenu menu;
	public double totalMoney;
	public double totalCost;
	
	private int numberOfTimesOrdering;

	public enum CustomerState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordering, WaitingForFood, Eating, DoneEating, Leaving, ReadingMenu, NotEnoughmoney, RequestingCheck, Dead, tiredOfWaiting};
	private CustomerState state = CustomerState.DoingNothing;//The start state

	public enum CustomerEvent 
	{none, gotHungry, followWaiter, seated, gotMenu, readyToOrder, ordered, foodArrived, doneEating, doneLeaving, ReceivedCheck};
	CustomerEvent event = CustomerEvent.none;
	

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public BrianCustomerRole(String n){
		super();
		this.name = n;
		totalMoney = 10; //can change in future;
			//hack to change total money.
			if (n.equals("0") || n.equals("5.99")){
				
				totalMoney = Double.parseDouble(n);
			}
		numberOfTimesOrdering = 0;
		
		eatingTimer = new Timer(eatingTime, new ActionListener() {
			   public void actionPerformed(ActionEvent e){
			      event = CustomerEvent.doneEating;
			      stateChanged();
			      eatingTimer.stop();
			   }
			});
		readMenuTimer = new Timer(readingMenuTime, new ActionListener() {
			   public void actionPerformed(ActionEvent e){
			      choice = RandomChoice(menu);
			      event = CustomerEvent.readyToOrder;
			      
			      //Super hack. Allows you to order a specific food off of the menu.
			      if (name.equals("Steak") || name.equals("Pizza") || name.equals("Salad")|| name.equals("Chicken")){
			    	  choice = name;
			      }
			      
			      stateChanged();
			      readMenuTimer.stop();
			   }
			});	
	}

	public String getCustomerName() {
		return name;
	}
// ##################### Messages #################
	
	@Override
	public void msgIsHungry(){ 
		Do("is hungry.");
		event = CustomerEvent.gotHungry; 
		stateChanged();
    }
	@Override
	public void msgFollowMe(BrianMenu m){
		menu = m;
		event = CustomerEvent.followWaiter;
		stateChanged();
	}
	
	@Override
	public void msgFullHouse(){
		int tiredOfWaiting = (int)(Math.random() * 2);
		if (tiredOfWaiting == 1){
			Do("Tired of Waiting in Restaurant");
			state = CustomerState.tiredOfWaiting;
			event = CustomerEvent.ReceivedCheck;
			stateChanged();
		}
	}
	
//Get a message from customer GUI when we reach the table to handle animation. Once we reach the table set Customer State to seated.
	@Override
	public void msgWhatWouldYouLike(){
		event = CustomerEvent.ordered;
		stateChanged();
	}
	
	@Override
	public void msgHeresYourOrder(String order){
		if (order!= choice){
			Do("Wrong Order!!!");
		}
		event = CustomerEvent.foodArrived;
		stateChanged();
	}
	
	@Override
	public void msgOutOfFood(BrianMenu m){
		menu = m;
		event = CustomerEvent.gotMenu;
		state = CustomerState.Seated;
		stateChanged();
	}
	
	//Paying
	@Override
	public void msgHereIsTotal(double totalCost2){
		totalCost = totalCost2;
		event = CustomerEvent.ReceivedCheck;
		stateChanged();
	}
	
	@Override
	public void msgHeresYourChange(double d){
		Do("Received: $"+ d);
		totalMoney = d;
	}
	
	@Override
	public void msgDie(){
		state = CustomerState.Dead;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == CustomerState.DoingNothing && event == CustomerEvent.gotHungry ){
			state = CustomerState .WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		
		if (state==CustomerState.tiredOfWaiting && event == CustomerEvent.ReceivedCheck){
			state = CustomerState.DoingNothing;
			leaveRestaurantEarly();
			return false;
		}
		
		if (state == CustomerState.WaitingInRestaurant && event == CustomerEvent.followWaiter ){
			state = CustomerState.Seated;
			Do(state.toString());
			followWaiter();
			return true;
		}
		if (state == CustomerState.Seated && event == CustomerEvent.gotMenu){
			state = CustomerState.ReadingMenu;
			ChooseFood();
			return true;
		}
		if (state == CustomerState.ReadingMenu && event == CustomerEvent.readyToOrder){
			state = CustomerState.Ordering;
			CallWaiter();
			return true;
		}
		if (state == CustomerState.Ordering && event == CustomerEvent.ordered){
			state = CustomerState.WaitingForFood;
			TellWaiterMyChoice();
			return true;
		}
		if (state == CustomerState.WaitingForFood && event == CustomerEvent.foodArrived){
			state = CustomerState.Eating;
			EatFood();
			return true;
		}
		if (state == CustomerState.Eating && event == CustomerEvent.doneEating){
			state = CustomerState.RequestingCheck;
			RequestCheck();
			return true;
		}
		if (state==CustomerState.RequestingCheck && event == CustomerEvent.ReceivedCheck){
			state = CustomerState.Leaving;
			leaveTable();
			return true;
		}
		if (state == CustomerState.Leaving && event == CustomerEvent.doneLeaving){
			state = CustomerState.DoingNothing;
			Paying();
			return true;
		}
		if (state==CustomerState.NotEnoughmoney && event == CustomerEvent.ReceivedCheck){
			leaveTable();
			state = CustomerState.DoingNothing;
		}
		
		if (state == CustomerState.Dead){
			Dead();
		}
		
		return false;
	}

// ################# ACTIONS ####################

	private void giveAllMoneyToCashier() {
		totalMoney = 0;
	}

	private void goToRestaurant() {
		Do("is going to restaurant with " + totalMoney);
		DoGoToWaitingArea();
		customerGui.setText("Hungry");
		host.msgIWantToEat(this);//send our instance, so he can respond to us
	}
	
	private void followWaiter(){
		customerGui.setText("Walking");
		DoFollowWaiter ();
		//call the gui to follow the waiter.
	}
	
	private void CallWaiter(){
		Do("is calling Waiter.");
		customerGui.setText("Call Waiter");
		waiter.msgReadyToOrder(this);
	}
	
	private void ChooseFood(){
		Do("is choosing food.");
		customerGui.setText("Choosing");
			if (!menu.HaveEnoughMoneyForAny(totalMoney) || numberOfTimesOrdering > 2){
				if (!menu.HaveEnoughMoneyForAny(totalMoney))
				Do("I don't have enough money!");
				else Do("This restaurant is always out of stock!");
				state = CustomerState.NotEnoughmoney;
				event = CustomerEvent.ReceivedCheck;
				stateChanged();
				return;
			}
		numberOfTimesOrdering ++;
		readMenuTimer.restart();
		readMenuTimer.start();
	}
	
	private void leaveRestaurantEarly(){
		Do("is leaving.");
		DoLeavingTable();
		if (waiter!=null)
			waiter.msgImDone(this);
		host.msgLeavingEarly(this);
		state = CustomerState.DoingNothing;
		customerGui.DoExitRestaurant(); //set done leaving here.
	}

	private void TellWaiterMyChoice(){
		Do("tells the waiter he wants " + choice + ".");
		waiter.msgHeresMyChoice(this,choice);
		event = CustomerEvent.ordered;
		customerGui.setText("?");
	}

	private void EatFood() {
		Do("is eating food.");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		eatingTimer.restart();
		customerGui.setText("Eating " + choice);
		eatingTimer.start();
	}
	
	private void RequestCheck(){
		Do("Requesting a check");
		customerGui.setText("Check Please");
		waiter.msgRequestCheck(this);
	}
	
	private void Paying(){
		Do("I'm paying");
		cashier.msgHeresIsMyMoney(this, totalMoney);
	}

	private void leaveTable() {
		Do("is leaving.");
		customerGui.setText("Leaving");
		DoLeavingTable();
		if (waiter!=null)
			waiter.msgImDone(this);
		state = CustomerState.Leaving;
		customerGui.DoExitRestaurant(); //set done leaving here.
	}
	
	private void Dead(){
		Do("has been terminated for lack of payment.");
		giveAllMoneyToCashier();
		customerGui.setText("Dead");
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public String toString() {
		return "customer " + getName();
	}
	
	// ###### GUI Messaging ########
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		Do("is seated.");
		event = CustomerEvent.gotMenu;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = CustomerEvent.doneLeaving;
		stateChanged();
	}
	
	public void DoGoToWaitingArea(){
		customerGui.DoGoToWaitingArea();
	}
	
	
	public void DoGoToDeadLocation(){
		customerGui.setText("Dead");
		customerGui.DoGoToDeadLocation();
	}
	
	//######### GUI Action###########
	private void DoFollowWaiter() {
		Do("is following the waiter.");
	}
	private void DoLeavingTable(){
		customerGui.DoLeavingTable();
	}
	
	//########## UTILITIES ###########
	private String RandomChoice(BrianMenu menu){
		int random = (int)(Math.random() * ((menu.getSize())));
		return menu.choice(random);
	}
	
	public double getMoney(){
		return totalMoney;
	
	}
	
	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}

	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		B_BrianRestaurant rest = (B_BrianRestaurant)(myPerson.getBuilding()); 
		cashier = rest.cashierRole;
		host = rest.hostRole;
		
	}
	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}
}

