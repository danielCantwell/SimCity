package jesseRest.interfaces;

import SimCity.Globals.Money;
import jesseRest.Check;

/**
 * A sample Market interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Market {
	public abstract void msgHereIsPayment(Money amountOwed);
}