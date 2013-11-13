package restaurant;

import agent.Agent;

import java.util.*;

import restaurant.gui.CookGui;
import restaurant.interfaces.Market;

/**
 * Restaurant Cook Agent
 */
// We only have 2 types of agents in this prototype. A customer and an agent
// that
// does all the rest. Rather than calling the other agent a waiter, we called
// him
// the HostAgent. A Host is the manager of a restaurant who sees that all
// is proceeded as he wishes.
public class CookAgent extends Agent {

	private CookGui cookGui = new CookGui();

	List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	List<MyMarket> markets = Collections
			.synchronizedList(new ArrayList<MyMarket>());
	List<FoodNeeded> foodToOrder = Collections
			.synchronizedList(new ArrayList<FoodNeeded>());

	private String name;
	
	private HostAgent host;
	
	Timer timer = new Timer();

	// type. timer. inventory. low. cap
	private Food steak = new Food("Steak", 8000, 6, 2, 8);
	private Food chicken = new Food("Chicken", 7000, 8, 2, 8);
	private Food pizza = new Food("Pizza", 5000, 5, 2, 6);
	private Food salad = new Food("Salad", 3000, 4, 2, 6);
	public Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());

	enum MarketState {
		Available, OutOfStock
	};

	enum FoodNeededState {
		NeedToOrder, Ordered, CantBeOrdered
	};

	enum State {
		Pending, OutOfStock, Cooking, Done, Plated, PickedUp
	};

	public CookAgent(String name, HostAgent host) {
		this.name = name;
		this.host = host;

		foods.put("Steak", steak);
		foods.put("Chicken", chicken);
		foods.put("Pizza", pizza);
		foods.put("Salad", salad);
	}

	// Messages

	// From Waiter
	public void msgHereIsAnOrder(WaiterAgent waiter, String choice, int table) {
		print("MESSAGE 7 : Waiter -> Cook : HereIsAnOrder");
		orders.add(new Order(waiter, choice, table));
		stateChanged();
	}

	// From Cook's Self
	public void msgFoodDone(Order order) {
		print("food done");
		order.state = State.Done;
		stateChanged();
	}

	// From Waiter
	public void msgGotFood(String choice, int table) {
		synchronized (orders) {
			for (Order order : orders) {
				if (order.choice == choice && order.table == table) {
					order.state = State.PickedUp;
					stateChanged();
				}
			}
		}
	}

	// From Market
	public void msgOrderNotFulfilled(Market market,
			String unfulfilledFood, int amount) {
		print("MESSAGE # : Market -> Cook : OrderNotFulfilled");
		foodToOrder.add(new FoodNeeded(unfulfilledFood, amount));
		synchronized (markets) {
			for (MyMarket myMarket : markets) {
				if (myMarket.market == market) {
					myMarket.outOf(unfulfilledFood);
				}
			}
		}
		stateChanged();
	}

	// From Market
	public void msgOrderFinished(Market market, String fulfilledFood,
			int amount) {
		print("MESSAGE # : Market -> Cook : OrderFinished");
		synchronized (foodToOrder) {
			for (FoodNeeded item : foodToOrder) {
				if (item.type.equals(fulfilledFood)) {
					foodToOrder.remove(item);
					break;
				}
			}
		}
		foods.get(fulfilledFood).inventory += amount;
		stateChanged();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {

		// If there is an order that is done, plate it
		synchronized (orders) {
			for (Order order : orders) {
				if (order.state == State.Done) {
					plateFood(order);
					return true;
				}
			}
		}
		// If there is an order that needs to be cooked, cook it
		synchronized (orders) {
			for (Order order : orders) {
				if (order.state == State.Pending) {
					tryToCookIt(order);
					return true;
				}
			}
		}
		//
		synchronized (orders) {
			for (Order order : orders) {
				if (order.state == State.PickedUp) {
					removeOrder(order);
					return true;
				}
			}
		}
		// If an order is out of stock, tell the waiter
		synchronized (orders) {
			for (Order order : orders) {
				if (order.state == State.OutOfStock) {
					notifyWaiterOutOfStock(order);
					return true;
				}
			}
		}
		synchronized (foodToOrder) {
			for (FoodNeeded item : foodToOrder) {
				if (item.state == FoodNeededState.NeedToOrder) {
					synchronized (markets) {
						for (MyMarket myMarket : markets) {
							if (myMarket.has(item.type)) {
								orderLowFood(myMarket);
								rotateMarkets();
								return true;
							}
						}
					}
					item.state = FoodNeededState.CantBeOrdered;
					print("All markets are out of " + item.type);
					return true;
				}
			}
		}
		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	// Actions

	private void tryToCookIt(final Order order) {
		print("Trying to Cook Order");

		Food food = foods.get(order.choice);

		if (food.inventory == 0) {
			order.state = State.OutOfStock;
			return;
		}
		
		DoCooking(order);

		food.inventory--;
		print(order.choice + " inventory = " + food.inventory);

		checkIfFoodIsLow();

		// Start cooking food
		order.state = State.Cooking;
		timer.schedule(new TimerTask() {
			public void run() {
				msgFoodDone(order);
			}
		}, food.cookingTimer);
	}

	private void plateFood(Order order) {
		DoPlating(order);
		order.waiter.msgOrderReady(order.choice, order.table);
		order.state = State.Plated;
	}

	private void removeOrder(Order order) {
		DoRemoveOrder(order.choice, order.table);
		orders.remove(order);
	}

	private void notifyWaiterOutOfStock(Order order) {
		Do(order.choice + " is out of stock");
		order.waiter.msgOutOf(order.choice, order.table);
		orders.remove(order);
	}

	private void orderLowFood(MyMarket myMarket) {
		Do("Order Low Food");

		Map<String, Integer> lowFoods = new HashMap<String, Integer>();
		synchronized (foodToOrder) {
			for (FoodNeeded item : foodToOrder) {
				lowFoods.put(item.type, item.amount);
				item.state = FoodNeededState.Ordered;
			}
		}
		myMarket.market.msgOrderFood(this, lowFoods);
	}

	// The animation DoXYZ() routines

	private void DoCooking(Order order) {
		cookGui.DoCookFood(order.choice, order.table);
	}

	private void DoPlating(Order order) {
		cookGui.DoPlateFood(order.choice, order.table);
	}

	private void DoRemoveOrder(String type, int table) {
		cookGui.DoRemoveFood(type, table);
	}

	public String getName() {
		return name;
	}

	private void checkIfFoodIsLow() {
		if (foods.get("Steak").isLow() && !foodToOrder.contains("Steak")) {
			foodToOrder.add(new FoodNeeded("Steak", foods.get("Steak").capacity
					- foods.get("Steak").low));
		}
		if (foods.get("Chicken").isLow() && !foodToOrder.contains("Chicken")) {
			foodToOrder.add(new FoodNeeded("Chicken",
					foods.get("Chicken").capacity - foods.get("Chicken").low));
		}
		if (foods.get("Pizza").isLow() && !foodToOrder.contains("Pizza")) {
			foodToOrder.add(new FoodNeeded("Pizza", foods.get("Pizza").capacity
					- foods.get("Pizza").low));
		}
		if (foods.get("Salad").isLow() && !foodToOrder.contains("Salad")) {
			foodToOrder.add(new FoodNeeded("Salad", foods.get("Salad").capacity
					- foods.get("Salad").low));
		}
	}

	private void rotateMarkets() {
		MyMarket first = markets.get(0);
		markets.remove(0);
		markets.add(first);
	}

	public void addMarket(Market market) {
		markets.add(new MyMarket(market));
		((Agent) market).startThread();
	}

	public CookGui getGui() {
		return cookGui;
	}
	
	public HostAgent getHost() {
		return host;
	}

	public class Order {

		WaiterAgent waiter;
		String choice;
		int table;
		State state = State.Pending;

		Order(WaiterAgent waiter, String choice, int table) {
			this.waiter = waiter;
			this.choice = choice;
			this.table = table;
		}
	}

	public class Food {
		public final String type;
		public final int cookingTimer;
		public int inventory;
		public final int low;
		public final int capacity;

		Food(String type, int cookingTimer, int inventory, int low, int capacity) {
			this.type = type;
			this.cookingTimer = cookingTimer;
			this.inventory = inventory;
			this.low = low;
			this.capacity = capacity;
		}

		public boolean isLow() {
			return (inventory == low);
		}

	}

	public class FoodNeeded {
		String type;
		int amount;
		FoodNeededState state;

		FoodNeeded(String type, int amount) {
			this.type = type;
			this.amount = amount;
			state = FoodNeededState.NeedToOrder;
		}
	}

	public class MyMarket {
		Market market;
		MarketState state;

		boolean outOfSteak = false;
		boolean outOfChicken = false;
		boolean outOfPizza = false;
		boolean outOfSalad = false;

		MyMarket(Market market) {
			this.market = market;
			state = MarketState.Available;
		}

		public void outOf(String type) {
			if (type == "Steak") {
				outOfSteak = true;
			} else if (type == "Chicken") {
				outOfChicken = true;
			} else if (type == "Pizza") {
				outOfPizza = true;
			} else if (type == "Salad") {
				outOfSalad = true;
			} else {
				print("outOf -- not a valid type");
			}
		}

		public boolean has(String type) {
			if (type == "Steak") {
				if (outOfSteak) {
					return false;
				} else {
					return true;
				}
			} else if (type == "Chicken") {
				if (outOfChicken) {
					return false;
				} else {
					return true;
				}
			} else if (type == "Pizza") {
				if (outOfPizza) {
					return false;
				} else {
					return true;
				}
			} else if (type == "Salad") {
				if (outOfSalad) {
					return false;
				} else {
					return true;
				}
			} else {
				print("has -- not a valid type");
				return false;
			}
		}
	}
}
