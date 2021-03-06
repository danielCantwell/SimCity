package timRest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import market.MarketManagerRole;
import market.interfaces.MarketDeliveryCashier;
import timRest.interfaces.TimCashier;
import timRest.interfaces.TimCustomer;
import timRest.interfaces.TimWaiter;
import SimCity.Base.God;
import SimCity.Base.Role;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;

public class TimCashierRole extends Role implements TimCashier, MarketDeliveryCashier{
	
	Timer timer = new Timer();

	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public HashMap<String, Money> priceMap = new HashMap<String, Money>();
	
	private Money cashInRegister;
	private boolean canLeave = false;
	
	public enum AgentState { idle, calculating };
	public AgentState state = AgentState.idle;

	public List<Bill> billsToPay = Collections.synchronizedList(new ArrayList<Bill>());
	
	public enum CheckState { pending, calculating, ready, requested }
	
	public TimCashierRole()
	{
		super();
		cashInRegister = new Money(0, 0);
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
					check.state = CheckState.requested;
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgPayMarket(int amount, Money pricePerUnit, MarketManagerRole manager)
	{
	    Do(AlertTag.TimRest, "Recieved bill.");
	    // multiply amount by pricePerUnit
	    Money money = new Money(0, 0);
	    for (int i = 0; i < amount; i++)
	    {
	        money.add(pricePerUnit);
	    }
	    billsToPay.add(new Bill(manager, money));
	    stateChanged();
	}

	public void msgHereIsTheMoney(Money cash)
	{
		// good
		cashInRegister.add(cash);
		Do(AlertTag.TimRest, "Thank you, come again.");
		stateChanged();
	}
	
	public void msgHereIsPartialMoney(Money cash, Money amountOwed)
	{
		cashInRegister.add(cash);
		Do(AlertTag.TimRest, "You owe me money next time.");
		stateChanged();
	}
	
	public void msgLeaveWork()
	{
	    canLeave = true;
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
					if (!bill.price.isGreaterThan(cashInRegister))
					{
						payMarket(bill);
						billsToPay.remove(bill);
						return true;
					}
					else if (!bill.interest)
					{
						Do(AlertTag.TimRest, "Cannot pay bill right now. I will pay $5 extra when I do pay.");
						bill.interest = true;
						bill.price.add(5, 0);
					}
				}
			}
			synchronized(checks)
			{
				for (Check check : checks)
				{
					if (check.state == CheckState.requested)
					{
						giveCheckToWaiter(check);
						return true;
					}
					else if (check.state ==  CheckState.pending)
					{
						Do(AlertTag.TimRest, "Calculating " + check.choice + ".");
						state = AgentState.calculating;
						calculate(check);
						return true;
					}
				}
			}
		}

        if (canLeave && billsToPay.isEmpty())
        {
            leaveBuilding();
            return false;
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
				Do(AlertTag.TimRest, "Check Ready");
				//isHungry = false;
				stateChanged();
			}
			public TimerTask init(Check o)
			{
				check = o;
				return this;
			}
		}.init(check);
		timer.schedule(t, 2000);//getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void giveCheckToWaiter(Check check)
	{
		check.waiter.msgHereIsACheck(check.tableNumber, priceMap.get(check.choice));
		checks.remove(check);
	}
	
	private void payMarket(Bill bill)
	{;
		Do(AlertTag.TimRest, "Paying " + bill.price + " to " + bill.manager.myPerson.getName() + ".");
		bill.manager.msgHereIsTheMoney(bill.price);
		cashInRegister.subtract(bill.price);
	}
    
    private void leaveBuilding()
    {
        myPerson.money.add(new Money(75, 00));
        Info(AlertTag.TimRest, "I have " + myPerson.money + " and I'm leaving the building.");
        canLeave = false;
        exitBuilding(myPerson);
        myPerson.msgGoHome();
    }
	
	public void addItemToMenu(String choice, Money price)
	{
		priceMap.put(choice, price);
		stateChanged();
	}
	
	public Money getCashInRegister()
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
		public MarketManagerRole manager;
		public Money price;
		public boolean interest;
		
		public Bill(MarketManagerRole manager, Money price)
		{
			this.manager = manager;
			this.price = price;
			interest = false;
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
        myPerson.Do("Closing time.");
        exitBuilding(myPerson);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "TCsr";
	}

	public void setMoney(Money m) {
		cashInRegister = m;
	}
}
