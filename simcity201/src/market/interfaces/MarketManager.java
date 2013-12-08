package market.interfaces;

import SimCity.Globals.Money;

public interface MarketManager
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgWantClerk(MarketCustomer customer, int id);
    void msgWantFood(int id, String choice, int amount);
    void msgFulfillOrder(MarketClerk clerk, int id, String choice, int amount);
    void msgOrderPacked(int id, String choice, int amount);
    void msgHereIsTheMoney(Money amount);
    void msgWithdrawalSuccessful(Money amount);
    void msgDepositSuccessful(Money amount);
    void msgGrabbingItem(String item, int amount);
    void msgIAmFree(MarketClerk marketClerkRole);
}
