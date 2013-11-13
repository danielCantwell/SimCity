/**
 * 
 */
package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.interfaces.Market;
import agent.Agent;

/**
 * @author Daniel
 *
 */
public class MarketAgent extends Agent implements Market {
	
	List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	
	CookAgent cook;
	
	private String name;
	Timer timer = new Timer();
	
	//					type, preptime, inventory, low, cap, price
	Food steak		= new Food("Steak",   9000, 8, 2, 8, 8);
	Food chicken	= new Food("Chicken", 8000, 2, 2, 8, 6);
	Food pizza		= new Food("Pizza",   7000, 6, 2, 6, 3);
	Food salad		= new Food("Salad",   5000, 10, 2, 6, 2);
	
	enum OrderState {Pending, Preparing, Done};
	
	public Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());

	public MarketAgent(String name) {
		this.name = name;
		
		foods.put("Steak",   steak);
		foods.put("Chicken", chicken);
		foods.put("Pizza",   pizza);
		foods.put("Salad",   salad);
	}
	
	// Messages

	// From Cook
	public void msgOrderFood(CookAgent cook, Map<String, Integer> lowFood) {
		print("MESSAGE # : Cook -> Market : OrderFood");
		for (String food : lowFood.keySet()) {
			orders.add(new Order(cook, food, lowFood.get(food)));
			print("Order Added : " + lowFood.get(food) + " " + food);
		}
		this.cook = cook;
		stateChanged();
	}
	// From Market's Self
	public void msgOrderReady(Order order) {
		print("order ready");
		order.state = OrderState.Done;
		stateChanged();
	}
	// From Restaurant Cashier
	public void msgPayment(double payment) {
		print("Cashier payed the market $" + payment);
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		// If there is an order that is done, give it to the cook
		synchronized (orders) {
			for (Order order : orders) {
				if (order.state == OrderState.Done) {
					giveOrderToCook(order);
					return true;
				}
			}
		}
		
		// If there is an order that needs to be prepared, prepare it
		synchronized (orders) {
			for (Order order : orders) {
				if (order.state == OrderState.Pending) {
					tryToPrepareOrder(order);
					return true;
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void tryToPrepareOrder(final Order order) {
		
		print ("Trying To Prepare : " + order.amount + " " + order.food + "s");
		
		int totalPrepTime = 0;
		int foodPrepTime = foods.get(order.food).prepTimer;
		int inventory = foods.get(order.food).inventory;
		int numWanted = order.amount;
		int numToPrepare = 0;
		
		boolean tooLow = false;
		
		if (inventory >= order.amount) {
			numToPrepare = order.amount;
		} else {
			numToPrepare = inventory;
			tooLow = true;
		}
		
		if (tooLow) {			
			cook.msgOrderNotFulfilled(this, order.food, numWanted - numToPrepare);
		}
		
		order.amountPrepared = numToPrepare;
		foods.get(order.food).inventory -= order.amountPrepared;
		
		totalPrepTime = (numToPrepare * foodPrepTime);
		
		order.state = OrderState.Preparing;
		timer.schedule(new TimerTask() {
			public void run() {
				msgOrderReady(order);
			}
		},
		totalPrepTime);
	}
	
	private void giveOrderToCook(Order order) {
		cook.msgOrderFinished(this, order.food, order.amountPrepared);
		cook.getHost().getCashier().
			msgPayMarket(this, foods.get(order.food).price * order.amountPrepared);
		orders.remove(order);
	}
	
	
	
	public String getName() {
		return name;
	}
	
	public class Order {
		CookAgent cook;
		String food;
		int amount;
		OrderState state = OrderState.Pending;

		int amountPrepared;
		
		Order(CookAgent cook, String food, int amount) {
			this.cook = cook;
			this.food = food;
			this.amount = amount;
		}
	}
	
	public class Food {
		final String type;
		final int prepTimer;
		final int low;
		final int capacity;
		int inventory;
		float price;
		
		Food(String type, int prepTimer, int inventory, int low, int capacity, float price) {
			this.type		= type;
			this.prepTimer	= prepTimer;
			this.inventory	= inventory;
			this.low		= low;
			this.capacity	= capacity;
			this.price		= price;
		}
		
		public boolean isLow() {
			return (inventory == low);
		}
		
	}
}

