package brianRest.interfaces;

import java.util.ArrayList;

import brianRest.BrianTable;
import restaurant.interfaces.Waiter;

public interface BrianHost {



	//Waiter wants a break.
	public abstract void msgWaiterWantsABreak(BrianWaiter waiter);

	public abstract void msgWaiterOffBreak(BrianWaiter waiter);

	public abstract void msgIWantToEat(BrianCustomer c);

	public abstract void msgTableIsClear(BrianTable t);

	public abstract void msgLeavingEarly(BrianCustomer c);

}