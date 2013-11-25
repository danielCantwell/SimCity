package market.interfaces;

import SimCity.Globals.Money;

public interface MarketCustomer
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgHereIsYourFood(String food, int amount, Money price);
    void msgGuiArrivedAtClerk();
    void msgGuiArrivedAtDoor();
}
