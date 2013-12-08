package market;

import java.util.*;

import exterior.gui.SimCityGui;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Base.Person.Intent;
import SimCity.Buildings.B_Market;
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
    public int destinationBuildingID;
    public B_Market home;
	
	public MarketDeliveryPersonRole() {
		super();
	}

	/** 
	 * Messages
	 */

    public void msgMakeDelivery(int id, String choice, int amount)
    {
        orders.add(new Order(id, choice, amount));
        destinationBuildingID = id;
        myPerson.msgExitBuilding();
        myPerson.mainRole.setActive(false);
        myPerson.msgGoToBuilding(God.Get().getBuilding(id), Intent.work);
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
        // TODO Auto-generated method stub
        stateChanged();
    }

    @Override
    protected void enterBuilding() {
    }
    
    @Override
    protected void exitBuilding(Person p)
    {
        gui.setPresent(false);
        super.exitBuilding(p);
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
	    if(location == AgentLocation.Market)
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
	    location = AgentLocation.InTransit;
		Building dest = God.Get().getPerson(order.id).building;
		myPerson.msgGoToBuilding(dest, Intent.work);
	}
	
	private void deliver(Order order)
	{
	    //God.Get().getBuilding(order.id)
	    // TODO: cannot implement without solid restaurant design document
	}

    private void goToMarket()
    {
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

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "MarketDeliveryPerson";
		}
}
