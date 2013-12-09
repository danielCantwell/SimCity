package market.interfaces;

import market.MarketManagerRole;
import SimCity.Globals.Money;

public interface MarketDeliveryCook
{
    // adds food to inventory
    // calls
    //      cashier.msgPayMarket(managerId, price);
    void msgHereIsYourFood(String food, int amount);
}
