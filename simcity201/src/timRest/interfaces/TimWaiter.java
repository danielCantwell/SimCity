package timRest.interfaces;

import java.awt.Point;

import SimCity.Globals.Money;

public interface TimWaiter {
	public abstract void msgSeatAtTable(TimCustomer customer, int tableNumber, Point tablePos);
	public abstract void msgImReadyToOrder(TimCustomer customer);
	public abstract void msgIWouldLike(TimCustomer customer, String choice);
	public abstract void msgCannotCookItem(int tableNumber);
	public abstract void msgOutOfFood(int tableNumber);
	public abstract void msgOrderIsReady(int tableNumber, String choice);
	public abstract void msgCanIHaveCheck(TimCustomer customer);
	public abstract void msgHereIsACheck(int tableNumber, Money price);
	public abstract void msgLeavingTable(TimCustomer customer);
	public abstract void msgGoOnBreak();
	public abstract void msgCannotBreak();
	public abstract void msgGoOffBreak();
	public abstract void msgSetWantBreak(boolean wantsBreak);
	public abstract void msgAnimationFinishedGoToTable();
	public abstract void msgAnimationFinishedGoToHost();
	public abstract void msgAnimationFinishedGoToCashier();
	public abstract void msgAnimationFinishedGoToCook();
	
	public abstract String getName();
}
