package market.interfaces;

import SimCity.Globals.Money;

public interface MarketManager
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgWantFood(String name, String choice, int amount);
    void msgFulfillOrder(String name, String choice, int amount);
    void msgOrderPacked(String name, String choice, int amount);
    void msgHereIsTheMoney(String name, Money amount);
    void msgWithdrawalSuccessful(Money amount);
    void msgDepositSuccessful(Money amount);
}
