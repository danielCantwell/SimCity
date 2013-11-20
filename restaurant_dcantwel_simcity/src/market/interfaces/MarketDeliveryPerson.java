package market.interfaces;

public interface MarketDeliveryPerson
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgWantFood(String name, String choice, int amount);
    void msgGiveToCustomer(String name, String choice, int amount);
    void msgHereIsMoney(String name, int amount);
}