/**
 * 
 */
package restaurant.interfaces;

import restaurant.test.mock.EventLog;

/**
 * @author Daniel
 *
 */
public interface Cashier {
	
	public EventLog log = new EventLog();
	
	public abstract void msgGetBill(Waiter waiter, Customer customer, String choice);
	
	public abstract void msgPayment(Customer customer, double cash);

	void msgHeresIsMyMoney(Customer c, double totalMoney);

}
