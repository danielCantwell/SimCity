package jesseRest;

import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_EricRestaurant;
import SimCity.Buildings.B_JesseRestaurant;
import agent.Agent;
import jesseRest.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.Timer;

public class JesseOrderStand{
	
	private List<Orders> orders = Collections.synchronizedList(new ArrayList<Orders>());
	B_JesseRestaurant jr;
	JesseCook cook;
	
	public JesseOrderStand(B_JesseRestaurant b, JesseCook c){
		this.jr = b;
		cook = c;
	}
	
	
	public void addOrder(String choice, JessePCWaiter w, int tableNumber){
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
		JessePCWaiter jw;
		int tableNumber;
		
		public Orders(String c, JessePCWaiter w, int i){
			choice = c;
			jw = w;
			tableNumber = i;
		}
		public String getChoice(){return choice;}
		public int getTableNumber(){return tableNumber;}
		public JessePCWaiter getWaiter() { return jw;}
		
	
	}
}