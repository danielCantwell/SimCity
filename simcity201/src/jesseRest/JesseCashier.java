package jesseRest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jesseRest.interfaces.Cashier;
import jesseRest.interfaces.Customer;
import jesseRest.interfaces.Waiter;
import market.MarketManagerRole;
import market.interfaces.MarketDeliveryCashier;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;

/**
 * Restaurant Cashier Agent
 */

public class JesseCashier extends Role implements Cashier, MarketDeliveryCashier {
	public List<MyCheck> checks = Collections.synchronizedList(new ArrayList<MyCheck>());
	public List<MyBill> bills = Collections.synchronizedList(new ArrayList<MyBill>());
	public Money money;
	public enum CheckState {Created, Pending, Paid, Closed, Debt};
	private Menu mymenu = new Menu();
	private String name;
	
	public JesseCashier(String name) {
		super();
		this.name = name;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	/**
	 * MESSAGES =====================================================
	 */

	public void msgComputeCheck(String choice, Customer c, Waiter w) {
		Money amt = mymenu.getPrice(choice);
		Check check = new Check(c, amt);
		MyCheck mcheck = new MyCheck(check, w);
		mcheck.state = CheckState.Created;
		checks.add(mcheck);
		stateChanged();
	}
	
	public void msgPaying(Check c, Money amt) {
		synchronized(checks){
			for (MyCheck mc : checks) {
				if (mc.check == c) {
					mc.check.amount.subtract(amt);
					money.add(amt);
					mc.state = CheckState.Paid;
					stateChanged();
				}
			}
		}
	}
	
	/*
	public void msgPayForItems(Market m, Money orderPrice) {
		MyBill bill = new MyBill(m, orderPrice);
		bills.add(bill);
		stateChanged();
	}
	*/
	
	public void msgPayMarket(int amount, Money pricePerUnit, MarketManagerRole manager)
	{
	    Do(AlertTag.JesseRest, "Recieved bill from Market " + manager + " for amount " + amount + ". Verifying check before payment.");
	    // multiply amount by pricePerUnit
	    Money money = new Money(0, 0);
	    for (int i = 0; i < amount; i++)
	    {
	        money.add(pricePerUnit);
	    }
	    bills.add(new MyBill(manager, money));
	    stateChanged();
	}

	/**
	 * SCHEDULER ====================================================
	 */
	
	public boolean pickAndExecuteAnAction() {
		synchronized(checks){
			for (MyCheck c : checks) {
				if (c.state == CheckState.Created) {
					GiveCheckToWaiter(c);
					return true;
				}
			}
		}
		synchronized(checks){
			for (MyCheck c : checks) {
				if (c.state == CheckState.Paid) {
					ComputePayment(c);
					return true;
				}
			}
		}
		synchronized(bills){
			for (MyBill b : bills) {
				if (!b.amountOwed.isGreaterThan(money)) {
					PayMarket(b);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * ACTIONS  ====================================================
	 */

	private void GiveCheckToWaiter(MyCheck c) {
		print("Message: Sending HereIsCheck from Cashier to Waiter");
		c.waiter.msgHereIsCheck(c.check);
		c.state = CheckState.Pending;
	}
	
	private void ComputePayment(MyCheck c) {
		if (!c.check.amount.isGreaterThan(0, 0)) {
			c.state = CheckState.Closed;
		} else {
			c.state = CheckState.Debt;
		}
		c.check.customer.msgHereIsCustomerCopyCheck(c.check); 
	}
	
	private void PayMarket(MyBill b) {
		bills.remove(b);
		money.subtract(b.amountOwed);
	    Do(AlertTag.JesseRest, "Check verified. Paying market. Money left: " + money + ".");
		b.market.msgHereIsTheMoney(b.amountOwed);
	}
	
	/**
	 * UTILITIES  ===================================================
	 */
	
	// MyCheck Class - represents a customer's payment status
	public class MyCheck {
		public Check check;
		Waiter waiter;
		public CheckState state;
		
		public MyCheck(Check c, Waiter w) {
			check = c;
			waiter = w;
			state = CheckState.Created;
		}
	}
	
	// MyBill Class - represents a payment due to Market. Especially useful if you dont have enough cash at hand.
	public class MyBill {
		MarketManagerRole market;
		public Money amountOwed;
		
		public MyBill(MarketManagerRole m, Money i) {
			market = m;
			amountOwed = i;
		}
	}
	public void print(String string) {
		System.out.println(string);
	}

	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
		myPerson.getMoney().add(50, 0);
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "JCsr";
	}

	public void setMoney(Money money2) {
		money = money2;
	}
}

