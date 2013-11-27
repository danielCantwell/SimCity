package restaurant.interfaces;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {

	public abstract void msgHereIsACheck(Waiter waiter, String choice, int tableNumber);
	public abstract void msgWantCheck(int tableNumber);
	public abstract void msgHereIsTheMoney(double amount);
	public abstract void msgHereIsPartialMoney(double amount, double price);
	public abstract void msgHereIsTheBill(Market market, double price);

	public abstract String getName();
}