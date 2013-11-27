package jesseRest.interfaces;

import jesseRest.JesseCustomer;
import jesseRest.JesseWaiter;
import restaurant.test.mock.EventLog;
import jesseRest.Check;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {
	public abstract void msgComputeCheck(String choice, Customer c, Waiter w);
}