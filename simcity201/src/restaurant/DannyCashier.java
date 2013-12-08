/**
 * 
 */
package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import SimCity.Base.Role;
import SimCity.Buildings.B_DannyRestaurant;
import agent.Agent;

/**
 * @author Daniel
 * 
 */
public class DannyCashier extends Role implements Cashier {

	private String name;

	public List<PayingCustomer> payingCustomers = Collections
			.synchronizedList(new ArrayList<PayingCustomer>());
	public List<PayingCustomer> customersWhoDidntPay = Collections
			.synchronizedList(new ArrayList<PayingCustomer>());

	private double steakCost = 16.00;
	private double chickenCost = 11.00;
	private double pizzaCost = 9.00;
	private double saladCost = 6.00;

	Food steak = new Food("Steak", steakCost);
	Food chicken = new Food("Chicken", chickenCost);
	Food pizza = new Food("Pizza", pizzaCost);
	Food salad = new Food("Salad", saladCost);

	public enum State {
		none, Bill, Payment
	};

	public Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());

	public DannyCashier() {
		foods.put("Steak", steak);
		foods.put("Chicken", chicken);
		foods.put("Pizza", pizza);
		foods.put("Salad", salad);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	// Messages

	public void msgGetBill(Waiter waiter, Customer customer, String choice) {
		print("MESSAGE 10 : Waiter -> Cashier : GetBill");
		boolean existingCustomer = false;

		synchronized (payingCustomers) {
			for (PayingCustomer myCustomer : payingCustomers) {
				if (myCustomer.customer == customer) {
					myCustomer.waiter = waiter;
					myCustomer.choice = choice;
					myCustomer.state = State.Bill;
					existingCustomer = true;
					break;
				}
			}
		}
		if (!existingCustomer) {
			payingCustomers.add(new PayingCustomer(waiter, customer, choice));
		}
		stateChanged();
	}

	public void msgPayment(Customer customer, double cash) {
		synchronized (payingCustomers) {
			for (PayingCustomer myCustomer : payingCustomers) {
				if (myCustomer.customer == customer) {
					print("MESSAGE 13A : Customer -> Cashier : Payment");
					myCustomer.state = State.Payment;
					myCustomer.cash = cash;
					break;
				}
			}
		}

		stateChanged();
	}
	

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		synchronized (payingCustomers) {
			for (PayingCustomer myCustomer : payingCustomers) {
				if (myCustomer.state == State.Bill) {
					getBill(myCustomer);
					return true;
				}
			}
		}

		synchronized (payingCustomers) {
			for (PayingCustomer myCustomer : payingCustomers) {
				if (myCustomer.state == State.Payment) {
					calculatePayment(myCustomer);
					return true;
				}
			}
		}

		return false;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	// Actions

	private void getBill(PayingCustomer myCustomer) {
		print("Getting bill for waiter");
		myCustomer.state = State.none;
		double cost = foods.get(myCustomer.choice).cost;
		if (myCustomer.inDebt) {
			cost += myCustomer.debt;
		}
		myCustomer.waiter.msgHereIsBill(myCustomer.customer, cost);
	}

	private void calculatePayment(PayingCustomer myCustomer) {
		print(myCustomer.customer.getCustomerName() + " owes "
				+ (foods.get(myCustomer.choice).cost + myCustomer.debt)
				+ " for " + myCustomer.choice
				+ (myCustomer.inDebt ? " plus the debt" : ""));
		print(myCustomer.customer.getCustomerName() + " pays "
				+ myCustomer.cash);

		myCustomer.state = State.none;
		double change = myCustomer.cash
				- (foods.get(myCustomer.choice).cost + myCustomer.debt);

		if (change < 0) {
			myCustomer.inDebt = true;
			myCustomer.debt = Math.abs(change);
			change = 0;
			print(myCustomer.customer.getCustomerName() + " is in debt : "
					+ myCustomer.debt);
			myCustomer.customer.msgInDebt(myCustomer.debt);
		} else {
			myCustomer.inDebt = false;
			myCustomer.debt = 0;
			myCustomer.customer.msgNotInDebt();
		}
		myCustomer.customer.msgHereIsYourChange(change);
	}
	

	private void print(String string) {
		System.out.println(string);
	}

	public String getName() {
		return name;
	}

	public class PayingCustomer {

		public Waiter waiter;
		public Customer customer;

		public String choice;
		public boolean inDebt = false;
		public double debt = 0;
		public State state = State.Bill;

		public double cash;

		PayingCustomer(Waiter waiter, Customer customer, String choice) {
			this.waiter = waiter;
			this.customer = customer;
			this.choice = choice;
		}
	}

	public class Food {
		public final String type;
		public final double cost;

		Food(String type, double cost) {
			this.type = type;
			this.cost = cost;
		}

	}
	
	
	@Override
	protected void enterBuilding() {
		System.out.println("Cashier enterBuilding");
	}

	@Override
	public void workOver() {
		System.out.println("Cashier workOver");
		B_DannyRestaurant rest = (B_DannyRestaurant)myPerson.getBuilding();
		rest.cashierFilled = false;
		exitBuilding(myPerson);
	}

	@Override
	public void msgHeresIsMyMoney(Customer c, double totalMoney) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
