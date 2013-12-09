package brianRest;

//import javax.swing.*;

import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.trace.AlertTag;
import agent.Agent;
import brianRest.OrderStand.Orders;
import brianRest.gui.BrianAnimationPanel;
import brianRest.gui.CookGui;
import brianRest.interfaces.BrianCook;
import brianRest.interfaces.BrianWaiter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
//import restaurant.HostAgent.HostState;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.List;
//import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;


public class BrianCookRole extends Role implements BrianCook {
	
	private String name;
	CookGui gui;
	
	//A list of ALL orders that the cook is attending to.
	private List<Order> orders;
	
	//On the orderstand
	OrderStand orderstand;

	//A map containing all the foods and their cook times. Implement in Constructor pls!
	private Map<String, Food> foodDictionary = new HashMap<String, Food>();
	
	private String searchMarketsFor = "";
	
	private enum OrderState { pending, checkingAmount, cooking, cooked, notified;}
	
	private int max_Capacity = 4;
	
	private Semaphore atTargetLocation = new Semaphore(0, true);
	
	boolean wantToGoHome = false;

	//Constructor
	public BrianCookRole(String name){
	  this.name = name;
	  orders = new ArrayList<Order>();
	  
	  //Tree map
	  foodDictionary.put("Steak", new Food("Steak", 1000, 1));
	  foodDictionary.put("Chicken", new Food("Chicken", 1000, 1));
	  foodDictionary.put("Salad", new Food("Salad", 1000, 1));
	  foodDictionary.put("Pizza", new Food("Pizza", 1000, 1));
	  
	}
		
//########## Messages  ###############
	@Override
	public void msgHeresAnOrder(String o, BrianWaiter w, int tableNumber)
	{
		Order order = new Order(o, w, tableNumber);
		 orders.add(order);
		 stateChanged();
	}
	public void msgStateChanged(){
		stateChanged();
	}
	
	@Override
	public void msgFillOrder(String choice, int amount, boolean filled){
		Do(AlertTag.BrianRest, "Refilling " + choice + " by " + amount+".");
		Food f = foodDictionary.get(choice);
		f.amount = foodDictionary.get(choice).amount+amount;
		foodDictionary.put(choice, f);
		if (filled){
			return;
		}
		else{
			f.orderFromIndex ++;
			searchMarketsFor = choice;
			stateChanged();
		}
	}
	
	public void msgLeaveRestaurant(){
		wantToGoHome = true;
		stateChanged();
	}
	
	
//##########  Scheduler  ##############
@Override
	protected boolean pickAndExecuteAnAction() {
		// if there exists an Order o in pendingOrder such that o.OrderState == pending
		//then CookOrder(o);
	try{

		if (orders.size() > 0){
				//Look for all pending orders.
				for(Order o : orders){
					if (o.getState() == OrderState.pending){
						CookOrder(o);
						return true;
					}
					
				}
				
				if (orderstand.getSize() > 0){
					makeCookOrder(orderstand.popFirstOrder());
					return true;
				}
				
				for (int i=0; i<orders.size();i++){
					
					if (orders.get(i).getState() == OrderState.cooked){
						tellWaiterOrderIsReady(orders.get(i), i);
						i--;
						return true;
					}
				}
		}
		
		if (wantToGoHome){
			leaveRestaurant();
		}
	}
	catch(ConcurrentModificationException e){
			return false;
	}
		
		return false;
	}
		
//########## Actions ###############
	private void makeCookOrder(Orders o){
		Order order = new Order(o.getChoice(), o.getWaiter(), o.getTableNumber());
		 orders.add(order);
		 stateChanged();
	}

