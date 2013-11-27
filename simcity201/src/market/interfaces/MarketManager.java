package market.interfaces;

import SimCity.Globals.Money;
import market.MarketClerkRole;

public interface MarketManager
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgWantFood(int id, String choice, int amount);
    void msgFulfillOrder(MarketClerkRole clerk, int id, String choice, int amount);
    void msgOrderPacked(int id, String choice, int amount);
    void msgHereIsTheMoney(MarketClerkRole clerk, int id, Money amount);
    void msgWithdrawalSuccessful(Money amount);
    void msgDepositSuccessful(Money amount);
    void msgIAmFree(MarketClerkRole marketClerkRole);
}
