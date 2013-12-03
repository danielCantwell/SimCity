package market.interfaces;

public interface MarketDeliveryPerson
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgMakeDelivery(int id, String choice, int amount);
    void msgGuiArrivedAtMarket();
    void msgGuiArrivedAtDestination();
}
