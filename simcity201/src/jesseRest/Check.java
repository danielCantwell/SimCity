package jesseRest;

import SimCity.Globals.Money;
import jesseRest.JesseCustomer;
import jesseRest.interfaces.Customer;

public class Check {
	public Customer customer;
	public Money amount;
	public enum CheckTransit {None, ToWaiter, ToCustomer, ToCashier};
	public CheckTransit state;
	
	public Check(Customer c, Money a) {
		customer = c;
		amount = a;
		state = CheckTransit.None;
	}
}