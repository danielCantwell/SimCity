package market.interfaces;

import market.MarketCustomerRole;
import SimCity.Globals.Money;

public interface MarketClerk
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgTakeOrder(MarketCustomerRole customer);
    void msgWantFood(int id, String choice, int amount);
    void msgGiveToCustomer(int id, String choice, int amount);
    void msgHereIsMoney(int id, Money prices);
}
