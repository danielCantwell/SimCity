/**
 * 
 */
package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.gui.DannyRestaurantAnimationPanel;
import restaurant.gui.DannyRestaurantPanel;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_DannyRestaurant;

/**
 * @author Daniel
 *
 */
public class OrderStand {
	
	private List<Orders> orders = Collections.synchronizedList(new ArrayList<Orders>());
	
	B_DannyRestaurant dannyRestaurant;
	DannyRestaurantAnimationPanel dannyAnimationPanel;
	DannyCook cook;
	
	public OrderStand(B_DannyRestaurant dr, DannyCook c){
		dannyRestaurant = dr;
		dannyAnimationPanel = (DannyRestaurantAnimationPanel) dannyRestaurant.getPanel();
		cook = c;
	}
	
	private String ordersToString(){
		String s = "";
		for (Orders o: orders){
			s += " \n " + o.choice;
		}
		return s;
	}
	
	public void addOrder(String choice, DannyAbstractWaiter dw, int tableNumber){
		orders.add(new Orders(choice, dw, tableNumber));
		//dannyAnimationPanel.orderStandString = ordersToString();
		cook.msgStateChanged();
	}
	
	public Orders popFirstOrder(){
		synchronized(orders){
			if (orders.size() > 0){
				Orders pop = orders.remove(0);
				//dannyAnimationPanel.orderStandString = ordersToString();
				return pop;
			}
		}
		return null;
	}
	
	public int getSize(){
		return orders.size();
	}

	public class Orders{
		String choice;
		DannyAbstractWaiter dannyWaiter;
		int tableNumber;
		
		public Orders(String c, DannyAbstractWaiter dw, int i){
			choice = c;
			dannyWaiter = dw;
			tableNumber = i;
		}
		public String getChoice(){return choice;}
		public int getTableNumber(){return tableNumber;}
		public DannyAbstractWaiter getWaiter() { return dannyWaiter;}
		
	
	}

}
