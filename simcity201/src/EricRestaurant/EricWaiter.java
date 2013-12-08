package EricRestaurant;

import EricRestaurant.EricCook.Food;
import EricRestaurant.EricCustomer.AgentState;
import EricRestaurant.gui.AnimationPanel;
import EricRestaurant.gui.HostGui;
import EricRestaurant.interfaces.Cashier;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;
import SimCity.Base.Role;
import SimCity.Base.Person.Intent;
import agent.Agent;
import brianRest.gui.BrianAnimationPanel;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */

public class EricWaiter extends Role implements Waiter {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<myCustomer> customer = Collections.synchronizedList(new ArrayList<myCustomer>());
	public class myCustomer {
		Waiter w;
		int wNum;
		Customer c;
		double money;
		int table;
		String choice;
		state s;
	}
	public enum state {nothing, hostcalled, ready, asked, waiting, pickingup, gotOrder, gavecook, gotFromCook, finished, leaving, gotcheck};
	private state s = state.nothing;//The start state

	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public int menu = 4;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore toCook = new Semaphore(0,true);
	private Semaphore atCust = new Semaphore(0,true);
	public HostGui hostGui = null;
	public EricHost host;
	public EricCashier ca;
	public String lowFood = null;
	Map<String, Double> mymap = new HashMap<String, Double>();

	public EricWaiter(String name, EricHost h, EricCashier c) {
		super();
		this.name = name;
		host = h;
		ca = c;
		mymap.put("Steak", 15.99);
		mymap.put("Pizza", 8.99);
		mymap.put("Chicken", 10.99);
		mymap.put("Salad", 5.99);
	}

	@Override
	public void msgWaiter(String c) {
		print("Cook no longer has any "+c);
		lowFood = c;
	}

	@Override
	public void restock(String c) {
		print("Cook restocked on "+c);
		lowFood = null;
	}
	private void print(String string) {
		System.out.println(string);
	}

	@Override
	public void setCashier(EricCashier c) {
		ca = c;
	}

	@Override
	public void setHost(EricHost h) {
		host = h;
	}

	@Override
	public String getMaitreDName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void HostCalled(EricCustomer cust, int table, int num){
		myCustomer mycust = new myCustomer();
		mycust.c = cust;
		mycust.wNum = num;
		hostGui.waiting(mycust.wNum);
		mycust.s = state.hostcalled;
		mycust.money = cust.cash;
		mycust.table = table;
		print("Host called me to seat " + cust.getCustomerName()+" at table: "+mycust.table);
		customer.add(mycust);
		stateChanged();
	}

	@Override
	public void takeMyOrder(Customer cust) {
		for(myCustomer mc : customer) {
			if(mc.c == cust) {
				mc.s = state.ready;
			}
		}
		print("preping to take order");
		stateChanged();
	}

	@Override
	public void hereIsOrder(Cashier cust, String choice) {
		if(cust.getName().equals("bum")) {
			for(myCustomer cx : customer) {
				if(cx.c == cust) {
					cx.choice = choice;
					cx.s = state.gotOrder;
					menu = 4;	
				}
			}
		}
		else{
			for(myCustomer cx : customer) {
				if(cx.c==cust) {
					if(!(lowFood == choice)) {
						double temp = mymap.get(choice);
						if(cx.money >= temp){
							cx.choice = choice;
							cx.s = state.gotOrder;
							menu = 4;
						}
						if (cx.money < temp) {
							print("You do not have enough money to order that, reorder please.");
							reorder(cx);
						}	
					}
					else { 
						if(lowFood == "Salad"){
							print(cx.c+"decided to leave the restaurant");
							cx.c.leaveTable();
							hostGui.waiting(cx.wNum);
						}
						else{
							print("Cook is out of "+choice+" please reorder"); 
							Random generator = new Random(); 
							int i = generator.nextInt(2)+1;
							if( i == 1) {
								goTakeOrder(cx);
							}
							if(i == 2 ) {
								print(cx.c+"decided to leave the restaurant");
								cx.c.leaveTable();
								hostGui.waiting(cx.wNum);
							}
						}
					}
				}
			}
		}
		stateChanged();
	}

	@Override
	public void reorder(myCustomer cx) {
		if (cx.money < 5.99) {
			print("You do not have enough money to order anything.");
			cx.c.leaveTable();
			hostGui.waiting(cx.wNum);
		}
		else if (cx.money < 10.99) {
			print("You can only order Salad or Pizza");
			menu = 2;
			goTakeOrder(cx);
		}
		else if (cx.money < 15.99) {
			print("You can only order Salad, Pizza, or Chicken");
			menu = 3;
			goTakeOrder(cx);
		}

		else {

			if(menu == 0) {
				print("You do not have enough money to order anything.");
				cx.c.leaveTable();
				hostGui.waiting(cx.wNum);
			}
			else goTakeOrder(cx);
		}

	}

