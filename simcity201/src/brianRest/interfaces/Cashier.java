package restaurant.interfaces;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

public interface Cashier {

	//########## Messages  ###############
	public abstract void msgHereIsCheck(String choice, Customer c,
			Waiter wa);

	public abstract void msgHeresIsMyMoney(Customer c, double totalMoney);

}