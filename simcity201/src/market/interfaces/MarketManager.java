package market.interfaces;

public interface MarketManager
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgWantFood(String name, String choice, int amount);
    void msgFulfillOrder(String name, String choice, int amount);
    void msgOrderPacked(String name, String choice, int amount);
    void msgHereIsTheMoney(String name, int amount);
    void msgWithdrawalSuccessful(int amount);
    void msgDepositSuccessful();
}
