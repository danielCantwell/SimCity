package market.interfaces;

import SimCity.Globals.Money;

public interface MarketManager
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgWantFood(int id, String choice, int amount);
    void msgFulfillOrder(int id, String choice, int amount);
    void msgOrderPacked(int id, String choice, int amount);
    void msgHereIsTheMoney(int id, Money amount);
    void msgWithdrawalSuccessful(Money amount);
    void msgDepositSuccessful(Money amount);
}
