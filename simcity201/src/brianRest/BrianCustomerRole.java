package brianRest;


import SimCity.Base.God;
import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import agent.Agent;
import brianRest.gui.BrianAnimationPanel;
import brianRest.gui.BrianRestaurantPanel;
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
	
	private final int eatingTime = 2000;
	private final int readingMenuTime = 1000;
	
	private String name;
	Timer eatingTimer;
	Timer readMenuTimer;
	private CustomerGui customerGui;

	//Necessary links.
	private BrianHostRole host;
		public BrianHostRole getHost(){
			return host;
		}
		public void setHost(BrianHostRole w){
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
		Do(AlertTag.BrianRest, "is hungry.");
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
			Do(AlertTag.BrianRest, "Tired of Waiting in Restaurant");
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
			Do(AlertTag.BrianRest, "Wrong Order!!!");
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
		Do(AlertTag.BrianRest, "Received: $"+ d);
		totalMoney = d;
		myPerson.money = new Money((int)d, (int) ((d-(int)d) * 100));
		myPerson.hungerLevel += 20;
		customerGui.setPresent(false);
		BrianRestaurantPanel brp = (BrianRestaurantPanel)myPerson.getBuilding().getPanel();
		BrianAnimationPanel br = brp.bap;
		br.removeGui(customerGui);
		myPerson.msgGoHome();
		exitBuilding(myPerson);
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
			return false;
		}
		
		if (state==CustomerState.tiredOfWaiting && event == CustomerEvent.ReceivedCheck){
			state = CustomerState.DoingNothing;
			leaveRestaurantEarly();
			return false;
		}
		
		if (state == CustomerState.WaitingInRestaurant && event == CustomerEvent.followWaiter ){
			state = CustomerState.Seated;
			Do(AlertTag.BrianRest, state.toString());
			followWaiter();
			return false;
		}
		if (state == CustomerState.Seated && event == CustomerEvent.gotMenu){
			state = CustomerState.ReadingMenu;
			ChooseFood();
			return false;
		}
		if (state == CustomerState.ReadingMenu && event == CustomerEvent.readyToOrder){
			state = CustomerState.Ordering;
			CallWaiter();
			return false;
		}
		if (state == CustomerState.Ordering && event == CustomerEvent.ordered){
			state = CustomerState.WaitingForFood;
			TellWaiterMyChoice();
			return false;
		}
		if (state == CustomerState.WaitingForFood && event == CustomerEvent.foodArrived){
			state = CustomerState.Eating;
			EatFood();
			return false;
		}
		if (state == CustomerState.Eating && event == CustomerEvent.doneEating){
			state = CustomerState.RequestingCheck;
			RequestCheck();
			return false;
		}
		if (state==CustomerState.RequestingCheck && event == CustomerEvent.ReceivedCheck){
			state = CustomerState.Leaving;
			leaveTable();
			return false;
		}
		if (state == CustomerState.Leaving && event == CustomerEvent.doneLeaving){
			state = CustomerState.DoingNothing;
			Paying();
			return false;
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
		Do(AlertTag.BrianRest, "is going to restaurant with " + totalMoney);
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
		Do(AlertTag.BrianRest, "is calling Waiter.");
		customerGui.setText("Call Waiter");
		waiter.msgReadyToOrder(this);
	}
	
	private void ChooseFood(){
		Do(AlertTag.BrianRest, "is choosing food.");
		customerGui.setText("Choosing");
			if (!menu.HaveEnoughMoneyForAny(totalMoney) || numberOfTimesOrdering > 2){
				if (!menu.HaveEnoughMoneyForAny(totalMoney))
				Do(AlertTag.BrianRest, "I don't have enough money!");
				else Do(AlertTag.BrianRest, "This restaurant is always out of stock!");
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
		Do(AlertTag.BrianRest, "is leaving.");
		DoLeavingTable();
		if (waiter!=null)
			waiter.msgImDone(this);
		host.msgLeavingEarly(this);
		state = CustomerState.DoingNothing;
		customerGui.DoExitRestaurant(); //set done leaving here.
	}

	private void TellWaiterMyChoice(){
		Do(AlertTag.BrianRest, "tells the waiter he wants " + choice + ".");
		waiter.msgHeresMyChoice(this,choice);
		event = CustomerEvent.ordered;
		customerGui.setText("?");
	}

	private void EatFood() {
		Do(AlertTag.BrianRest, "is eating food.");
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
		Do(AlertTag.BrianRest, "Requesting a check");
		customerGui.setText("Check Please");
		waiter.msgRequestCheck(this);
	}
	
	private void Paying(){
		Do(AlertTag.BrianRest, "I'm paying");
		cashier.msgHeresIsMyMoney(this, totalMoney);
	}

	private void leaveTable() {
		Do(AlertTag.BrianRest, "is leaving.");
		customerGui.setText("Leaving");
		DoLeavingTable();
		if (waiter!=null)
			waiter.msgImDone(this);
		state = CustomerState.Leaving;
		customerGui.DoExitRestaurant(); //set done leaving here.
	}
	
	private void Dead(){
		Do(AlertTag.BrianRest, "has been terminated for lack of payment.");
		God.Get().removePerson(myPerson);
		giveAllMoneyToCashier();
		
		customerGui.setText("Dead");
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}

	public String toString() {
		return "RCmr";
	}
	
	public void exitBuildingFunction(){
		exitBuilding(myPerson);
	}
	
	// ###### GUI Messaging ########
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		Do(AlertTag.BrianRest, "is seated.");
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
		Do(AlertTag.BrianRest, "is following the waiter.");
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
		System.out.println("BrianCustomer entered building");
		totalMoney = myPerson.getMoney().dollars + myPerson.getMoney().cents/100.0;
		B_BrianRestaurant rest = (B_BrianRestaurant)(myPerson.getBuilding()); 
		cashier = rest.cashierRole;
		host = rest.hostRole;
		//add the gui
		brianRest.gui.CustomerGui wg = new brianRest.gui.CustomerGui(this, rest.hostRole);
		customerGui = wg;
		BrianRestaurantPanel brp = (BrianRestaurantPanel)myPerson.building.getPanel();
		BrianAnimationPanel bap = (BrianAnimationPanel)brp.bap;
		bap.addGui(wg);
		
		if (!myPerson.building.getOpen()){
			System.out.println("BrianCustomer leaving restaurant");
				bap.removeGui(wg);
				myPerson.msgGoHome();
				exitBuilding(myPerson);
				return;
		}

		msgIsHungry();
		
	}
	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}


}

