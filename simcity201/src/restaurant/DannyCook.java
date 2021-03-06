package restaurant;

import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.God.BuildingType;
import SimCity.Base.Role;
import SimCity.Buildings.B_DannyRestaurant;
import SimCity.trace.AlertTag;
import SimCity.Buildings.B_Market;

import java.util.*;

import market.MarketManagerRole;
import market.interfaces.MarketDeliveryCook;
import restaurant.gui.CookGui;
import restaurant.gui.DannyRestaurantAnimationPanel;
import restaurant.interfaces.Waiter;

/**
 * Restaurant Cook Agent
 */
// We only have 2 types of agents in this prototype. A customer and an agent
// that
// does all the rest. Rather than calling the other agent a waiter, we called
// him
// the HostAgent. A Host is the manager of a restaurant who sees that all
// is proceeded as he wishes.
public class DannyCook extends Role implements MarketDeliveryCook {

	private CookGui cookGui = new CookGui();

	List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	List<FoodNeeded> foodToOrder = Collections
			.synchronizedList(new ArrayList<FoodNeeded>());

	private String name;

	Timer timer = new Timer();
	
	public boolean workOver = false;
	
	OrderStand orderstand;

	// type. timer. inventory. low. cap
	private Food steak = new Food("Steak", 8000, 3, 2, 8);
	private Food chicken = new Food("Chicken", 7000, 3, 2, 8);
	private Food pizza = new Food("Pizza", 5000, 3, 2, 6);
	private Food salad = new Food("Salad", 3000, 3, 2, 6);
	public Map<String, Food> foods = Collections
			.synchronizedMap(new HashMap<String, Food>());

	enum FoodNeededState {
		NeedToOrder, Ordered, CantBeOrdered
	};

	enum State {
		Pending, OutOfStock, Cooking, Done, Plated, PickedUp
	};

	public DannyCook() {
		foods.put("Steak", steak);
		foods.put("Chicken", chicken);
		foods.put("Pizza", pizza);
		foods.put("Salad", salad);
	}

	public void setName(String name) {
		this.name = name;
	}

	// Messages
	
	public void msgStateChanged() {
		stateChanged();
	}

	public void msgLeaveRestaurant() {
		workOver = true;
		stateChanged();
	}

	// From Waiter
	public void msgHereIsAnOrder(DannyWaiter waiter, String choice, int table) {
		print("MESSAGE 7 : Waiter -> Cook : HereIsAnOrder");
		orders.add(new Order(waiter, choice, table));
		System.out.println("COOK MSG PERSON = " + myPerson.hashCode());
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

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		if (workOver) {
			leaveRestaurant();
			return true;
		}
		
		synchronized (foodToOrder) {
			for (FoodNeeded f : foodToOrder) {
				if (f.state == FoodNeededState.NeedToOrder) {
					orderFood(f);
					return true;
				}
			}
		}

		// If there is an order that is done, plate it
		synchronized (orders) {
			for (Order order : orders) {
				if (order.state == State.Done) {
					plateFood(order);
					return true;
				}
			}
		}
		if (orderstand.getSize() > 0){
			makeCookOrder(orderstand.popFirstOrder());
			return true;
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
		
		

		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	// Actions
	
	private void leaveRestaurant() {
		System.out.println("Cook workOver");
		B_DannyRestaurant rest = (B_DannyRestaurant) myPerson.getBuilding();
		rest.cookFilled = false;
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson.building
				.getPanel();
		cookGui.setPresent(false);
		ap.cookPresent = false;
		ap.removeGui(cookGui);
		myPerson.msgGoHome();
		exitBuilding(myPerson);
		workOver = false;
	}
	
	private void makeCookOrder(restaurant.OrderStand.Orders o) {
		Do(AlertTag.DannyRest, "Getting order from order stand");
		
		Order order = new Order(o.getWaiter(), o.choice, o.getTableNumber());
		orders.add(order);
		stateChanged();
	}

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
		msgFoodDone(order);
		/*
		timer.schedule(new TimerTask() {
			public void run() {
				msgFoodDone(order);
			}
		}, 100);
		*/
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
		Do(AlertTag.DannyRest, order.choice + " is out of stock");
		((DannyWaiter) order.waiter).msgOutOf(order.choice, order.table);
		orders.remove(order);
	}
	
	private void orderFood(FoodNeeded f) {
		B_Market market = (B_Market) God.Get().findBuildingOfType(BuildingType.Market);
		//
		Building building = myPerson.getBuilding();
		int id = building.getID();
		MarketManagerRole manager = market.getManager();
		System.out.println("FOOD TYPE : " + f.type);
		System.out.println("FOOD AMOUNT : " + f.amount);
		manager.msgWantFood(id, f.type, f.amount);
		//market.getManager().msgWantFood(myPerson.getBuilding().getID(), f.type, f.amount);
		f.state = FoodNeededState.Ordered;
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

	public CookGui getGui() {
		return cookGui;
	}

	public class Order {

		Waiter waiter;
		String choice;
		int table;
		State state = State.Pending;

		Order(Waiter waiter, String choice, int table) {
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

	private void print(String string) {
		System.out.println(string);
	}

	@Override
	protected void enterBuilding() {
		System.out.println("Cook enterBuilding");
		CookGui cg = new CookGui();
		cookGui = cg;
		// add gui
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson.building
				.getPanel();
		ap.addGui(cookGui);
		ap.cookPresent = true;
		
		B_DannyRestaurant rest = (B_DannyRestaurant) myPerson.getBuilding();
		orderstand = rest.getOrderStand();
	}

	@Override
	public void workOver() {
		/*
		System.out.println("Cook workOver");
		B_DannyRestaurant rest = (B_DannyRestaurant) myPerson.getBuilding();
		rest.cookFilled = false;
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson.building
				.getPanel();
		cookGui.setPresent(false);
		ap.removeGui(cookGui);
		exitBuilding(myPerson);
		*/
		DannyRestaurantAnimationPanel ap = (DannyRestaurantAnimationPanel) myPerson.building
				.getPanel();
		ap.cookPresent = false;
		ap.removeGui(cookGui);
		myPerson.msgGoHome();
		exitBuilding(myPerson);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "DCok";
	}

	@Override
	public void msgHereIsYourFood(String food, int amount) {
		for (FoodNeeded f : foodToOrder) {
			if (f.type == food) {
				foodToOrder.remove(f);
			}
		}
		Do(AlertTag.DannyRest, "Received " + amount + food + "s from market");
		foods.get(food).inventory += amount;
		stateChanged();
	}
}