package EricRestaurant;
import java.text.DecimalFormat;

import EricRestaurant.interfaces.Cashier;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import agent.Agent;

import java.util.*;

import restaurant.test.mock.EventLog;


public class EricCashier extends Role {
	Money ERestMoney;
	public EventLog log = new EventLog();
	public List <Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public class Check {
		Money price;
		Customer c;
		Waiter w;
		state s;
		Money change;
	}
	public List <Bills> bill = Collections.synchronizedList(new ArrayList<Bills>());
	public class Bills {
		double price;
		//Market m;
		
	}
	boolean bum = false;
	Cashier b;
	EricHost host;
	//Market mk;
	public enum state{nothing, prepared, delivered, gavemoney, givechange, bumpay};
	private state s = state.nothing;
	DecimalFormat df = new DecimalFormat("#0.00");

	//Messages
	public void setHost(EricHost h) {
		host = h;
	}
	public void askCheck(Customer c, String choice, Waiter w) {
//		if(c.getName().equals("bum")){
//			Check mycheck = new Check();
//			mycheck.s = state.prepared;
//			mycheck.c = c;
//			mycheck.w = w;
//			mycheck.price = 0;
//			System.out.println("Cashier: Bum doesn't have enough, pay next time.");
//			checks.add(mycheck);
//		}
		
			Check mycheck = new Check();
			mycheck.s = state.prepared;
			System.out.println("Cashier: Received request for check from waiter");
			if(choice == "Chicken") { mycheck.price = new Money(11,0);}
			if(choice == "Steak") { mycheck.price = new Money(16,0);}
			if(choice == "Pizza") { mycheck.price = new Money(9,0);}
			if(choice == "Salad") { mycheck.price = new Money(6,0);}
			mycheck.c = c;
			mycheck.w = w;
			checks.add(mycheck);
		
		stateChanged();
	}

	public void hereIsPay(Money p, Customer c) {
		System.out.println("Cashier: Recieved $"+p.getDollar()+" from "+c.getName());
		synchronized(checks) {
			for(Check ck : checks) {
				if(ck.c == c ) {
					ck.change = p.subtract(ck.price);
					ERestMoney.add(ck.price);
					System.out.println("Cashier: gave $"+ck.change.getDollar()+" to customer "+c+" and Cashier now has $"+ERestMoney.getDollar());
					ck.c.giveChange(ck.change);	

				}
			}
		}
		checks.remove(0);
		stateChanged();
	}
	public void bumPays(double c, Cashier cust) {
		bum = true;
		b = cust;
		stateChanged();
	}


//	public void marketBill(double money, Market m) {
//		Bills newB = new Bills();
//		newB.price = money;
//		newB.m = m;
//		bill.add(newB);
//		stateChanged();
//	}

	//Scheduler

	public boolean pickAndExecuteAnAction() {
//		synchronized(bill){
//			for(Bills b : bill) {
//				payMarket(bill.get(0));
//				return true;
//			}
//		}
		//System.out.println("check size "+checks.size()+" and myPerson is this: "+myPerson);
		synchronized(checks){
			for( Check ck : checks) {
				if(ck.s == state.prepared) {
					checkToWaiter(ck);
				}
				return true;
			}
		}
		
		return false;
	}



	//Actions
	public void checkToWaiter(Check ck) {
		System.out.println("Cashier: Giving check to waiter");
		ck.w.waiterGotCheck(ck.price, ck.c, this);
		ck.s = state.delivered;
		stateChanged();
	}
	
//	public void payMarket(Bills b) {
//		if (b.price > cashMoney) {
//			bank = bank - b.price + cashMoney;
//			System.out.println("Cashier: paid the Market $"+cashMoney);
//			System.out.println("Cashier: Borrowed another $"+df.format((b.price - cashMoney))+" from the Bank");
//			cashMoney = 0;
//			b.m.cashierandBankPaid(b.price);
//			bill.remove(b);
//			stateChanged();
//		}
//		else {
//			cashMoney = cashMoney - b.price;
//			System.out.println("Cashier: paid the Market $"+b.price);
//			b.m.cashierPaid(b.price);
//			bill.remove(b);
//			stateChanged();
//		}
//	}

	public void setMoney(Money m) {
		ERestMoney = m;
	}
	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
		host.setMoney(ERestMoney);
		exitBuilding(myPerson);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ECsr";
	}
}
