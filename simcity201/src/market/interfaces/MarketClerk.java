package market.interfaces;

import SimCity.Globals.Money;

public interface MarketClerk
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgTakeOrder(MarketCustomer customer);
    void msgWantFood(int id, String choice, int amount);
    void msgGiveToCustomer(int id, String choice, int amount);
    void msgHereIsMoney(int id, Money prices);
    void msgLeaveMarket();
}
