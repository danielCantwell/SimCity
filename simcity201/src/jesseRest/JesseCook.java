package jesseRest;

import SimCity.Base.Role;
import SimCity.Buildings.B_JesseRestaurant;
import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import jesseRest.*;
import jesseRest.JesseOrderStand.Orders;
import jesseRest.gui.AnimationPanel;

/**
 * Restaurant Cook Agent
 */

public class JesseCook extends Role {
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public enum CookState {Pending, Cooking, Done};
	public enum InventoryState {Ordering, Done, Reorder};
	public InventoryState status; 
	public String currentOrder;
	private Semaphore cookingFood = new Semaphore(0,true);
	private Map<String, Food> foods = new HashMap<String, Food>();
//	private List<JesseMarket> markets = Collections.synchronizedList(new ArrayList<JesseMarket>());
	private int currentMarket = 0;
	private String name;
	private Timer timer = new Timer();
	private AnimationPanel animationPanel;
	JesseOrderStand orderstand;
	// When is the inventory quantity considered low?
	private final int LOW = 3;
	
	// How many foods does the cook order from a Market at once?
	private final int ORDERSIZE = 10;
	private int amountBought = 0;
	
	public JesseCook(String name) {
		super();
		this.name = name;
		status = InventoryState.Done;
		// Default values for food inventory. Change if you want.
		foods.put("Steak", new Food("Steak", 700, 2));
		foods.put("Chicken", new Food("Chicken", 600, 2));
		foods.put("Salad", new Food("Salad", 300, 2));
		foods.put("Pizza", new Food("Pizza", 500, 2));
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
//	public void addMarket(JesseMarket m) {
//		markets.add(m);
//	}
	
	public void setAnimationPanel(AnimationPanel a) {
		animationPanel = a;
	}

	/**
	 * MESSAGES =====================================================
	 */

	public void msgHereIsAnOrder(JesseAbstractWaiter w, String choice, int table) {
		// Cook receives order from a Waiter and stores it in a list
		orders.add(new Order(w, choice, table));
		stateChanged();
	}
	
	public void msgFoodDone(Order o) {
		o.state = CookState.Done;
		stateChanged();
	}
	
	public void msgFoodPickedUp(String choice) {
		animationPanel.counterItems.remove(choice.substring(0,2));
	}
	
	public void msgDeliverFood(int quantity, String choice) {
		foods.get(choice).inventory += quantity;
		amountBought += quantity;
		// Inventory is fully replenished.
		if (amountBought >= ORDERSIZE) {
			print("Message: Cook Inventory has been fully replenished. Total quantity - " + foods.get(choice).inventory);
			status = InventoryState.Done;
			currentMarket = 0;
			amountBought = 0;
		} else {
			// Inventory is partially replenished.
			if (quantity > 0) {
				print("Message: Market number " + (currentMarket+1) + " does not have enough food. Inventory partially replenished.");
			}
			// Inventory wasn't fully replenished because all markets are out.
//			if (currentMarket == markets.size() - 1) {
//				print("Message: All markets contacted for " + choice + ". No inventory left.");
//				status = InventoryState.Done;
//				currentMarket = 0;
//				amountBought = 0;
//			} else {
//				status = InventoryState.Reorder;
//				currentOrder = choice;
//			}
		}
		
		stateChanged();
	}

	/**
	 * SCHEDULER ====================================================
	 */
	
	public boolean pickAndExecuteAnAction() {
		synchronized(orders){
			for (Order o : orders) {
				if (o.state == CookState.Done){
					PlateFood(o);
					return true;
				}
			}
		}
		synchronized(orders){
			for (Order o : orders) {
				if (o.state == CookState.Pending) {
					CookIt(o);
					return true;
				}
			}
		}
		if(orderstand.getSize()>0) {
			putOnStand(orderstand.popFirstOrder());
			return true;
		}
//		if (status == InventoryState.Reorder) {
//			OrderFood(currentOrder);
//		}
		return false;
	}
	
	/**
	 * ACTIONS  ====================================================
	 */

	public void putOnStand(Orders o) {
		System.out.println("Cook: Gets order off of JesseOrderStand");
		Order order = new Order(o.jw, o.choice, o.tableNumber);
		orders.add(order);
		stateChanged();
	}
//	private void OrderFood(String choice) {
//		currentMarket++;
//		if (currentMarket < markets.size()) {	
//			markets.get(currentMarket).msgINeedFood(choice, ORDERSIZE - amountBought);
//			status = InventoryState.Ordering;
//		}
//	}
	private void CookIt(Order o) {
		// If you have inventory, remove one - else say you're out of food
		if (foods.get(o.choice).inventory > 0) {
			foods.get(o.choice).inventory--;
		}
		else {
			// Tell waiter that customer must reorder
			print("Message: Sending OutOfFood from Cook to Waiter");
			o.waiter.msgOutOfFood(o.choice, o.table);
			orders.remove(o);
			return;
		}
		
		System.out.print("INVENTORY FOR " + foods.get(o.choice).item + ": " + foods.get(o.choice).inventory + '\n');
		
		// Checks when inventory is low
//		if (foods.get(o.choice).inventory <= LOW && status == InventoryState.Done) {
//			System.out.println("Message: Sending INeedFood from Cook to Market number " + (currentMarket+1));
//			// Orders food from market
//			markets.get(0).msgINeedFood(o.choice, ORDERSIZE);
//			status = InventoryState.Ordering;
//		}
		
		o.state = CookState.Cooking;
		animationPanel.grillItems.add(o.choice.substring(0,2));
		// Food order is cooked by a timer
		timer.schedule(new TimerTask() {
			public void run() {
				cookingFood.release();
			}
		},
		foods.get(o.choice).cookingTime);
		
		try {
			cookingFood.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		msgFoodDone(o);
		print("Finished cooking: " + o.choice);
	}
	
	private void PlateFood(Order o) {
		// Waiter is notified of cooked order once Timer runs out
		print("Message 8: Sending OrderIsReady from Cook to Waiter");
		o.waiter.msgOrderIsReady(o.choice, o.table);
		animationPanel.grillItems.remove(o.choice.substring(0,2));
		animationPanel.counterItems.add(o.choice.substring(0,2));
		orders.remove(o);
	}
	
	/**
	 * UTILITIES  ===================================================
	 */
	
	// Order Class - represents a single customer's order
	public class Order {
		JesseAbstractWaiter waiter;
		String choice;
		int table;
		CookState state = CookState.Pending;
		
		public Order(JesseAbstractWaiter w, String c, int t) {
			waiter = w;
			choice = c;
			table = t;
		}
	}
	
	// Food Class - holds how much time it takes to cook
	private class Food {
		String item;
		int cookingTime;
		int inventory;
		
		public Food(String name, int time, int quantity) {
			item = name;
			cookingTime = time;
			inventory = quantity;
		}
	}

	public void print(String string) {
		System.out.println(string);
	}
	
	public void setOrderStand(JesseOrderStand os) {
		orderstand = os;
	}
	
	protected void enterBuilding() {
//		B_JesseRestaurant jr = (B_JesseRestaurant)myPerson.getBuilding();
//		orderstand = jr.getOrderStand();
	}

	@Override
	public void workOver() {
		myPerson.getMoney().add(50, 0);
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "JCok";
	}

	public void msgStateChanged() {
		stateChanged();
	}
}

