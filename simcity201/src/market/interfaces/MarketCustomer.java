package market.interfaces;

import java.awt.Point;

import market.MarketClerkRole;
import SimCity.Globals.Money;

public interface MarketCustomer
{
	/**
	 * @author Timothy So
	 */
	
    // MESSAGES
    void msgPleaseTakeANumber(Point location);
    void msgWhatDoYouWant(MarketClerkRole clerk);
    void msgHereIsYourFood(String food, int amount, Money price);
    void msgGuiArrivedAtLine();
    void msgGuiArrivedAtClerk();
    void msgGuiArrivedAtDoor();
}
