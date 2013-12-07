package timRest.interfaces;

import java.awt.Point;
import java.util.HashMap;

import timRest.interfaces.TimCashier;
import SimCity.Globals.Money;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Timothy So
 *
 */
public interface TimCustomer {
	
	public abstract void msgPleaseSit(Point pos);
	public abstract void msgWeAreFull();
	public abstract void msgFollowMeToTable(TimWaiter waiter);
	public abstract void msgSitAtTable(Point tablePos, HashMap<String, Money> choices);
	public abstract void msgWhatWouldYouLike();
	public abstract void msgWeAreOut(HashMap<String, Money> newChoices);
	public abstract void msgNoMoreFood();
	public abstract void msgHereIsYourOrder(String choice);
	public abstract void msgHereIsTheCheck(TimCashier cashier, Money price);
	public abstract void msgAnimationFinishedGoToSeat();
	public abstract void msgAnimationFinishedLeaveRestaurant();
	public abstract void msgAnimationFinishedGoToCashier();
	
	public abstract String getName();
}