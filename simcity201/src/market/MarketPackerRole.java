package market;

import java.util.*;

import SimCity.Base.Role;
import market.gui.MarketManagerGui;
import market.gui.MarketPackerGui;
import market.interfaces.MarketManager;
import market.interfaces.MarketPacker;

/**
 * Market Manager Agent
 * 
 * @author Timothy So
 */
public class MarketPackerRole extends Role implements MarketPacker {
	
	private String name;

	private MarketPackerGui gui = new MarketPackerGui(this); 
	
	public MarketPackerRole(String name) {
		super();

		this.name = name;
	}

	public String getName() {
		return name;
	}

	/** 
	 * Messages
	 */
	
	public void msgPackage(String name, String choice, int amount)
	{
	    
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

	public void setGui(MarketPackerGui gui) {
		this.gui = gui;
	}
	
	public MarketPackerGui getGui()
	{
	    return gui;
	}

	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}
}
