package jesseRest;

import SimCity.Base.Role;
import jesseRest.JesseHost.Table;
import jesseRest.interfaces.Waiter;

public abstract class JesseAbstractWaiter extends Role implements Waiter {
	
	protected abstract void GiveOrder(MyCustomer c);
	public abstract void msgDoneEatingAndPaying(JesseCustomer jesseCustomer);
	public abstract void msgImReadyToOrder(JesseCustomer jesseCustomer);
	public abstract void msgHereIsMyOrder(String choice, JesseCustomer jesseCustomer);
	public abstract void msgOutOfFood(String choice, int table);
	public abstract void msgOrderIsReady(String choice, int table);
	public abstract void msgSitAtTable(JesseCustomer cust, Table table);

	public enum CustomerState {DoingNothing, Waiting, Seated, Ready, AskedToOrder, AskedToReorder, Ordered, GettingFood, Eating, Done};

	protected class MyCustomer {
		JesseCustomer customer;
		Table table;
		String choice = "";
		Check check;
		CustomerState state = CustomerState.Waiting;
		
		public MyCustomer(JesseCustomer c, Table t) {
			customer = c;
			table = t;
		}
	}





}
