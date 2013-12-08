package EricRestaurant;
import EricRestaurant.gui.AnimationPanel;
import EricRestaurant.gui.CookGui;
import EricRestaurant.gui.HostGui;
//import EricRestaurant.interfaces.Market;
import EricRestaurant.interfaces.Waiter;
import SimCity.Base.Role;
import SimCity.Base.Person.Intent;
import agent.Agent;

import javax.swing.Timer;

import java.util.*;

public class EricCook extends Role {

//	public Market market;
	public CookGui cookGui = null;
	private String name;
	Map<String, Food> mymap = new HashMap<String, Food>();

	private List<Order>orders = Collections.synchronizedList(new ArrayList<Order>());
	class Order {
		Waiter w;
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
		steakfood = new Food("Steak", 2, 2, 1);
		chickenfood = new Food("Chicken", 2, 2, 1);
		saladfood = new Food("Salad", 2, 2, 1);
		pizzafood = new Food("Pizza", 2, 2, 1);
		mymap.put("Steak", steakfood);
		mymap.put("Pizza", pizzafood);
		mymap.put("Chicken", chickenfood);
		mymap.put("Salad", saladfood);
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


	public void giveCook(String choice, Waiter w) {
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
//			if (cs == cookState.ordering) {
//				for (Order o : orders) {
//					if(o.s == state.low) {
//						msgMarket(o.choice);
//					}
//				}
//
//				return true;	
//			}
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
		return false;
	}


	//actions

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
		System.out.println("Cook: Plating the order and giving it to the waiter");
		o.w.orderDone(o.choice);
		o.s = state.waiting;
	}

	public void giveWaiter(Order o) {
		o.w.atCook(o.choice);
		orders.remove(o);
	}
	//	public void notifyWaiter(String s){
	//	for (Order o : orders) {
	//		if(o.choice == s) {
	//			o.w.restock(s);
	//			orders.remove(o);
	//		}
	//	}
	//	}
//	public void msgMarket(String choice) {
//		market.cookOrder(choice, 3, this);
//		cs = cookState.idle;
//	}
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
	}

	@Override
	public void workOver() {
		cookGui.doLeaveBuilding();
		AnimationPanel ap = (AnimationPanel)myPerson.building.getPanel();
		ap.removeGui(cookGui);
		myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		exitBuilding(myPerson);		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "EricCook";
	}
}
