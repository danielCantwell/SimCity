package restaurant.test.mock;


import restaurant.CookAgent;
import restaurant.interfaces.Market;

/**
 * A sample MockMarket built to unit test.
 *
 *
 */
public class MockMarket extends Mock implements Market {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public EventLog log;

	public MockMarket(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgWantMoreFood(CookAgent cook, String choice, int amount) {
		log.add(new LoggedEvent("Received msgWantMoreFood from " + cook.getName() + ". " + choice + " " + amount + "x."));
	}
	
	@Override
	public void msgHereIsPayment(double price)
	{
		log.add(new LoggedEvent("Received msgHereIsPayment. Price: " + price + "."));
	}
}
