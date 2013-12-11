package market;

import java.util.*;

import brianRest.BrianHostRole;
import timRest.TimHostRole;
import exterior.gui.SimCityGui;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_DannyRestaurant;
import SimCity.Buildings.B_EricRestaurant;
import SimCity.Buildings.B_JesseRestaurant;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_TimRest;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import market.gui.MarketDeliveryPersonGui;
import market.interfaces.MarketDeliveryCashier;
import market.interfaces.MarketDeliveryCook;
import market.interfaces.MarketDeliveryPerson;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketDeliveryPersonRole extends Role implements MarketDeliveryPerson{
	
	private MarketDeliveryPersonGui gui = new MarketDeliveryPersonGui(this);
	
	/**
	 * Data
	 */

    public MarketManagerRole manager;
    public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
    public enum AgentLocation { Market, Destination, InTransit, Closed, Returned };
    public AgentLocation location;
    public B_Market home;

    private boolean canLeave = false;
	
	public MarketDeliveryPersonRole() {
		super();
		location = AgentLocation.Market;
	}

	/** 
	 * Messages
	 */

    public void msgMakeDelivery(int id, String choice, int amount)
    {
        Do(AlertTag.Market, "Okay. Delivering to building #" + id + ".");
        orders.add(new Order(id, choice, amount));
        stateChanged();
    }
    
    public void msgGuiArrivedAtMarket()
    {
        Do(AlertTag.Market, "I have returned.");
        location = AgentLocation.Returned;
        stateChanged();
    }

    public void msgGuiArrivedAtDestination()
    {
        location = AgentLocation.Destination;
        stateChanged();
    }

    public void msgGuiRestaurantClosed()
    {
        Do(AlertTag.Market, "Restaurant is closed.");
        location = AgentLocation.Closed;
        stateChanged();
    }

    public void msgLeaveMarket()
    {
        canLeave = true;
        stateChanged();
    }
    
    public void workOver()
    {
        //exitBuilding(myPerson);
        //stateChanged();
    }

    @Override
    protected void enterBuilding() {
        
    }

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(location == AgentLocation.Returned)
		{
		    manager.msgIAmBack(this);
            location = AgentLocation.Market;
	        if (canLeave)
	        {
	            leaveBuilding();
	            return false;
	        }
	        return true;
		}
	    if(location == AgentLocation.Market)
	    {
    	    synchronized(orders)
    	    {
    	        for (Order o : orders)
    	        {
    	            if (o.state == OrderState.Pending)
    	            {
    	                goToDestination(o);
    	                return false;
    	                // wait to arrive
    	            }
    	        }
    	    }
	    }
	    if(location == AgentLocation.Destination)
        {
            synchronized(orders)
            {
                for (Order o : orders)
                {
                    if (o.state == OrderState.Delivering)
                    {
                        deliver(o);
                        goToMarket();
                        return false;
                        // wait to arrive
                    }
                }
            }
        }
	    if(location == AgentLocation.Closed)
        {
	        goToMarket();
	        return false;
        }
	    
		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	/**
	 * Actions
	 */

	private void goToDestination(Order order)
	{
        exitBuilding(myPerson);
	    location = AgentLocation.InTransit;
		Building dest = God.Get().getBuilding(order.id);
		order.state = OrderState.Delivering;
		myPerson.msgGoToBuilding(dest, Intent.work);
	}
	
	private void deliver(Order order)
	{
	    Building building = God.Get().getBuilding(order.id);
	    // input code for each restaurant
	    if (building instanceof B_TimRest)
	    {
	        // cast to correct rest
	        B_TimRest r = (B_TimRest)building;
	        TimHostRole host = r.getManager();
            if (host.getCook() instanceof MarketDeliveryCook)
            {
                ((MarketDeliveryCook)host.getCook()).msgHereIsYourFood(order.choice, order.amount);
            }
            else
            {
                System.err.println("TimCookRole doesn't implement MarketDeliveryCook.");
            }
            if (host.getCashier() instanceof MarketDeliveryCashier)
            {
                ((MarketDeliveryCashier)host.getCashier()).msgPayMarket(order.amount, new Money(10, 0), manager);
            }
            else
            {
                System.err.println("TimCashierRole doesn't implement MarketDeliveryCashier.");
            }
	    }
	    else if (building instanceof B_BrianRestaurant)
        {
            // cast to correct rest
	        B_BrianRestaurant r = (B_BrianRestaurant)building;
	        if (r.cookRole instanceof MarketDeliveryCook)
	        {
	            ((MarketDeliveryCook)r.cookRole).msgHereIsYourFood(order.choice, order.amount);
	        }
            else
            {
                System.err.println("BrianCookRole doesn't implement MarketDeliveryCook.");
            }
            if (r.cashierRole instanceof MarketDeliveryCashier)
            {
                ((MarketDeliveryCashier)r.cashierRole).msgPayMarket(order.amount, new Money(10, 0), manager);
            }
            else
            {
                System.err.println("BrianCashierRole doesn't implement MarketDeliveryCashier.");
            }
        }
        else if (building instanceof B_DannyRestaurant)
        {
            // cast to correct rest
            B_DannyRestaurant r = (B_DannyRestaurant)building;
            if (r.cookRole instanceof MarketDeliveryCook)
            {
                ((MarketDeliveryCook)r.cookRole).msgHereIsYourFood(order.choice, order.amount);
            }
            else
            {
                System.err.println("DannyCook doesn't implement MarketDeliveryCook.");
            }
            if (r.cashierRole instanceof MarketDeliveryCashier)
            {
                ((MarketDeliveryCashier)r.cashierRole).msgPayMarket(order.amount, new Money(10, 0), manager);
            }
            else
            {
                System.err.println("DannyCashier doesn't implement MarketDeliveryCashier.");
            }
        }
        else if (building instanceof B_EricRestaurant)
        {
            // cast to correct rest
            B_EricRestaurant r = (B_EricRestaurant)building;
            if (r.cook instanceof MarketDeliveryCook)
            {
                ((MarketDeliveryCook)r.cook).msgHereIsYourFood(order.choice, order.amount);
            }
            else
            {
                System.err.println("EricCook doesn't implement MarketDeliveryCook.");
            }
            if (r.cashier instanceof MarketDeliveryCashier)
            {
                ((MarketDeliveryCashier)r.cashier).msgPayMarket(order.amount, new Money(10, 0), manager);
            }
            else
            {
                System.err.println("EricCashier doesn't implement MarketDeliveryCashier.");
            }
        }
        else if (building instanceof B_JesseRestaurant)
        {
            // cast to correct rest
            B_JesseRestaurant r = (B_JesseRestaurant)building;
            if (r.cook instanceof MarketDeliveryCook)
            {
                ((MarketDeliveryCook)r.cook).msgHereIsYourFood(order.choice, order.amount);
            }
            else
            {
                System.err.println("JesseCook doesn't implement MarketDeliveryCook.");
            }
            if (r.cashier instanceof MarketDeliveryCashier)
            {
                ((MarketDeliveryCashier)r.cashier).msgPayMarket(order.amount, new Money(10, 0), manager);
            }
            else
            {
                System.err.println("JesseCashier doesn't implement MarketDeliveryCashier.");
            }
        }
        else
        {
            Error(AlertTag.Market, "Not making delivery to restaurant.");
            return;
        }
	    orders.remove(order);
	}
    
    private void leaveBuilding()
    {
        myPerson.money.add(new Money(75, 00));
        Info(AlertTag.Market, "I have " + myPerson.money + " and I'm leaving the building.");
        canLeave  = false;
        exitBuilding(myPerson);
        myPerson.msgGoHome();
    }

    private void goToMarket()
    {
        exitBuilding(myPerson);
        location = AgentLocation.InTransit;
        myPerson.msgGoToBuilding(home, Intent.work);
    }
		
    /**
     * Utilities
     */
    public void setHomeMarket(B_Market home)
    {
        this.home = home;
    }

	public void setGui(MarketDeliveryPersonGui gui) {
		this.gui = gui;
	}

    public MarketDeliveryPersonGui getGui() { return gui; }
    
    public void setManager(MarketManagerRole manager)
    {
        this.manager = manager;
    }

    public void setPerson(Person person)
    {
        super.setPerson(person);
    }

    /**
     * Inner Classes
     */
	
		public enum OrderState { Pending, Delivering, Ready };
		
		public class Order
		{
			int id;
		    String choice;
		    int amount;
		    OrderState state;
		    
		    Order(int id, String choice, int amount)
		    {
		        this.id = id;
		        this.choice = choice;
		        this.amount = amount;
		        state = OrderState.Pending;
		    }
		    
		    public int getId()
		    {
		        return id;
		    }

            public void setPending()
            {
                state = OrderState.Pending;
            }
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "MDvy";
		}
}
