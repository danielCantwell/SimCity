package restaurant.test.mock;


import java.awt.Point;
import java.util.HashMap;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Timothy So
 *
 */
public class MockCustomer extends Mock implements Customer {
	
	public Cashier cashier;
	public EventLog log;

	public MockCustomer(String name) {
		super(name);
		log = new EventLog();
	}
	
	@Override
	public void msgHereIsTheCheck(double price) {
		log.add(new LoggedEvent("Received msgHereIsTheCheck from cashier. Total = " + price + "."));
	}
	
	@Override
	public void msgPleaseSit(Point pos)
	{
		log.add(new LoggedEvent("Recieved msgPleaseSit."));
	}

	@Override
	public void msgWeAreFull() {
		log.add(new LoggedEvent("Received msgWeAreFull."));
	}

	@Override
	public void msgFollowMeToTable(Waiter waiter) {
		log.add(new LoggedEvent("Received msgFollowMeToTable. Following " + waiter.getName() + "."));
	}

	@Override
	public void msgSitAtTable(Point tablePos, HashMap<String, Double> choices) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWeAreOut(HashMap<String, Double> newChoices) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNoMoreFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourOrder(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		// TODO Auto-generated method stub
		
	}

}
