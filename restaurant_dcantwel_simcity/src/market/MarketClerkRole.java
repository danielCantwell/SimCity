package market;

import java.util.*;

import SimCity.Base.Role;
import market.gui.MarketClerkGui;
import market.interfaces.MarketClerk;
import market.interfaces.MarketManager;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketClerkRole extends Role implements MarketClerk {
	
	private String name;
	private MarketClerkGui gui;
	
	/**
	 * Data
	 */
	
	public MarketClerkRole(String name) {
		super();
		
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/** 
	 * Messages
	 */

    public void msgGiveToCustomer(String name, String food, int amount)
    {
        // TODO Auto-generated method stub
        
    }

    public void msgWantFood(String name, String choice, int amount)
    {
        // TODO Auto-generated method stub
        
    }

    public void msgHereIsMoney(String name, int amount)
    {
        // TODO Auto-generated method stub
        
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

	/*
	 * public HostGui getGui() { return hostGui; }
	 */
	

    /**
     * Inner Classes
     */
	
	public enum OrderState { Pending, Processing, Ready };
	
	public class Order
	{
	    String name;
	    String choice;
	    int amount;
	    OrderState state;
	    
	    Order(String name, String choice, int amount)
	    {
	        this.name = name;
	        this.choice = choice;
	        this.amount = amount;
	        state = OrderState.Pending;
	    }
	}
}
