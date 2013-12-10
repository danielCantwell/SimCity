package EricRestaurant;

import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_EricRestaurant;
import agent.Agent;
import EricRestaurant.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

public class EricOrderStand{
	
	private List<Orders> orders = Collections.synchronizedList(new ArrayList<Orders>());
	B_EricRestaurant er;
	EricCook cook;
	
	public EricOrderStand(B_EricRestaurant b, EricCook c){
		this.er = b;
		cook = c;
	}
	
	
	public void addOrder(String choice, EricPCWaiter w, int tableNumber){
		orders.add(new Orders(choice, w, tableNumber));
		cook.msgStateChanged();
	}
	
	public Orders popFirstOrder(){
		synchronized(orders){
			if (orders.size() > 0){
				Orders popme = orders.remove(0);
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
		EricPCWaiter ew;
		int tableNumber;
		
		public Orders(String c, EricPCWaiter w, int i){
			choice = c;
			ew = w;
			tableNumber = i;
		}
		public String getChoice(){return choice;}
		public int getTableNumber(){return tableNumber;}
		public EricPCWaiter getWaiter() { return ew;}
		
	
	}
}