	private void CookOrder(Order o){
		o.state = OrderState.checkingAmount;
		Food temp = foodDictionary.get(o.choice);
		if (temp.amount == 0){
			o.waiter.msgOutOfFood(o.choice, o.tableNumber);
			orders.remove(o);
			Do(AlertTag.BrianRest, "Out of "+ o.choice);
			return;
		}
		DoGoToGrill();
		gui.DoGoToGrills(o.choice);
		if (temp.amount == 1){
			//order more for the restaurant;
			Do(AlertTag.BrianRest, "Last "+ o.choice+". Ordering more.");
		}
		
		temp.amount --;
		
		  Do(AlertTag.BrianRest, "is cooking " + o.choice + ".");
		  o.setTimer(foodDictionary.get(o.choice).cookTime);
	}
	
	
	private void tellWaiterOrderIsReady(Order o, int index){
		DoGoToGrill();
		gui.DoRemoveFromGrill(o.choice);
		DoGoToPlates();
		gui.DoGoToPlates(o.choice);
		
		o.waiter.msgOrderIsReady(o.choice, o.tableNumber);
		o.setState(OrderState.notified);
		orders.remove(index);
	}
	
	private void DoGoToGrill(){
		gui.DoGoToGrills();
		atLocAcquire();
	}
	
	private void DoGoToPlates(){
		gui.DoGoToPlates();
		atLocAcquire();
	}
	
	
	public void DoRemovePlate(String choice){
		gui.DoRemovePlate(choice);
	}
//################    Utility     ##################
	public String toString(){
		return "Cook " + name;
	}
	
	public void leaveRestaurant(){
			DoExitBuilding();
			wantToGoHome = false;
			exitBuilding(myPerson);
	}
	
	private void DoExitBuilding(){
		gui.DoLeaveRestaurant();
		try {
			atTargetLocation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		wantToGoHome = false;
		BrianAnimationPanel br = (BrianAnimationPanel)myPerson.getBuilding().getPanel();
		br.removeGui(gui);
		System.out.println("Cook is leaving restaurant");
		myPerson.msgGoHome();
		exitBuilding(myPerson);
	}
	
	private void atLocAcquire(){
		try {
			atTargetLocation.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void atLocation() {//from animation
		atTargetLocation.release();// = true;
	}

	public void setGUI(CookGui wg){
		gui = wg;
	}
	public CookGui getGUI(){
		return gui;
	}

//######################## End of Class Cook#############################
	
	//#### Inner Class ####	
	private class Food {
		   private String choice;
		   private int cookTime;
		   private int amount;
		   
		   private int orderFromIndex = 0;
		   
		   
		   private Food(String c, int ct, int amt){
			   choice = c;
			   cookTime = ct;
			   amount = amt;
		   }
	}
	
	private class Order {
		  String choice;
		  BrianWaiter waiter;
		  int tableNumber;
		  Timer timer;
		  int orderTime;
		  
		  private OrderState state = OrderState.pending;
		  
		  public Order(String c, BrianWaiter w, int tableNumber){
			 choice = c;
			 waiter = w;
			 this.tableNumber = tableNumber;
		  }
		  public void setTimer(int time){
			  orderTime = time;
			  state =  OrderState.cooking;
			  //Timer is a cooking timer.
			  timer = new Timer(time, new ActionListener() {
				   public void actionPerformed(ActionEvent e){
				      state = OrderState.cooked;
				      
				      timer.stop();
				      stateChanged();
				   }
				});
			  timer.start();
		  }
		  public OrderState getState(){
			  return state;
		  }
		  public void setState(OrderState state){
			  this.state = state;
		  }
		  public String getChoice(){
			  return choice;
		  }
		  
	}

	@Override
	protected void enterBuilding() {
		
		brianRest.gui.CookGui wg = new brianRest.gui.CookGui(this);
		BrianAnimationPanel bap = (BrianAnimationPanel)myPerson.building.getPanel();
		gui = wg;
		bap.addGui(wg);
		
		B_BrianRestaurant br = (B_BrianRestaurant)myPerson.getBuilding();
		orderstand  = br.getOrderStand();
		
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}



}



