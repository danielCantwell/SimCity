package market.interfaces;

public interface MarketClerk
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgWantFood(int id, String choice, int amount);
    void msgGiveToCustomer(int id, String choice, int amount);
    void msgHereIsMoney(int id, int amount);
}
