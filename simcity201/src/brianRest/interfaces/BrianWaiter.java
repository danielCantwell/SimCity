package brianRest.interfaces;

import brianRest.BrianTable;
public interface BrianWaiter {

	//Wants a break
	public abstract void msgWantABreak();

	public abstract void msgCanGoOnBreak();

	// ######## Messages ################
	public abstract void msgSeatAtTable(BrianCustomer c, BrianTable t);

	public abstract void msgReadyToOrder(BrianCustomer c);

	public abstract void msgHeresMyChoice(BrianCustomer ca, String c);

	public abstract void msgOutOfFood(String choice, int table);

	public abstract void msgOrderIsReady(String o, int tableNumber);

	public abstract void msgImDone(BrianCustomer c);

	//Having to do with the check.
	public abstract void msgRequestCheck(BrianCustomer c);

	public abstract void msgHereIsCheck(double totalCost, BrianCustomer c);

	public abstract void msgCleanUpDeadCustomer(BrianCustomer customer);

}