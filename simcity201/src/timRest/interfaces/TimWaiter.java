package restaurant.interfaces;

import java.awt.Point;

public interface Waiter {
	public abstract void msgSeatAtTable(Customer customer, int tableNumber, Point tablePos);
	public abstract void msgImReadyToOrder(Customer customer);
	public abstract void msgIWouldLike(Customer customer, String choice);
	public abstract void msgCannotCookItem(int tableNumber);
	public abstract void msgOutOfFood(int tableNumber);
	public abstract void msgOrderIsReady(int tableNumber, String choice);
	public abstract void msgCanIHaveCheck(Customer customer);
	public abstract void msgHereIsACheck(int tableNumber, double price);
	public abstract void msgLeavingTable(Customer customer);
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
