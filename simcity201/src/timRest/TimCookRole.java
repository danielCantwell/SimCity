package timRest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import market.MarketManagerRole;
import market.interfaces.MarketDeliveryCook;
import SimCity.Base.God;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import timRest.gui.TimCookGui;
import timRest.gui.TimHostGui;
import timRest.interfaces.TimWaiter;

public class TimCookRole extends Role implements MarketDeliveryCook {
	
	private final int NORMAL_AMOUNT_TO_ORDER = 8;
	
	private TimCookGui gui = new TimCookGui(this);
	
	private TimHostRole host;
	
	private Timer timer = new Timer();

	private List<Order> itemsToCook = Collections.synchronizedList(new ArrayList<Order>());
	private List<Food> foods = Collections.synchronizedList(new ArrayList<Food>());
	//private List<Delivery> deliveries = Collections.synchronizedList(new ArrayList<Delivery>());
	private List<MyMarket> markets = Collections.synchronizedList(new ArrayList<MyMarket>());
	
	private HashMap<String, Integer> cookingMap = new HashMap<String, Integer>();
	
	private boolean outOfFood;
	enum CookState { idle, cooking };
	private CookState state = CookState.idle;
	
	private boolean canLeave = false;
	
	enum OrderState { pending, cooking, justFinished, ready }
	
	public TimCookRole()
	{
		super();
	}

//	public void msgCannotFulfill(MarketAgent market, String choice)
//	{
//		// food out, try different market
//		synchronized(markets)
//		{
//			for (MyMarket m : markets)
//			{
//				if (market == m.marketRef)
//				{
//					// cannot order from that market
//					m.foodAvail.remove(choice);
//					// food not on order anymore
//					print("Cannot order " + choice + " from " + market.name + " anymore.");
//					synchronized(foods)
//					{
//						for (Food f : foods)
//						{
//							// force cook to order again
//							if (f.name.equals(choice))
//							{
//								f.state = FoodState.inStock;
//								break;
//							}
//						}
//					}
//					stateChanged();
//					return;
//				}
//			}
//		}
//	}
//
//	public void msgCanFulfillPartial(MarketAgent market, String choice, int amount)
//	{
//		synchronized(markets)
//		{
//			for (MyMarket m : markets)
//			{
//				if (market == m.marketRef)
//				{
//					// cannot order from that market
//					m.foodAvail.remove(choice);
//					// food not on order anymore
//					print("Cannot order " + choice + " from " + market.name + " anymore.");
//					synchronized(foods)
//					{
//						for (Food f : foods)
//						{
//							// force cook to order again
//							if (f.name.equals(choice))
//							{
//								f.state = FoodState.inStock;
//								f.amountToOrder -= amount;
//								break;
//							}
//						}
//					}
//					stateChanged();
//					return;
//				}
//			}
//		}
//	}
//	

    @Override
    public void msgHereIsYourFood(String food, int amount)
    {
        synchronized(foods)
        {
            for (Food f : foods)
            {
                if (f.name.equals(food))
                {
                    f.state = FoodState.inStock;
                    f.inventory += amount;
                    outOfFood = false;
                    Do(AlertTag.BrianRest, "Recieved delivery! Now I have " + f.inventory + " " + f.name + " in stock.");
                    stateChanged();
                    return;
                }
            }
        }
        /*synchronized(deliveries)
        {
            deliveries.add(new Delivery(manager, price));
        }*/
    }
	
//	public void msgPartialOrderDelivered(Market market, String choice, int amount)
//	{
//		synchronized(foods)
//		{
//			for (Food f : foods)
//			{
//				if (f.name.equals(choice))
//				{
//					f.state = FoodState.inStock;
//					f.inventory += amount;
//				}
//			}
//		}
//		// fill rest of order from other market
//		synchronized(markets)
//		{
//			for (MyMarket m : markets)
//			{
//				if (market == m.marketRef)
//				{
//					// cannot order from that market
//					m.foodAvail.remove(choice);
//					// food not on order anymore
//					synchronized(foods)
//					{
//						for (Food f : foods)
//						{
//							if (f.name.equals(choice))
//							{
//								f.state = FoodState.inStock;
//							}
//						}
//					}
//				}
//			}
//		}
//		outOfFood = false;
//		stateChanged();
//	}
	
	public void msgHereIsAnOrder(TimWaiter waiter, String choice, int tableNumber)
	{
		// if food is out
		if (outOfFood)
		{
			// out of place msg
			waiter.msgOutOfFood(tableNumber);
			return;
		}
		// get food object
		Food food = null;
		synchronized(foods)
		{
			for (Food f : foods)
			{
				if (f.name.equals(choice))
				{
					food = f;
					break;
				}
			}
		}
		if (food == null)
		{
			// food not found
			return;
		}
		if (food.inventory > 0)
		{
			Order order = new Order(waiter, choice, tableNumber);
			itemsToCook.add(order);
			food.inventory--;
			print("Recieved Order.");
			//debug
			print(food.inventory + " " + food.name + "(s) left.");
		}
		else
		{
			// out of place msg
			// notify waiter
			waiter.msgCannotCookItem(tableNumber);
		}
		stateChanged();
	}
	
