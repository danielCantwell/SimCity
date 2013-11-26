package market;

import SimCity.Base.Person;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import market.gui.MarketCustomerGui;
import market.interfaces.MarketCustomer;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketCustomerRole extends Role implements MarketCustomer {
	
	private MarketCustomerGui gui = new MarketCustomerGui(this);
	
	/**
	 * Data
	 */
	
    public MarketManagerRole manager;
	
	public MarketCustomerRole() {
		super();
	}

	/** 
	 * Messages
	 */

    public void msgHereIsYourFood(String food, int amount, Money price)
    {
        // TODO Auto-generated method stub

        stateChanged();
    }

    public void msgGuiArrivedAtClerk()
    {
        // TODO Auto-generated method stub

        stateChanged();
    }

    public void msgGuiArrivedAtDoor()
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

		public void setGui(MarketCustomerGui gui) {
			this.gui = gui;
		}
	
		public MarketCustomerGui getGui() { return gui; }

    @Override
    protected void enterBuilding() {
        // TODO Auto-generated method stub
        
    }
    
    public void setPerson(Person person)
    {
        super.setPerson(person);
    }

    /**
     * Inner Classes
     */
	
	public enum OrderState { Pending, Processing, Ready };
	
	public class Order
	{
	    String choice;
	    int amount;
	    OrderState state;
	    
	    Order(String choice, int amount)
	    {
	        this.choice = choice;
	        this.amount = amount;
	        state = OrderState.Pending;
	    }
	    
	    public boolean equals(Order other)
	    {
	        return choice.equals(other.choice) && amount == other.amount;
	    }
	}
}
