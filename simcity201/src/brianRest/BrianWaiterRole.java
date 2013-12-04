package brianRest;

import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import agent.Agent;


import brianRest.gui.BrianAnimationPanel;
import brianRest.gui.WaiterGui;
import brianRest.interfaces.BrianCashier;
import brianRest.interfaces.BrianCook;
import brianRest.interfaces.BrianCustomer;
import brianRest.interfaces.BrianHost;
import brianRest.interfaces.BrianWaiter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

public class BrianWaiterRole extends Role implements BrianWaiter {
	WaiterGui gui;
	List<MyCustomer> myCustomers = new ArrayList<MyCustomer>();
	BrianCook cook;
	BrianHost host;
	BrianCashier cashier;
	
	int waiterNumber = 0;
	public int getWaiterNumber(){
		return waiterNumber;
	}
	
	private enum WaiterState {none, wantABreak, askedBreak, goingOnBreak, onBreak};
	private WaiterState state = WaiterState.none;
	
		public BrianHost getHost(){
			return host;
		}
	String name;
		public String getName(){
			return name;
		}
		
	Timer breakTimer;
	int breakLength = 1; //seconds
	
	// This is to distribute the waiting customers evenly among waiters.
	private int numberOfCustomers;
	enum MyCustomerState {waiting, seated, readyToOrder, ordering, reordering, ordered, orderCooking, orderReady, eating, doneEating,
		gotCheck, wantCheck, waitingCheck, paying, dead, rotting;}
	
	//Animation stuff - To implement in 2c
	private Semaphore atTargetLocation = new Semaphore(0, true);
	boolean idle; //Idle is not a state. It is simply an animation helper variable.
	
	public BrianWaiterRole(String name, BrianHost h, BrianCook c, BrianCashier cash, int numberOfWaiters) {
		this.name = name;
		host = h;
		cook = c;
		cashier = cash;
		waiterNumber = numberOfWaiters;
		
		breakTimer = new Timer(breakLength*1000, new ActionListener() {
			   public void actionPerformed(ActionEvent e){
			      OffBreak();
			      breakTimer.stop();
			   }
			});
	}
	
	//Wants a break
	@Override
	public void msgWantABreak(){
		Do("Want a break after my customers leave.");
		state = WaiterState.wantABreak;
		stateChanged();
	}
	@Override
	public void msgCanGoOnBreak(){
		state = WaiterState.goingOnBreak; 
		stateChanged();
	}

// ######## Messages ################
	@Override
	public void msgSeatAtTable(BrianCustomer c, BrianTable t) {
		/*((BrianCustomer) c).setWaiter(this);
		MyCustomer mc = new MyCustomer(c,t);
		mc.state = MyCustomerState.waiting;
		t.occupiedBy = c;
		idle = true;
		myCustomers.add(mc);
		numberOfCustomers++;*/
		stateChanged();
	};	
	
