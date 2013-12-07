package EricRestaurant;
import java.text.DecimalFormat;

import EricRestaurant.gui.AnimationPanel;
import EricRestaurant.gui.CustomerGui;
import EricRestaurant.interfaces.Cashier;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Host;
import SimCity.Base.Role;
import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_EricRestaurant;

import java.util.*;

/**
 * Restaurant customer agent.
 */
public class EricCustomer extends Role implements Customer, Cashier {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private int count = 0;
	private int t;
	Random generator = new Random(); 
	int m = generator.nextInt(10)+1;
	public double cash = 2000*0.1*m;
	String choice = null;
	DecimalFormat df = new DecimalFormat("#0.00");
	// agent correspondents
	private EricHost host;
	private EricWaiter waiter;
	private EricCashier cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ready, Asked, Eating, DoneEating, Leaving, paying, paid, bumowes};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, ready, asked, received, eating, doneEating, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public EricCustomer(String name){
		super();
		this.name = name;
	}
	public void setHost(EricHost host) {
		this.host = host;
	}

	@Override
	
	public void setCash(EricCashier cash) {
		this.cashier = cash;
	}

	@Override
	
	public void setWaiter(EricWaiter waiter) {
		this.waiter = waiter;
	}

	@Override
	public String getCustomerName() {
		return name;
	}
	// Messages

	@Override
	public void gotHungry() {
		if(this.name.equals("salad")) {
			cash = 5.99;
		}
		if(this.name.equals("bum"))   {
			count++;
			if(count == 1) {
			cash = 8;
			}
			if(count == 2) {
				cash = 20;
				count = 0;
				state = AgentState.bumowes;
				stateChanged();
			}
		}
		print("I'm hungry and have $"+df.format(cash));
		event = AgentEvent.gotHungry;
		
		stateChanged();
	}

	private void print(String string) {
		System.out.println(string);
	}

	@Override
	public void msgSitAtTable(EricWaiter w, int table) {
		print("Received msgSitAtTable");
		t = table;
		this.waiter = w;
		event = AgentEvent.followHost;
		stateChanged();
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	@Override
	public void whatOrder() {
		event = AgentEvent.asked;
		state = AgentState.Ready;
		stateChanged();
	}

	@Override
	public void checkToCust(EricCashier cs) {
		cashier = cs;
		print("Has recieved the check");
		state = AgentState.paying;
		stateChanged();
	}

	@Override
	
	public void foodReceived() {
		event = AgentEvent.received;
		stateChanged();
	}

	@Override
	
	public void giveChange(double change) {
		cash = change;
		print("Has "+df.format(cash)+" amount of money left");
		state = AgentState.Eating;
	}

	@Override
	
	public void bumChange(double change) {
		cash = change;
		print("Has "+df.format(cash)+" amount of money left");
		leaveTable();
	}

	@Override
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

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
		if (state == AgentState.BeingSeated && event == AgentEvent.ready){
			state = AgentState.Ready;
			readyToOrder();
			return true;
		}
		if (state == AgentState.Ready && event == AgentEvent.asked){
			state = AgentState.Asked;
			giveOrder();
			return true;
		}
		if (state == AgentState.Asked && event == AgentEvent.asked){
			state = AgentState.Eating;
			//EatFood();
			return true;
		}
		if (event == AgentEvent.received){
			state = AgentState.Eating;
			try {
				Thread.sleep(5000);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.paying) {
			payCashier();
		}
		if(state == AgentState.bumowes) {
			oweCashier();
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		customerGui.custWait();
		host.msgIWantFood(this, cash);//send our instance, so he can respond to us
	}
	private void SitDown() {
		Do("Being seated. Going to table "+t);
		customerGui.DoGoToSeat(t);//hack; only one table
		event = AgentEvent.ready;
		stateChanged();
	}

	private void readyToOrder() {
		Do("Ready to order");
		waiter.takeMyOrder(this);
	}

	private void giveOrder() {
		Random generator = new Random(); 
		if(this.name.equals("steak")) {
			choice = "Steak";
		}
		if(this.name.equals("bum"))   {
			choice = "Pizza";
		}
		if(this.name.equals("chicken")) {
			choice = "Chicken";
		}
		else {
			int i = generator.nextInt(waiter.menu)+1;
			if (i == 4){
				choice = "Steak";
			}
			else if (i == 3){
				choice = "Chicken";
			}
			else if (i == 2){
				choice = "Pizza";
			}
			else if (i == 1){
				choice = "Salad";
			}
		}
		Do("Here is my order of "+choice);
		waiter.hereIsOrder(this, choice);
	}

	private void payCashier() {
		cashier.hereIsPay(cash, this);
	}
	private void oweCashier() {
		cashier.bumPays(cash, this);
	}
	private void EatFood() {
		event = AgentEvent.eating;
		Do("Eating Food");

		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating");
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		2000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	@Override
	
	public void leaveTable() {
		Do("Leaving.");
		waiter.LeavingTable(this);
		customerGui.DoExitRestaurant();
		
	}
	
	public void leaveRestaurant(){
		System.out.println(myPerson.actions.size());
		AnimationPanel ap = (AnimationPanel)myPerson.building.getPanel();
		ap.removeGui(customerGui);
		myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		System.out.println(myPerson.actions.size());
		exitBuilding(myPerson);
	}

	@Override
	
	public String getName() {
		return name;
	}

	@Override
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#setHungerLevel(int)
	 */
	/* (non-Javadoc)
	 * @see restaurant.Customer#setHungerLevel(int)
	 */
	@Override
	
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#toString()
	 */
	/* (non-Javadoc)
	 * @see restaurant.Customer#toString()
	 */
	@Override

	public String toString() {
		return "customer " + getName();
	}

	@Override

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	@Override
	
	public CustomerGui getGui() {
		return customerGui;
	}

	@Override
	public void setWaiter(EricRestaurant.interfaces.Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(EricRestaurant.interfaces.Waiter w, int table) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void enterBuilding() {
		System.out.println("Eric Restaurant Customer entered building");
		B_EricRestaurant rest = (B_EricRestaurant)(myPerson.getBuilding()); 
		cashier = rest.cashier;
		host = rest.host;
		EricRestaurant.gui.CustomerGui cg = new EricRestaurant.gui.CustomerGui(this, rest.host);
		customerGui = cg;
		AnimationPanel ap = (AnimationPanel)myPerson.building.getPanel();
		ap.addGui(cg);
		customerGui.setText("Customer");
		if (!myPerson.building.getOpen()){
			System.out.println("Customer leaving restaurant");
				customerGui.DoExitRestaurant();
				return;
		}

		System.out.println(host + " is being messaged. Is active?" + host.getActive());
		customerGui.setHungry();
	}
	

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}
}

