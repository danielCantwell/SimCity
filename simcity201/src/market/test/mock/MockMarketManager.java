package market.test.mock;

import SimCity.Globals.Money;
import market.interfaces.MarketClerk;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketManager;

public class MockMarketManager implements MarketManager
{

    @Override
    public void msgWantClerk(MarketCustomer customer, int id)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgWantFood(int id, String choice, int amount)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgFulfillOrder(MarketClerk clerk, int id, String choice,
            int amount)
    {
        System.out.println("Manager: recieved msgFulfillOrder.");
    }

    @Override
    public void msgOrderPacked(int id, String choice, int amount)
    {
        System.out.println("Manager: recieved msgOrderPacked.");
    }

    @Override
    public void msgHereIsTheMoney(MarketClerk clerk, int id, Money amount)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgWithdrawalSuccessful(Money amount)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgDepositSuccessful(Money amount)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void msgGrabbingItem(String item, int amount)
    {
        System.out.println("Manager: recieved msgGrabbingItem.");
    }

    @Override
    public void msgIAmFree(MarketClerk marketClerkRole)
    {
        System.out.println("Manager: recieved msgIAmFree.");
    }

}
