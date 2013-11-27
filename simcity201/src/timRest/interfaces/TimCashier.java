package timRest.interfaces;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface TimCashier {

	public abstract void msgHereIsACheck(TimWaiter waiter, String choice, int tableNumber);
	public abstract void msgWantCheck(int tableNumber);
	public abstract void msgHereIsTheMoney(double amount);
	public abstract void msgHereIsPartialMoney(double amount, double price);

	public abstract String getName();
}