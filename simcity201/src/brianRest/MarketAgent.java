package restaurant;

//import javax.swing.*;

import agent.Agent;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;


import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;


public class MarketAgent extends Agent implements Market {
	
	String name;
	JesseCashier cashier;
	
	private double money = 0;
	private double marketCharge = 1;
	
	MarketAgent me;
	
	//Hashmap of all food types and quantity available.
	HashMap<String, Integer> inventory = new HashMap<String, Integer>();
	List<MarketOrder> marketOrders = new ArrayList<MarketOrder>();
	
	public enum OrderState { pending, cooking, cooked, notified;}

	//Constructor
	public MarketAgent(String name, JesseCashier ca){
		me = this;
	  this.name = name;
	  cashier = ca;
	  
	  //Tree map
	  inventory.put("Steak", 1);
	  inventory.put("Chicken", 1);
	  inventory.put("Salad", 1);
	  inventory.put("Pizza", 1);
	  
	}
		
//########## Messages  ###############
	@Override
	public void msgINeedFood(String choice, int amount, Cook c){
		Do("Filling " + choice + " wanting "+ amount + " in 15 seconds.");
		MarketOrder mo = new MarketOrder(choice, amount, c);
		marketOrders.add(mo);
		stateChanged();
	}
	
	public void msgPayMarket(double money){
		this.money = money;
	}
	
//##########  Scheduler  ##############
@Override
	protected boolean pickAndExecuteAnAction() {
		try{
			
			if (marketOrders.size() > 0){
				fillOrder();
				return true;
			}
			
		}
		catch(ConcurrentModificationException e){
				return false;
		}
		return false;
	}
		
//########## Actions ###############
	public void fillOrder(){
			Do("Market Filled " + marketOrders.get(0).choice + " with "+ marketOrders.get(0).amount);
			MarketOrder mo = marketOrders.remove(0);
			if(inventory.get(mo.choice)>0){
				if (mo.amount > inventory.get(mo.choice)){
					mo.cook.msgFillOrder(mo.choice, inventory.get(mo.choice), false);
					cashier.msgHereIsMarketCost(inventory.get(mo.choice) * marketCharge, me);
					inventory.put(mo.choice, 0);
				}
				else{
					mo.cook.msgFillOrder(mo.choice, mo.amount, true);
					cashier.msgHereIsMarketCost(mo.amount * marketCharge, me);
					inventory.put(mo.choice, inventory.get(mo.choice) - mo.amount);
				}
			}
		
	}
	
//################    Utility     ##################
	public String toString(){
		return "Cook " + name;
	}

//######################## End of Class Cook#############################
	
	//#### Inner Class ####	
	private class MarketOrder {
		  Cook cook;
		  String choice;
		  int amount;
		  
		  public MarketOrder(String ch, int amt, Cook c){
			  cook = c;
			  choice = ch;
			  amount = amt;
		  }
	}
}



