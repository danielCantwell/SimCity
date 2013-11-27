package restaurant.interfaces;

import restaurant.CustomerAgent;
import restaurant.Table;

public interface Host {

	//Waiter wants a break.
	public abstract void msgWaiterWantsABreak(Waiter waiter);

	public abstract void msgWaiterOffBreak(Waiter waiter);

	public abstract void msgIWantToEat(Customer c);

	public abstract void msgTableIsClear(Table t);

	public abstract void msgLeavingEarly(Customer c);

}