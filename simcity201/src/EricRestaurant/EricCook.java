package EricRestaurant;
import EricRestaurant.EricOrderStand.Orders;
import EricRestaurant.gui.AnimationPanel;
import EricRestaurant.gui.CookGui;

import EricRestaurant.gui.HostGui;
//import EricRestaurant.interfaces.Market;
import EricRestaurant.interfaces.Waiter;
import SimCity.Base.God;
import SimCity.Base.Role;
import SimCity.Base.God.BuildingType;
import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_EricRestaurant;
import SimCity.Buildings.B_Market;
import SimCity.trace.AlertTag;
import agent.Agent;
import brianRest.BrianCookRole.Food;

import javax.swing.Timer;

import market.interfaces.MarketDeliveryCook;

import java.util.*;

public class EricCook extends Role implements MarketDeliveryCook {

//	public Market market;
	public CookGui cookGui = null;
	private String name;
	Map<String, Food> mymap = new HashMap<String, Food>();
	EricOrderStand orderstand;
	private String searchMarketsFor = "";
    private List<B_Market> markets = new ArrayList<B_Market>();

	private List<Order>orders = Collections.synchronizedList(new ArrayList<Order>());
	class Order {
		EricAbstractWaiter w;
		String choice;
		int table;
		state s;
	}
	private List<Food> foods = Collections.synchronizedList(new ArrayList<Food>());	
	class Food {
		String type;
		int cookingTime;
		int amount;
		int low;
		   private int orderFromIndex = 0;

		public Food(String t, int ct, int a, int l) {
			type = t;
			cookingTime = ct;
			amount = a;
			low = l;
		}
	}		
	Food steakfood;
	Food chickenfood;
	Food saladfood;
	Food pizzafood;

	public EricCook(String name) {
		super();
		this.name = name;
		Random generator = new Random(); 
		int i = generator.nextInt(2)+1;
		steakfood = new Food("Steak", 2, 6, 5);
		chickenfood = new Food("Chicken", 2, 6, 5);
		saladfood = new Food("Salad", 2, 6, 5);
		pizzafood = new Food("Pizza", 2, 6, 5);
		mymap.put("Steak", steakfood);
		mymap.put("Pizza", pizzafood);
		mymap.put("Chicken", chickenfood);
		mymap.put("Salad", saladfood);
	    markets.add((B_Market) God.Get().findBuildingOfType(BuildingType.Market));

	}


	public enum state {nothing, pending, cooking, done, waiting, waiterHere, low};
	private state s = state.nothing;//The start state

	public enum cookState {idle, ordering, specordering};
	private cookState cs = cookState.idle;
	//	Timer timer = new Timer(1000,this);

	// public WaiterAgent waiter;

//	public void addMarket(Market m) {
//		market = m;
//		Food myfood = mymap.get("Pizza");
//		if(myfood.amount <= 1) {
//			System.out.println("Cook: "+myfood.type+" is low, ording more from the market");
//			cs = cookState.specordering;
//			stateChanged();
//		}
//	}

	//messages
	private void SearchMarkets(String choice){
        Do(AlertTag.BrianRest, "Searching other markets for " + choice+ ".");
        searchMarketsFor = "";
        Food temp = mymap.get(choice);
        if (temp.orderFromIndex == markets.size()) {
                Do(AlertTag.BrianRest, "Stopped searching for "+ temp.type+".");
                return; //If the cook searched all the markets, then forget about searching more.
        }
        markets.get(temp.orderFromIndex).getManager().msgWantFood(myPerson.building.getID(), temp.type, 4);
}

	public void giveCook(String choice, EricAbstractWaiter w) {
		//		this.waiter = w;
		Order myorder = new Order();
		myorder.choice = choice;
		myorder.w = w;
		orders.add(myorder);
		myorder.s = state.pending;
		System.out.println("Cook: has recieved order from waiter");
		Food myfood = mymap.get(choice);
		myfood.type = choice;
		myfood.amount = myfood.amount - 1;

		if(myfood.amount == myfood.low) {
			System.out.println("Cook: "+myfood.type+" is low, ording more from the market");
			cs = cookState.ordering;
			myorder.s = state.low;

		}
		if(myfood.amount == 0) {
			System.out.println("Cook: "+myfood.type+" is completely out. Do not order anymore");
			w.msgWaiter(choice);
		}

		stateChanged();
	}

	public void msgImHere(Waiter w) {
		synchronized(orders) {
			for (Order o : orders) {
				if (o.w == w) {
					o.s = state.waiterHere;
					stateChanged();
				}
			}
		}
	}

