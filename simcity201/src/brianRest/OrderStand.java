package brianRest;

import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import agent.Agent;


import brianRest.gui.BrianAnimationPanel;
import brianRest.gui.BrianRestaurantPanel;
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
	B_BrianRestaurant br;
	BrianAnimationPanel bap;
	BrianCookRole cook;
	
	public OrderStand(B_BrianRestaurant br, BrianCookRole cr){
		this.br = br;
		BrianRestaurantPanel brp  = (BrianRestaurantPanel)br.getPanel();
		bap = (BrianAnimationPanel)brp.bap;
		cook = cr;
	}
	
	private String ordersToString(){
		String s = "";
		for (Orders o: orders){
			s += " \n " + o.choice;
		}
		return s;
	}
	
	public void addOrder(String choice, BrianWaiter bw, int tableNumber){
		orders.add(new Orders(choice, bw, tableNumber));
		bap.orderStandString = ordersToString();
		cook.msgStateChanged();
	}
	
	public Orders popFirstOrder(){
		synchronized(orders){
			if (orders.size() > 0){
				Orders popme = orders.remove(0);
				bap.orderStandString = ordersToString();
				return popme;
			}
		}
		return null;
	}
	
	public int getSize(){
		return orders.size();
	}

	public class Orders{
		String choice;
		BrianWaiter bwr;
		int tableNumber;
		
		public Orders(String c, BrianWaiter bw, int i){
			choice = c;
			bwr = bw;
			tableNumber = i;
		}
		public String getChoice(){return choice;}
		public int getTableNumber(){return tableNumber;}
		public BrianWaiter getWaiter() { return bwr;}
		
	
	}
}