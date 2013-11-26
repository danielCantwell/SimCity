package market;

import SimCity.Base.Person;
import SimCity.Base.Role;
import market.gui.MarketClerkGui;
import market.interfaces.MarketClerk;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketClerkRole extends Role implements MarketClerk {
	
		private MarketClerkGui gui = new MarketClerkGui(this);
		
		/**
		 * Data
		 */
		
	    public MarketManagerRole manager;
		
		public MarketClerkRole() {
			super();
		}
	
		/** 
		 * Messages
		 */

    public void msgGiveToCustomer(int id, String food, int amount)
    {
        // TODO Auto-generated method stub

        stateChanged();
    }

    public void msgWantFood(int id, String choice, int amount)
    {
        // TODO Auto-generated method stub

        stateChanged();
    }

    public void msgHereIsMoney(int id, int amount)
    {
        // TODO Auto-generated method stub

        stateChanged();
    }

    public void workOver()
    {
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

    /**
     * Utilities
     */

		public void setGui(MarketClerkGui gui) {
			this.gui = gui;
		}
	
		public MarketClerkGui getGui() { return gui; }

    @Override
    protected void enterBuilding() {
        // TODO Auto-generated method stub
        
    }
    
    public void setPerson(Person person)
    {
        super.setPerson(person);
    }
    
    public void setManager(MarketManagerRole manager)
    {
        this.manager = manager;
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
