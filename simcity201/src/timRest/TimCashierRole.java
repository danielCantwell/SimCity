package timRest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import timRest.interfaces.TimCashier;
import timRest.interfaces.TimCustomer;
import timRest.interfaces.TimWaiter;
import SimCity.Base.Role;

public class TimCashierRole extends Role implements TimCashier{
	
	Timer timer = new Timer();

	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public HashMap<String, Double> priceMap = new HashMap<String, Double>();
	
	private double cashInRegister;
	
	public enum AgentState { idle, calculating };
	public AgentState state = AgentState.idle;

	public List<Bill> billsToPay = Collections.synchronizedList(new ArrayList<Bill>());
	
	public enum CheckState { pending, calculating, ready }
	
	public TimCashierRole()
	{
		super();
		cashInRegister = 0.0d;
	}
	
	public void msgHereIsACheck(TimWaiter waiter, String choice, int tableNumber)
	{
		Check check = new Check(waiter, choice, tableNumber);
		checks.add(check);
		print("Recieved check from table " + tableNumber + ".");
		stateChanged();
	}
	
	public void msgWantCheck(int tableNumber)
	{
		synchronized(checks)
		{
			for (Check check : checks)
			{
				if (check.tableNumber == tableNumber)
				{
					check.state = CheckState.ready;
					stateChanged();
					return;
				}
			}
		}
	}

	public void msgHereIsTheMoney(double cash)
	{
		// good
		cashInRegister += cash;
		Do("Thank you, come again.");
		stateChanged();
	}
	
	public void msgHereIsPartialMoney(double cash, double amountOwed)
	{
		cashInRegister += cash;
		Do("You owe me money next time.");
		stateChanged();
	}
	
//	public void msgHereIsTheBill(Market market, double price)
//	{
//		billsToPay.add(new Bill(market, price));
//		stateChanged();
//	}
	
	@Override
	public boolean pickAndExecuteAnAction()
	{
		if (state == AgentState.idle)
		{
			synchronized(billsToPay)
			{
				for (Bill bill : billsToPay)
				{
					if (bill.price <= cashInRegister)
					{
						//payMarket(bill);
						billsToPay.remove(bill);
						return true;
					}
					else if (!bill.interest)
					{
						Do("Cannot pay bill right now. I will pay 10% interest when I do pay.");
						bill.interest = true;
						bill.price *= 1.10;
					}
				}
			}
			synchronized(checks)
			{
				for (Check check : checks)
				{
					if (check.state == CheckState.ready)
					{
						giveCheckToWaiter(check);
						return true;
					}
					else if (check.state ==  CheckState.pending)
					{
						Do("Calculating " + check.choice + ".");
						state = AgentState.calculating;
						calculate(check);
						return true;
					}
				}
			}
		}
		
		return false;
	}

	// actions
	private void calculate(Check check)
	{
		check.state = CheckState.calculating;
		TimerTask t = new TimerTask() {
			Check check;
			public void run() {
				state = AgentState.idle;
				check.state = CheckState.ready;
				//isHungry = false;
				stateChanged();
			}
			public TimerTask init(Check o)
			{
				check = o;
				return this;
			}
		}.init(check);
		timer.schedule(t, 5000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void giveCheckToWaiter(Check check)
	{
		check.waiter.msgHereIsACheck(check.tableNumber, priceMap.get(check.choice));
		checks.remove(check);
	}
	
//	private void payMarket(Bill bill)
//	{
//		Do("Paying $" + bill.price + " to " + bill.market.getName() + ".");
//		bill.market.msgHereIsPayment(bill.price);
//		cashInRegister -= bill.price;
//	}
	
	public void addItemToMenu(String choice, double price)
	{
		priceMap.put(choice, price);
		stateChanged();
	}
	
	public double getCashInRegister()
	{
		return cashInRegister;
	}
	
	public String getName() {
		return myPerson.name;
	}
	
	public class Check
	{
		public String choice;
		public int tableNumber;
		public TimWaiter waiter;
		public CheckState state;
		
		public Check(TimWaiter waiter, String choice, int tableNumber)
		{
			this.waiter = waiter;
			this.choice = choice;
			this.tableNumber = tableNumber;
			state = CheckState.pending;
		}
	}
	
	public class Bill
	{
		//public Market market;
		public double price;
		public boolean interest;
		
//		public Bill(Market market, double price)
//		{
//			this.market = market;
//			this.price = price;
//			interest = false;
//		}
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
		// TODO Auto-generated method stub
		
	}

}
