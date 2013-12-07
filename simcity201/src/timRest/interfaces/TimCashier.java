package timRest.interfaces;

import SimCity.Globals.Money;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Timothy So
 *
 */
public interface TimCashier {

	public abstract void msgHereIsACheck(TimWaiter waiter, String choice, int tableNumber);
	public abstract void msgWantCheck(int tableNumber);
	public abstract void msgHereIsTheMoney(Money amount);
	public abstract void msgHereIsPartialMoney(Money amount, Money price);

	public abstract String getName();
}