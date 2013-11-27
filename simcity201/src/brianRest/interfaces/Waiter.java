package restaurant.interfaces;

import restaurant.CustomerAgent;
import restaurant.Table;

public interface Waiter {

	//Wants a break
	public abstract void msgWantABreak();

	public abstract void msgCanGoOnBreak();

	// ######## Messages ################
	public abstract void msgSeatAtTable(Customer c, Table t);

	public abstract void msgReadyToOrder(Customer c);

	public abstract void msgHeresMyChoice(Customer ca, String c);

	public abstract void msgOutOfFood(String choice, int table);

	public abstract void msgOrderIsReady(String o, int tableNumber);

	public abstract void msgImDone(Customer c);

	//Having to do with the check.
	public abstract void msgRequestCheck(Customer c);

	public abstract void msgHereIsCheck(double totalCost, Customer c);

	public abstract void msgCleanUpDeadCustomer(Customer customer);

}