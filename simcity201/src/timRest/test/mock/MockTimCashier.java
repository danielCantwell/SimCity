package restaurant.test.mock;


import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCashier built to unit test.
 *
 *
 */
public class MockCashier extends Mock implements Cashier {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public EventLog log;

	public MockCashier(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgHereIsACheck(Waiter waiter, String choice, int tableNumber) {
		// TODO Auto-generated method stub
		// change internals
		
	}

	@Override
	public void msgWantCheck(int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheMoney(double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPartialMoney(double amount, double price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsTheBill(Market market, double price)
	{
	}
}
