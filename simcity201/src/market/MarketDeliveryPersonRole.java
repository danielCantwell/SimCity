package market;

import java.util.*;

import exterior.gui.SimCityGui;
import SimCity.Base.Building;
import SimCity.Base.God;
import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Base.Person.Intent;
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
	
		public MarketDeliveryPersonRole() {
			super();
		}
	
		/** 
		 * Messages
		 */

    public void msgMakeDelivery(int id, String choice, int amount)
    {
        // TODO Auto-generated method stub
    	
    }
    
    public void guiArrivedAtMarket()
    {
        // TODO Auto-generated method stub
        
    }

    public void guiArrivedAtDestination()
    {
        // TODO Auto-generated method stub
        
    }

    public void workOver()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void enterBuilding() {
        // TODO Auto-generated method stub
        
    }

		/**
		 * Scheduler. Determine what action is called for, and do it.
		 */
		protected boolean pickAndExecuteAnAction() {
			
		    
		    
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
				Building dest = God.Get().getPerson(order.id).building;
				myPerson.msgGoToBuilding(dest, Intent.customer);
		}
		
    /**
     * Utilities
     */

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
        person.gui = gui;
    }
	

    /**
     * Inner Classes
     */
	
		public enum OrderState { Pending, Processing, Ready };
		
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
