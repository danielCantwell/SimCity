package jesseRest.interfaces;

import jesseRest.CustomerAgent;
import jesseRest.WaiterAgent;
import restaurant.test.mock.EventLog;
import agent.Check;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {
	public abstract void msgComputeCheck(String choice, Customer c, Waiter w);
}