package market;

import java.util.*;

import timRest.TimHostRole;
import exterior.gui.SimCityGui;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_Market;
import SimCity.Buildings.B_TimRest;
import SimCity.Globals.Money;
import market.gui.MarketDeliveryPersonGui;
import market.interfaces.MarketDeliveryPerson;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketDeliveryPersonRole extends Role implements MarketDeliveryPerson{
	
	private SimCityGui cityGui;
	private MarketDeliveryPersonGui gui = new MarketDeliveryPersonGui(this);
	
	/**
	 * Data
	 */

    public MarketManagerRole manager;
    public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
    public enum AgentLocation { Market, Destination, InTransit };
    public AgentLocation location;
    public B_Market home;
	
	public MarketDeliveryPersonRole() {
		super();
		location = AgentLocation.Market;
	}

	/** 
	 * Messages
	 */

    public void msgMakeDelivery(int id, String choice, int amount)
    {
        orders.add(new Order(id, choice, amount));
        stateChanged();
    }
    
    public void msgGuiArrivedAtMarket()
    {
        location = AgentLocation.Market;
        stateChanged();
    }

    public void msgGuiArrivedAtDestination()
    {
        location = AgentLocation.Destination;
        stateChanged();
    }

    public void workOver()
    {
        exitBuilding(myPerson);
        stateChanged();
    }

    @Override
    protected void enterBuilding() {
        
    }

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
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
	        host.getCook().msgHereIsYourFood(order.choice, order.amount, manager, new Money(0, 0));
	    }
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
		}
}
