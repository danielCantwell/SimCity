package EricRestaurant;

import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;
import SimCity.Base.Role;
import SimCity.Globals.Money;

public abstract class EricAbstractWaiter extends Role implements Waiter {

	public int menu = 4;

	protected abstract void gotOrder(myCustomer c);
	public enum state {nothing, hostcalled, ready, asked, waiting, pickingup, gotOrder, gavecook, gotFromCook, finished, leaving, gotcheck};

	protected class myCustomer {
		Waiter w;
		int wNum;
		Customer c;
		Money money;
		int table;
		String choice;
		state s;
	}
}
