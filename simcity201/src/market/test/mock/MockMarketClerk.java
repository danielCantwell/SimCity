package market.test.mock;

import SimCity.Globals.Money;
import market.interfaces.MarketClerk;
import market.interfaces.MarketCustomer;

public class MockMarketClerk implements MarketClerk
{

    @Override
    public void msgWantFood(int id, String choice, int amount)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgGiveToCustomer(int id, String choice, int amount)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgHereIsMoney(int id, Money prices)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgTakeOrder(MarketCustomer customer)
    {
        // TODO Auto-generated method stub
        
    }

}