	public void msgPickedUpOrder(int tableNumber)
	{
		synchronized(itemsToCook)
		{
			for (Order order : itemsToCook)
			{
				if (order.tableNumber == tableNumber)
				{
					gui.removeServingArea(order.tableNumber);
					itemsToCook.remove(order);
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgLeaveWork()
	{
	    canLeave = true;
	}
	
	@Override
	protected boolean pickAndExecuteAnAction()
	{
	    if (canLeave)
	    {
	        leaveBuilding();
	        return false;
	    }
        /*if (!deliveries.isEmpty())
        {
            askCashierToPay(deliveries.get(0));
            return true;
        }*/
		if (state == CookState.idle)
		{
			synchronized(itemsToCook)
			{
				for (Order order : itemsToCook)
				{
					if (order.state == OrderState.justFinished)
					{
						Do(AlertTag.TimRest, "Order up! Table " + order.tableNumber + "!");
						orderDone(order);
						return true;
					}
					else if (order.state ==  OrderState.pending)
					{
						Do(AlertTag.TimRest, "Cooking " + order.choice + ".");
						state = CookState.cooking;
						cook(order);
						return true;
					}
				}
			}
		}
		synchronized(foods)
		{
			for (Food f : foods)
			{
				if (f.inventory <= 3)
				{
					// out of item
					// notify waiter
					// order more if haven't already
					if (f.state == FoodState.inStock)
					{
						// order more;
						orderMoreFood(f, f.amountToOrder);
						// if amount was changed, reset to initial value
						f.amountToOrder = NORMAL_AMOUNT_TO_ORDER;
						return true;
					}
				}
			}
			// find out if cook has no food
			for (Food f : foods)
			{
				if (f.inventory > 0)
				{
					return true;
				}
			}
		}
		// all out of food
		outOfFood = true;
		return true;
	}

	// actions
	private void cook(Order order)
	{
		gui.setGrill(order.choice);
		TimerTask t = new TimerTask() {
			Order order;
			public void run() {
				state = CookState.idle;
				order.state = OrderState.justFinished;
				//isHungry = false;
				gui.setGrill("");
				stateChanged();
			}
			public TimerTask init(Order o)
			{
				order = o;
				return this;
			}
		}.init(order);
		timer.schedule(t, cookingMap.get(order.choice));//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void orderDone(Order order)
	{
		gui.addServingArea(order.tableNumber, order.choice);
		order.state = OrderState.ready;
		order.waiter.msgOrderIsReady(order.tableNumber, order.choice);
	}
	
	/*private void askCashierToPay(Delivery delivery)
	{
        host.getCashier().msgPayMarket(delivery.manager, delivery.price);
        deliveries.remove(delivery);
	}*/
	
	private void orderMoreFood(Food food, int amount)
	{
		// sequentially chooses markets
		for (MyMarket market : markets)
		{
			// if market has food
			if (market.foodAvail.contains(food.name))
			{
				food.state = FoodState.onOrder;
				Do(AlertTag.TimRest,"Ordering " + amount + "x " + food.name + ".");
				market.manager.msgWantFood(myPerson.getBuilding().getID(), food.name, amount);
				break;
			}
		}
	}
    
    private void leaveBuilding()
    {
        canLeave = false;
        exitBuilding(myPerson);
    }
	
	public void addItemToInventory(String choice, int amount, int cookingTime)
	{
		Food food = new Food(choice, amount);
		cookingMap.put(choice, cookingTime);
		foods.add(food);
		stateChanged();
	}
	
	public String getName() {
		return myPerson.name;
	}
	
	private class Order
	{
		String choice;
		int tableNumber;
		TimWaiter waiter;
		OrderState state;
		
		public Order(TimWaiter waiter, String choice, int tableNumber)
		{
			this.waiter = waiter;
			this.choice = choice;
			this.tableNumber = tableNumber;
			state = OrderState.pending;
		}
	}
	
	private enum FoodState { inStock, onOrder };
	
	private class Food
	{
		String name;
		int inventory;
		int amountToOrder;
		FoodState state;
		
		public Food(String name, int inventory)
		{
			this.name = name;
			this.inventory = inventory;
			amountToOrder = NORMAL_AMOUNT_TO_ORDER;
			state = FoodState.inStock;
		}
	}
	
	/*public class Delivery
	{
	    MarketManagerRole manager;
	    Money price;
	    
	    public Delivery(MarketManagerRole manager, Money price)
	    {
	        this.manager = manager;
	        this.price = price;
	    }
	}*/
	
	private class MyMarket
	{
		MarketManagerRole manager;
		ArrayList<String> foodAvail;
		
		public MyMarket(MarketManagerRole manager, ArrayList<String> foods)
		{
			this.manager = manager;
			foodAvail = foods;
		}
	}
	
	public void addMarket(MarketManagerRole marketManager)
    {
        ArrayList<String> foodList = new ArrayList<String>();
        for (Food food : foods)
        {
            foodList.add(food.name);
        }
        markets.add(new MyMarket(marketManager, foodList));
        stateChanged();
    }

	public void setGui(TimCookGui cookGui) {
		gui = cookGui;
	}
	
	public TimCookGui getGui()
	{
	    return gui;
	}
	
	public void setHost(TimHostRole host)
	{
	    this.host = host;
	}

	public void print(String string) {
		System.out.println(string);
	}
	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
        myPerson.Do("Closing time.");
        //exitBuilding(myPerson);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TimCookRole";
	}
}