	public void deliver(String s, int a) {
		Food myfood = mymap.get(s);
		myfood.amount = myfood.amount + a;
		System.out.println("Cook: inventory for "+s+" is "+myfood.amount);
		//notifyWaiter(s);
		stateChanged();
	}


	//scheduler

	protected boolean pickAndExecuteAnAction() {
//		System.out.println("order size "+orders.size()+" and myPerson is: "+myPerson);
//		if (cs == cookState.specordering) {
//			msgMarket("Pizza");
//		}
		synchronized(orders) {
			if (cs == cookState.ordering) {
				for (Order o : orders) {
					if(o.s == state.low) {
						msgMarket(o);
					}
				}

				return true;	
			}
		}
		synchronized(orders) {
			for (Order o : orders) {
				if(o.s == state.waiterHere) {
					giveWaiter(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for (Order o : orders) {
				if(o.s == state.done) {
					plateIt(o);
					return true;
				}
			}
		}
		synchronized(orders) {

			for (Order o : orders) {
				if(o.s == state.cooking) {
					cookfood(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for (Order o : orders) {
				if(o.s == state.pending || o.s == state.low) {
					cookIt(o);
					return true;

				}
			}
		}
		if(orderstand.getSize()>0) {
			addOrder(orderstand.popFirstOrder());
		}
		return false;
	}


	//actions

	public void addOrder(Orders orders2) {
		Do(AlertTag.EricRest,"Cook: getting order from orderstand");
		Order o = new Order();
		o.choice = orders2.choice;
		o.table = orders2.tableNumber;
		o.w = orders2.ew;
		o.s = state.pending;
		orders.add(o);
		stateChanged();
	}
	
	public void cookIt(final Order o) {
		//		timer.schedule(new Timer task()){
		//			o.s = state.cooking;
		//			stateChanged();
		//		}
		//		
		o.s = state.cooking;
		cookGui.getIng();
		System.out.println("Cook: started cooking the food");
		try {
			Thread.sleep(1000);
			//atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.goGrill();
		try {
			Thread.sleep(500);
			//atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cookGui.goCounter();
		try {
			Thread.sleep(500);
			//atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stateChanged();
	}

	public void cookfood(final Order o) {
		//		timer.start();
		System.out.println("Cook: finished cooking the food");
		o.s = state.done;
		stateChanged();
	}

	public void plateIt(Order o) {
		System.out.println("Cook: Plating the order and giving it to the waiter: "+o.w);
		o.w.orderDone(o.choice);
		o.s = state.waiting;
	}

	public void giveWaiter(Order o) {
		o.w.atCook(o.choice);
		orders.remove(o);
	}
		public void notifyWaiter(String s){
		for (Order o : orders) {
			if(o.choice == s) {
				o.w.restock(s);
				orders.remove(o);
			}
		}
		}
	public void msgMarket(Order o) {
        Do(AlertTag.BrianRest, "Low amount of "+ o.choice+". Ordering more.");
        Food temp = mymap.get(o.choice);
        if (temp.orderFromIndex < markets.size())
        {
        if (markets.get(temp.orderFromIndex).getManager() !=null)
        markets.get(temp.orderFromIndex).
        getManager().msgWantFood(
       		 myPerson.building.getID(),
       		 temp.type, 5);
        }
		cs = cookState.idle;
	}
	public void setGui(CookGui gui) {
		cookGui = gui;
	}


	public CookGui getGui() {
		return cookGui;
	}

	@Override
	protected void enterBuilding() {
		EricRestaurant.gui.CookGui cg = new EricRestaurant.gui.CookGui(this);
		cookGui = cg;
		AnimationPanel ap = (AnimationPanel)myPerson.building.getPanel();
		ap.addGui(cg);	
		cookGui.setText("Cook");
		B_EricRestaurant er = (B_EricRestaurant)myPerson.getBuilding();
		orderstand = er.getOrderStand();
	}

	@Override
	public void workOver() {
		myPerson.getMoney().add(50, 0);
		cookGui.doLeaveBuilding();
		AnimationPanel ap = (AnimationPanel)myPerson.building.getPanel();
		ap.removeGui(cookGui);
		myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		exitBuilding(myPerson);		
	}
	public void msgStateChanged(){
		stateChanged();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ECok";
	}

	@Override
	public void msgHereIsYourFood(String food, int amount) {
		Do(AlertTag.EricRest, "Refilling " + food + " by " + amount+".");
		Food f = mymap.get(food);
		f.amount = mymap.get(food).amount+amount;
		mymap.put(food,  f);
	}
}
