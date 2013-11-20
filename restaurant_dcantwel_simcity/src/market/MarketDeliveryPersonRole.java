package market;

import java.util.*;

import market.gui.MarketClerkGui;
import market.gui.MarketDeliveryPersonGui;
import market.interfaces.MarketDeliveryPerson;
import market.interfaces.MarketManager;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketDeliveryPersonRole extends Role implements MarketDeliveryPerson{
	
	private String name;
	private MarketDeliveryPersonGui gui;
	
	/**
	 * Data
	 */
	
	
	public MarketDeliveryPersonRole(String name) {
		super();
		
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/** 
	 * Messages
	 */

    public void msgWantFood(String name, String choice, int amount)
    {
        // TODO Auto-generated method stub
        
    }
    
    public void msgGiveToCustomer(String name, String choice, int amount)
    {
        // TODO Auto-generated method stub
        
    }

    public void msgHereIsMoney(String name, int amount)
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

	public void setGui(MarketDeliveryPersonGui gui) {
		this.gui = gui;
	}

	/*
	 * public HostGui getGui() { return hostGui; }
	 */
	

    /**
     * Inner Classes
     */
	
}
