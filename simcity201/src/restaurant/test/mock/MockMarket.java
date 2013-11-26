/**
 * 
 */
package restaurant.test.mock;

import java.util.Map;

import restaurant.DannyCook;
import restaurant.MarketAgent.Order;
import restaurant.interfaces.Market;

/**
 * @author Daniel
 *
 */
public class MockMarket extends Mock implements Market {
	
	public EventLog log = new EventLog();

	public MockMarket(String name) {
		super(name);
	}

	@Override
	public void msgOrderFood(DannyCook cook, Map<String, Integer> lowFood) {
		log.add(new LoggedEvent("Received msgOrderFood from cook"));
	}

	@Override
	public void msgOrderReady(Order order) {
		log.add(new LoggedEvent("Received msgOrderReady"));
	}

	@Override
	public void msgPayment(double payment) {
		log.add(new LoggedEvent("Received msgPayment from cashier. Cashier payed $" + payment));
	}

}
