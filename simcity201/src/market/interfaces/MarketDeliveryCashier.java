package market.interfaces;

import market.MarketManagerRole;
import SimCity.Globals.Money;

public interface MarketDeliveryCashier
{
    // calls
    //      manager.msgHereIsTheMoney(price);
    // subtracts money from restaurant
    void msgPayMarket(MarketManagerRole manager, Money price);
}
