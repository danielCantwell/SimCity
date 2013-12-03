package market.test.mock;

import java.awt.Point;

import SimCity.Globals.Money;
import market.MarketClerkRole;
import market.interfaces.MarketCustomer;

public class MockMarketCustomer implements MarketCustomer
{

    @Override
    public void msgPleaseTakeANumber(Point location)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgWhatDoYouWant(MarketClerkRole clerk)
    {
        System.out.println("Customer: recieved msgWhatDoYouWant.");
    }

    @Override
    public void msgHereIsYourFood(String food, int amount, Money price)
    {
        System.out.println("Customer: recieved msgHereIsYourFood.");
    }

    @Override
    public void msgGuiArrivedAtLine()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgGuiArrivedAtClerk()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgGuiArrivedAtDoor()
    {
        // TODO Auto-generated method stub

    }

}
