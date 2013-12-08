package EricRestaurant;
import java.text.DecimalFormat;

import EricRestaurant.interfaces.Cashier;
import EricRestaurant.interfaces.Customer;
import EricRestaurant.interfaces.Waiter;
import SimCity.Base.Role;
import agent.Agent;

import java.util.*;

import restaurant.test.mock.EventLog;


public class EricCashier extends Role {
	static double cashMoney = 50;
	static double bank = 100;
	public EventLog log = new EventLog();
	public List <Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public class Check {
		double price;
		Customer c;
		Waiter w;
		state s;
		double change;
	}
	public List <Bills> bill = Collections.synchronizedList(new ArrayList<Bills>());
	public class Bills {
		double price;
		//Market m;
	}
	boolean bum = false;
	Cashier b;
	//Market mk;
	public enum state{nothing, prepared, delivered, gavemoney, givechange, bumpay};
	private state s = state.nothing;
	DecimalFormat df = new DecimalFormat("#0.00");

	//Messages
	public void askCheck(Customer c, String choice, Waiter w) {
		if(c.getName().equals("bum")){
			Check mycheck = new Check();
			mycheck.s = state.prepared;
			mycheck.c = c;
			mycheck.w = w;
			mycheck.price = 0;
			System.out.println("Cashier: Bum doesn't have enough, pay next time.");
			checks.add(mycheck);
		}
		else {
			Check mycheck = new Check();
			mycheck.s = state.prepared;
			System.out.println("Cashier: Received request for check from waiter");
			if(choice == "Chicken") { mycheck.price = 10.99;}
			if(choice == "Steak") { mycheck.price = 15.99;}
			if(choice == "Pizza") { mycheck.price = 8.99;}
			if(choice == "Salad") { mycheck.price = 5.99;}
			mycheck.c = c;
			mycheck.w = w;
			checks.add(mycheck);
		}
		stateChanged();
	}

	public void hereIsPay(double p, Customer c) {
		System.out.println("Cashier: Recieved $"+df.format(p)+" from "+c.getName());
		synchronized(checks) {
			for(Check ck : checks) {
				if(ck.c == c ) {
					ck.change = p - ck.price;
					cashMoney = cashMoney + ck.price;
					System.out.println("Cashier: gave $"+df.format(ck.change)+" to customer "+c+" and Cashier now has $"+df.format(cashMoney));
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
		if(bum == true) {
			bumChange();
			bum = false;
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
	public void bumChange() {
		System.out.print("Cashier: took 8.99 for the pizza");
		b.bumChange(11.01);
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

	@Override
	protected void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
		exitBuilding(myPerson);
	}
}
