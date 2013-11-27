package jesseRest;

import jesseRest.JesseCustomer;
import jesseRest.interfaces.Customer;

public class Check {
	public Customer customer;
	public double amount;
	public enum CheckTransit {None, ToWaiter, ToCustomer, ToCashier};
	public CheckTransit state;
	
	public Check(Customer c, double a) {
		customer = c;
		amount = a;
		state = CheckTransit.None;
	}
}