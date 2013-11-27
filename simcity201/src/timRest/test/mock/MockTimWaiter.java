package restaurant.test.mock;


import java.awt.Point;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockWaiter built to unit test.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log;

	public MockWaiter(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgSeatAtTable(Customer customer, int tableNumber,
			Point tablePos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImReadyToOrder(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWouldLike(Customer customer, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCannotCookItem(int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(int tableNumber, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCanIHaveCheck(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsACheck(int tableNumber, double price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCannotBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoOffBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSetWantBreak(boolean wantsBreak) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToHost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToCook() {
		// TODO Auto-generated method stub
		
	}

}
