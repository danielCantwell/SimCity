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

public class OrderStand{
	
	private List<Orders> orders = Collections.synchronizedList(new ArrayList<Orders>());
	
	public void addOrder(String choice, BrianWaiterRole bwr, int tableNumber){
		orders.add(new Orders(choice, bwr, tableNumber));
	}
	
	public Orders popFirstOrder(){
		synchronized(orders){
			if (orders.size() > 0){
				return orders.remove(0);
			}
		}
		return null;
	}
	
	public int getSize(){
		return orders.size();
	}

	public class Orders{
		String choice;
		BrianWaiterRole bwr;
		int tableNumber;
		
		public Orders(String c, BrianWaiterRole b, int i){
			choice = c;
			bwr = b;
			tableNumber = i;
		}
		public String getChoice(){return choice;}
		public int getTableNumber(){return tableNumber;}
		public BrianWaiterRole getWaiter() { return bwr;}
		
	
	}
}