	@Override
	public void atTheCook() {
		toCook.release();
	}

	@Override
	public void atTheCust() {
		atCust.release();
	}

	@Override
	public void orderDone(String choice) {
		for (myCustomer mc : customer) {
			if (mc.choice == choice) {
				mc.s = state.waiting;
				stateChanged();
			}
		}
	}


	@Override
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}

	@Override
	public void atCook(String choice) {
		for (myCustomer mc : customer) {
			if (mc.choice == choice) {
				mc.s = state.gotFromCook;
				stateChanged();
			}
		}
	}

	@Override
	public void waiterGotCheck(double p, Customer c, EricCashier cs) {
		print("Recieved check from Cashier");
		for (myCustomer mc : customer) {
			if (mc.c == c) {
				mc.c.checkToCust(cs);
			}
		}
	}

	@Override
	public void LeavingTable(Customer c) {
		synchronized(customer){
			for (myCustomer mc : customer) {
				if(mc.c == c) {
					mc.s = state.leaving;
					stateChanged();
				}
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//System.out.println("customer size "+customer.size()+" and myPerson is this: "+myPerson.toString());
		try {
			for(myCustomer mc : customer) {
				if(mc.s == state.hostcalled) {
					seatCustomer(mc.c, mc.table);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		try {
			for (myCustomer c : customer) {
				if (c.s == state.gotFromCook) {
					bringToCust(c);
					return true;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		try {
			for (myCustomer c : customer) {
				if (c.s == state.waiting) {
					deliver(c);
					return true;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		try{
			for (myCustomer c : customer) {
				if (c.s == state.gotOrder){
					gotOrder(c);
					return true;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		try {
			for (myCustomer c : customer) {
				if (c.s == state.ready){
					goTakeOrder(c);
					return true;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		try {
			for (myCustomer c : customer) {
				if (c.s == state.leaving){
					alertHost(c);
					return true;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}
		return false;

	}

	// Actions

	private void seatCustomer(Customer cust, int table) {
		//hostGui.DoLeaveCustomer();
		try {
			Thread.sleep(1500);
			//atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cust.msgSitAtTable(this, table);
		DoSeatCustomer(cust, table);
		try {
			Thread.sleep(3000);
			//atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		table.setOccupant(cust);
		//		waitingCustomers.remove(cust);
		// hostGui.DoLeaveCustomer();
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(Customer customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		hostGui.DoBringToTable(customer, table); 
	}

	private void goTakeOrder(myCustomer c) {
		c.c.whatOrder();
		c.s = state.asked;
	}

	private void gotOrder(myCustomer c) {
		Do(" Giving Customer order to cook");
		try {
			host.cook.giveCook(c.choice, this);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("----------Host: "+host+"     Cook: "+host.cook);
		}
		c.s = state.gavecook;

		//customer.remove(c);	//if i don't remove it, it continually alternates between this and the msg in Cook
	}

	@Override
	public void deliver(myCustomer c) {

		hostGui.getfromCook();
		c.s = state.pickingup;
		print("Picking up order: "+c.choice+" for customer: "+c.c.getName());
		try {
			//toCook.acquire();
			Thread.sleep(1000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		host.cook.msgImHere(this);
	}

	@Override
	public void bringToCust(myCustomer c) {
		print("Bringing "+c.choice+" to "+c.c.getName());
		hostGui.foodToCust(c.table);
		c.c.foodReceived();
		ca.askCheck(c.c, c.choice, this);
		c.s = state.finished;
		try {
			//atCust.acquire();
			Thread.sleep(3000);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		hostGui.waiting(c.wNum);
	}

	@Override
	public void alertHost(myCustomer c) {
		host.msgLeavingTable(c.c, this);
		customer.remove(c);
	}

	@Override
	public void askBreak() {
		host.canIBreak(this);
	}

	@Override
	public void offBreak() {
		host.backFromBreak(this);
	}


	@Override
	public void addWaiter() {
		host.newWaiter(this);
	}

	@Override
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	@Override
	public HostGui getGui() {
		return hostGui;
	}
	@Override
	protected void enterBuilding() {
		EricRestaurant.gui.HostGui hg = new EricRestaurant.gui.HostGui(this);
		hostGui = hg;
		AnimationPanel ap = (AnimationPanel)myPerson.building.getPanel();
		ap.addGui(hg);	
		hostGui.setText("Waiter");
		hostGui.waiting(1);
	}
	@Override
	public void workOver() {
		hostGui.DoLeaveCustomer();
		AnimationPanel ap = (AnimationPanel)myPerson.building.getPanel();
		ap.removeGui(hostGui);
		myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		exitBuilding(myPerson);
	}
}
