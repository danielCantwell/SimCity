package market.interfaces;

import market.MarketManagerRole;
import SimCity.Globals.Money;

public interface MarketDeliveryCashier
{
    // calls
    //      manager.msgHereIsTheMoney(pricePerUnit * amount);
    // subtracts money from restaurant
    void msgPayMarket(int amount, Money pricePerUnit, MarketManagerRole manager);
}
