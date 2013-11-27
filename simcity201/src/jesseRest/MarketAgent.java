package jesseRest;

import agent.Agent;
import agent.Check;
import agent.Menu;

import java.util.*;
import java.util.concurrent.Semaphore;

import jesseRest.interfaces.Market;

/**
 * Restaurant Market Agent
 */

public class MarketAgent extends Agent implements Market {
	public enum DeliveryState {Pending, Created};
	public Map<String, Integer> inventory = new HashMap<String, Integer>();
	private Map<String, Integer> prices = new HashMap<String, Integer>();
	private Semaphore deliveringFood = new Semaphore(0, true);
	private Timer deliverTimer = new Timer();
	private CookAgent mycook;
	private CashierAgent mycashier;
	private List<Delivery> deliveries = Collections.synchronizedList(new ArrayList<Delivery>());
	private String name;
	private double money = 0;
	
	public MarketAgent(String name) {
		super();
		this.name = name;
		
		// Inventory has finite amounts. They don't replenish.
		inventory.put("Steak", new Integer(7));
		inventory.put("Chicken", new Integer(7));
		inventory.put("Pizza", new Integer(7));
		inventory.put("Salad", new Integer(7));
		
		// Prices of each item from the Market - significantly lower than Menu price
		prices.put("Steak", new Integer(4));
		prices.put("Chicken", new Integer(3));
		prices.put("Pizza", new Integer(2));
		prices.put("Salad", new Integer(1));
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setCook(CookAgent c) {
		mycook = c;
	}
	
	public void setCashier(CashierAgent c) {
		mycashier = c;
	}

	/**
	 * MESSAGES =====================================================
	 */

	public void msgINeedFood(String choice, int amount) {
		print(name + " has recieved an order from the Cook. Market quantity: " + inventory.get(choice) + ". Delivery amount: " + amount);
		Delivery delivery = new Delivery(choice, amount);
		deliveries.add(delivery);
		stateChanged();
	}
	
	public void msgHereIsPayment(int amountOwed) {
		money += amountOwed;
	}

	/**
	 * SCHEDULER ====================================================
	 */
	
	public boolean pickAndExecuteAnAction() {
		synchronized(deliveries){
			for (Delivery d : deliveries) {
				if (d.s == DeliveryState.Created) {
					ProcessDelivery(d);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * ACTIONS  ====================================================
	 */

	private void ProcessDelivery(Delivery d) {
		if (inventory.get(d.choice).intValue() == 0) {
			print("Market has absolutely run out of food for choice: " + d.choice);
			deliveries.remove(d);
			return;
		}
		
		if (d.quantity > inventory.get(d.choice).intValue()) {
			d.quantity = inventory.get(d.choice).intValue();
			print("There is not enough food in this market to complete an order.");
		} else {
			System.out.println("Market has sufficient quantity for order.");
		}
		
		d.s = DeliveryState.Pending;
		inventory.put(d.choice, inventory.get(d.choice) - d.quantity);
		
		deliverTimer.schedule(new TimerTask() {
			public void run() {
				deliveringFood.release();
			}
		},
		750 * d.quantity);
		
		try {
			deliveringFood.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Message: Sending DeliverFood from Market to Cook");
		mycook.msgDeliverFood(d.quantity, d.choice);
		
		// Calculate total cost of order
		int orderPrice = prices.get(d.choice) * d.quantity;
		System.out.println("Message: Sending PayForItems from Market to Cashier");
		mycashier.msgPayForItems(this, orderPrice);
	}

	/**
	 * UTILITIES  ===================================================
	 */
	
	// Delivery Class - represents a delivery from the Market to the Cook
	private class Delivery {
		String choice;
		int quantity;
		DeliveryState s;
		
		public Delivery(String c, int q) {
			choice = c;
			quantity = q;
			s = DeliveryState.Created;
		}
	}
}

