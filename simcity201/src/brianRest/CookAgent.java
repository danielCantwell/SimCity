package restaurant;

//import javax.swing.*;

import agent.Agent;

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

import restaurant.gui.CookGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Waiter;

public class CookAgent extends Agent implements Cook {
	
	private String name;
	CookGui gui;
	
	//A list of ALL orders that the cook is attending to.
	private List<Order> orders;
	
	//List of all the markets
	private List<MarketAgent> markets = new ArrayList<MarketAgent>();

	//A map containing all the foods and their cook times. Implement in Constructor pls!
	private Map<String, Food> foodDictionary = new HashMap<String, Food>();
	
	private String searchMarketsFor = "";
	
	private enum OrderState { pending, checkingAmount, cooking, cooked, notified;}
	
	private int max_Capacity = 4;
	
	private Semaphore atTargetLocation = new Semaphore(0, true);

	//Constructor
	public CookAgent(String name){
	  this.name = name;
	  orders = new ArrayList<Order>();
	  
	  //Tree map
	  foodDictionary.put("Steak", new Food("Steak", 5000, 1));
	  foodDictionary.put("Chicken", new Food("Chicken", 4500, 1));
	  foodDictionary.put("Salad", new Food("Salad", 6000, 1));
	  foodDictionary.put("Pizza", new Food("Pizza", 7000, 1));
	  
	}
		
//########## Messages  ###############
	@Override
	public void msgHeresAnOrder(String o, Waiter w, int tableNumber)
	{
		Order order = new Order(o, w, tableNumber);
		 orders.add(order);
		 stateChanged();
	}
	
	@Override
	public void msgFillOrder(String choice, int amount, boolean filled){
		Do("Refilling " + choice + " by " + amount+".");
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
				
				for (int i=0; i<orders.size();i++){
					
					if (orders.get(i).getState() == OrderState.cooked){
						tellWaiterOrderIsReady(orders.get(i), i);
						i--;
						return true;
					}
				}
				
				if (searchMarketsFor.trim().length() > 0){
					SearchMarkets(searchMarketsFor);
					return true;
				}
		}
	}
	catch(ConcurrentModificationException e){
			return false;
	}
		
		return false;
	}
		
//########## Actions ###############
	private void CookOrder(Order o){
		o.state = OrderState.checkingAmount;
		Food temp = foodDictionary.get(o.choice);
		if (temp.amount == 0){
			o.waiter.msgOutOfFood(o.choice, o.tableNumber);
			orders.remove(o);
			Do("Out of "+ o.choice);
			if (temp.orderFromIndex < markets.size())
			markets.get(temp.orderFromIndex).msgINeedFood(temp.choice, max_Capacity - temp.amount , this);
			return;
		}
		DoGoToGrill();
		gui.DoGoToGrills(o.choice);
		if (temp.amount == 1){
			//order more for the restaurant;
			Do("Last "+ o.choice+". Ordering more.");
			if (temp.orderFromIndex < markets.size())
			markets.get(temp.orderFromIndex).msgINeedFood(temp.choice, max_Capacity - temp.amount , this);
		}
		
		temp.amount --;
		
		  Do("is cooking " + o.choice + ".");
		  o.setTimer(foodDictionary.get(o.choice).cookTime);
	}
	
	//Search markets is only called when the cook needs to iterate to more markets because another market ran out of the item he needed.
	private void SearchMarkets(String choice){
		Do("Searching other markets for " + choice+ ".");
		searchMarketsFor = "";
		Food temp = foodDictionary.get(choice);
		if (temp.orderFromIndex == markets.size()) {
			Do("Stopped searching for "+ temp.choice+".");
			return; //If the cook searched all the markets, then forget about searching more.
		}
		markets.get(temp.orderFromIndex).msgINeedFood(temp.choice, max_Capacity - temp.amount, this); //Ask market for food.
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
	
	public void addMarket(MarketAgent ma){
		markets.add(ma);
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
		  Waiter waiter;
		  int tableNumber;
		  Timer timer;
		  int orderTime;
		  
		  private OrderState state = OrderState.pending;
		  
		  public Order(String c, Waiter w, int tableNumber){
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

}