	@Override
	public void msgReadyToOrder(BrianCustomer c){  		
		for (MyCustomer mc : myCustomers){
			/*if (mc.customer == c){
				Do("Received customer call");
				mc.state = MyCustomerState.readyToOrder;
				stateChanged();
			}
			*/
		}
	}
	@Override
	public void msgHeresMyChoice(BrianCustomer ca, String c){ 
		for (MyCustomer mc : myCustomers){
			/*if (mc.customer == ca){
				//mc.order = new Order(c, this, mc.table.tableNumber);
				mc.choice = c;
				mc.state = MyCustomerState.ordered;
				stateChanged();
			}*/
		}
	}
	@Override
	public void msgOutOfFood(String choice, int table){
		for (MyCustomer mc : myCustomers){
			if (mc.table.tableNumber ==  table){
				//Do go back to customer and ask for an order.
				mc.state = MyCustomerState.reordering;
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgOrderIsReady(String o, int tableNumber){ 		
		for (MyCustomer mc : myCustomers){
			if (mc.choice == o && mc.table.tableNumber == tableNumber){
				mc.state = MyCustomerState.orderReady;
				stateChanged();
			}
		}
	}
	@Override
	public void msgImDone(BrianCustomer c){ 
		for (MyCustomer mc: myCustomers){
			/*if (mc.customer == c){
				mc.state = MyCustomerState.doneEating;
				stateChanged();
			}
			*/
		}
	}
	
	//Having to do with the check.
	@Override
	public void msgRequestCheck (BrianCustomer c){
		for (MyCustomer mc: myCustomers){
			/*if (mc.customer == c){
				mc.state = MyCustomerState.wantCheck;
				stateChanged();
			}
			*/
		}
	}
	
	@Override
	public void msgHereIsCheck(double totalCost, BrianCustomer c){
		for(MyCustomer mc: myCustomers){
			/*if (mc.customer == c){
				mc.totalCost = totalCost;
				mc.state = MyCustomerState.gotCheck;
				stateChanged();
			}
			*/
		}
	}
	
	@Override
	public void msgCleanUpDeadCustomer(BrianCustomer c){
		/*Do("Readying to kill customer");
		MyCustomer mc = new MyCustomer(c, null);
		myCustomers.add(mc);
		mc.state = MyCustomerState.dead;
		stateChanged();
		*/
	}
	
	
//##########  Scheduler  ##############
	protected boolean pickAndExecuteAnAction(){
		try{
		if (!myCustomers.isEmpty()){
			for (MyCustomer mc : myCustomers){
				if (mc.state == MyCustomerState.waiting){
						idle = false;
						if (mc.table != null)
						SeatCustomer(mc.table, mc);
						
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.readyToOrder){
					idle = false;
					TakeOrder(mc);
					
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.ordered){ 
					idle = false;
					GiveOrderToCook(mc, true);
					
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if(mc.state == MyCustomerState.reordering){
					idle = false;
					TakeReorder(mc);
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.orderReady){
					idle = false;
					GiveFoodToCustomer(mc);
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.wantCheck){
					idle = false;
					AskCashierForTotal(mc);
				}
			}
			
			for(MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.gotCheck){
					idle = false;
					GiveCustomerCheck(mc);
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.doneEating){
					idle = false;
					CustomerLeaving(mc);
				}
			}
			
			for (MyCustomer mc: myCustomers){
				if (mc.state == MyCustomerState.dead){
					idle = false;
					CleanDeadCustomer(mc);
				}
			}
			
			DoIdle();
			return true;
			}
		
			if (state == WaiterState.wantABreak){
				IWantABreak();
				return true;
			}
			
			if (state == WaiterState.goingOnBreak && numberOfCustomers == 0){
				TakeABreak();
				return true;
			}
		}
	
		catch(ConcurrentModificationException e){
			DoIdle();
			return false;
		}
		
		DoIdle();
		return false;
		
	}
	
//############ Action ################
	//Want a break;
	private void IWantABreak(){
		Do("I'm telling the host I want a break.");
		state = WaiterState.askedBreak;
		host.msgWaiterWantsABreak(this);
	}
	
	//Take a break;
	private void TakeABreak(){
		Do("I'm taking a break!");
		DoTakeABreak();
		state = WaiterState.onBreak;
		breakTimer.restart();
		breakTimer.start();
	}
	//OffBreak
	private void OffBreak(){
		Do("I'm coming back to work!");
		state = WaiterState.none;
		host.msgWaiterOffBreak(this);
		gui.DoOffBreak();
	}
	
	private void SeatCustomer(BrianTable t, MyCustomer mc) {
		DoGetCustomer();
		//Do("is seating " + ((BrianCustomer) mc.customer).getName());
		//mc.customer.msgFollowMe(new BrianMenu());
		mc.state = MyCustomerState.seated;
		DoSeatCustomer(t.getTableNumber(), mc);
	}
	
	private void TakeOrder(MyCustomer mc){
		//Do("is taking " + ((JesseCustomer) mc.customer).getName() + "'s order.");
		DoWalkToCustomer(mc, "");
		//mc.customer.msgWhatWouldYouLike();
		mc.state = MyCustomerState.ordering;
	}
	 
	private void GiveOrderToCook(MyCustomer mc, boolean displayText){
		DoGiveOrderToCook();
		mc.state = MyCustomerState.orderCooking;
		cook.msgHeresAnOrder(mc.choice, this, mc.table.tableNumber);
	}
	
	private void TakeReorder(MyCustomer mc){
		//Do("Going to customer " + ((BrianCustomer) mc.customer).getCustomerName() + " for a reorder.");
		DoWalkToCustomer(mc, "Reordering");
		BrianMenu m = new BrianMenu();
		m.remove(mc.choice);
		//mc.customer.msgOutOfFood(m);
		mc.state = MyCustomerState.ordering;
	}

	private void GiveFoodToCustomer(MyCustomer mc){
		DoGiveOrderToCook();
		if (cook instanceof BrianCook)
			//((BrianCook) cook).DoRemovePlate(mc.choice);
		DoWalkToCustomer(mc, mc.choice);
		//Do("is giving food to " + ((BrianCustomer) mc.customer).getName());	
		mc.state = MyCustomerState.eating;
		//mc.customer.msgHeresYourOrder(mc.choice);
	}
	
	private void AskCashierForTotal(MyCustomer mc){
		DoGetCheck();
		//Do("Asking "+ cashier.name + " for check.");
		mc.state = MyCustomerState.waitingCheck;
		//cashier.msgHereIsCheck(mc.choice, mc.customer, this);
	}
	
	private void GiveCustomerCheck(MyCustomer mc){
		DoWalkToCustomer(mc, "Giving Check");
		//mc.customer.msgHereIsTotal(mc.totalCost);
		mc.state = MyCustomerState.paying;
	}
	
	private void CustomerLeaving(MyCustomer c){
		//Do(((BrianCustomer) c.customer).getName() + " is leaving the restaurant.");
		host.msgTableIsClear(c.table);
		myCustomers.remove(c);
		numberOfCustomers--;
	}
	
	private void CleanDeadCustomer(MyCustomer mc){
		//Do("Killing Customer "+ ((BrianCustomer) mc.customer).getName());
		DoGetDeadCustomer();
		//mc.customer.DoGoToDeadLocation();
		DoGoToDeadLocation();
		mc.state = MyCustomerState.rotting;
		myCustomers.remove(mc);
	}

	//##GUI ACTIONS###
	private void DoGoToDeadLocation(){
		gui.setText("Carrying");
		gui.DoGoToDeathPile();
		atLocAcquire();
	}
	
	private void DoTakeABreak(){
		gui.setText("OnBreak");
		gui.DoTakeABreak();
		atLocAcquire();
	}
	
	private void DoSeatCustomer(int tableNum, MyCustomer mc){
		gui.setText("Seating Customer");
		//gui.DoBringToTable(mc.customer, tableNum);
		//if (mc.customer instanceof BrianCustomer)
		//((BrianCustomer) mc.customer).getGui().DoGoToSeat(tableNum);
		atLocAcquire();
	}
	
	public void DoGetCustomer(){
		Do("is getting customer.");
		gui.setText("Getting Customer");
		gui.DoGetCustomer();
		atLocAcquire();
	}
	
	public void DoGetDeadCustomer(){
		Do("is removing a dead customer.");
		gui.setText("Killing Customer");
		gui.DoGetCustomer();
		atLocAcquire();
	}
	
	public void DoGetCheck(){
		gui.setText("Get Check");
		gui.DoGoToCashier();
		atLocAcquire();
	}
	
	public void DoWalkToCustomer(MyCustomer mc, String displayText){
		gui.DoWalkToCustomer(mc.table, displayText);
		atLocAcquire();
	}
	
	public void DoGiveOrderToCook(){
		Do("gives an order to the cook.");
		gui.setText("Going to Cook");
		gui.DoGiveOrderToCook();
		atLocAcquire();
	}
	
	private void atLocAcquire(){
		try {
			atTargetLocation.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}
	
	private void DoIdle(){
		//System.out.println("Do idle");
		gui.DoIdle();
		//gui.setText("Idle");
		idle = false;
	}

//#####    GUI Utilities  ####
	public void atLocation() {//from animation
				atTargetLocation.release();// = true;
				idle = true;
	}
	
	public void setGUI(WaiterGui wg){
	    	gui = wg;
	}
	public WaiterGui getGUI(){
		return gui;
	}
	
//#### Inner Class ####	
	private class MyCustomer {
		BrianCustomer customer;
		   BrianTable table;
		   String choice;
		   double totalCost;
		   MyCustomerState state = MyCustomerState.waiting;
		   
		   public MyCustomer(BrianCustomer c, BrianTable t) {
				customer = c;
				table = t;
				totalCost = 0;
			}
	}

	@Override
	protected void enterBuilding() {		
		//add gui
		brianRest.gui.WaiterGui wg = new brianRest.gui.WaiterGui(this);
		gui = wg;
		BrianAnimationPanel bap = (BrianAnimationPanel)myPerson.building.getPanel();
		bap.addGui(wg);
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}

}
