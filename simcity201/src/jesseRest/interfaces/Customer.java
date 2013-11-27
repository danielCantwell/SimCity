package jesseRest.interfaces;

import agent.Check;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	/**
	 *
	 * Sent by the cashier - gives a receipt to the Customer.
	 */
	public abstract void msgHereIsCustomerCopyCheck(Check c);
	
	public abstract void msgTakeCheck(Check c);
}