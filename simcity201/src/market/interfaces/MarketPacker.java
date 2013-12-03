package market.interfaces;

import market.MarketPackerRole.AgentLocation;

public interface MarketPacker
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgPackage(int id, String choice, int amount, int location);
    void msgGuiArrivedAtCounter();
    void msgGuiArrivedAtItem();
}
