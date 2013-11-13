/**
 * 
 */
package restaurant.test.mock;

import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * @author Daniel
 *
 */
public class MockWaiter extends Mock implements Waiter {
	
	public EventLog log = new EventLog();
	
	String name;

	public MockWaiter(String name) {
		super(name);
		this.name = name;
	}

	@Override
	public void msgHereIsBill(Customer customer, double cost) {
		log.add(new LoggedEvent("Received msgHereIsBill from cashier. Customer owes " + cost));
	}

	@Override
	public void msgDoneAndPaying(Customer customer) {
		log.add(new LoggedEvent("Received msgDoneAndPaying from customer."));
	}

	@Override
	public void msgReadyToOrder(Customer customer) {
		log.add(new LoggedEvent("Received msgReadyToOrder from customer."));
	}

	@Override
	public void msgHereIsMyOrder(String choice, Customer customer) {
		log.add(new LoggedEvent("Received msgHereIsMyOrder from customer. Customer ordered " + choice));
	}

	@Override
	public void msgCantAffordAnythingElse(Customer customer) {
		log.add(new LoggedEvent("Received msgCantAffordAnythingElse from customer."));
	}

}
