package brianRest;

import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import SimCity.Buildings.B_Market;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
import agent.Agent;


import brianRest.interfaces.BrianCashier;
import brianRest.interfaces.BrianCustomer;
import brianRest.interfaces.BrianMarket;
import brianRest.interfaces.BrianWaiter;

import java.util.*;

import market.MarketManagerRole;
import market.interfaces.MarketDeliveryCashier;
import market.interfaces.MarketDeliveryCook;

public class BrianCashierRole extends Role implements BrianCashier, MarketDeliveryCashier {
	
	String name;
	
	private int money;
	
	public List<Check> checks;
	public BrianMenu menu;
	public enum CheckStatus {pending, calculated, paid};
	public enum CheckType {restaurant, market};
	boolean wantToGoHome = false;

	//Constructor
	public BrianCashierRole(String name){
		checks =  Collections.synchronizedList(new ArrayList<Check>());
		menu = new BrianMenu();
		this.name = name;
	}
		
//########## Messages  ###############
	//Leave restaurant
		public void msgLeaveRestaurant(){
			wantToGoHome = true;
			stateChanged();
		}
	
	
	public void msgHereIsCheck(String choice, BrianCustomer c, BrianWaiter wa){
		Check ch = new Check(choice, c, wa);
		checks.add(ch);
		stateChanged();
	}
	
	@Override
	public void msgHeresIsMyMoney(BrianCustomer c, double totalMoney){
		synchronized(checks){
			for (Check ch: checks){
				if (ch.customer == c){
					ch.state = CheckStatus.paid;
					ch.customerPayment = totalMoney;
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgHereIsMarketCost(double cost, MarketManagerRole m){
		Check ch = new Check(cost, m);
		checks.add(ch);	
		stateChanged();
	}
	
//##########  Scheduler  ##############
@Override
public boolean pickAndExecuteAnAction() {
		// if there exists an Order o in pendingOrder such that o.OrderState == pending
		//then CookOrder(o);
	
	synchronized (checks){
		for(Check ch: checks){
			if (ch.type == CheckType.restaurant && ch.state == CheckStatus.pending){
				CalculateCheck(ch);
				return true;
			}
		}
	}
	synchronized (checks){
		for (Check ch: checks){
			if (ch.type == CheckType.restaurant && ch.state == CheckStatus.paid){
				CheckIsPaid(ch);
				return true;
			}
		}
	}
	synchronized (checks){
		for (Check ch: checks){
			if (ch.type == CheckType.market && ch.state == CheckStatus.pending){
				PayMarket(ch);
				return true;
			}
		
		}
	}
	
	if (wantToGoHome){
		leaveRestaurant();
	}
	
		return false;
	}
		
//########## Actions ###############
	public void CalculateCheck(Check c){
		Do(AlertTag.BrianRest, "Calculating Check");
		c.state = CheckStatus.calculated;
		c.totalCost = menu.getPrice(c.choice);
		c.waiter.msgHereIsCheck(c.totalCost, c.customer);
	}
	
	public void CheckIsPaid(Check c){
		if (c.customerPayment - c.totalCost < 0){
			c.waiter.msgCleanUpDeadCustomer(c.customer);
			c.customer.msgDie();
			checks.remove(c);
			return;
		}
		
		Do(AlertTag.BrianRest, "Here is your change: $" + (c.customerPayment-c.totalCost));
		c.customer.msgHeresYourChange(c.customerPayment - c.totalCost);
		checks.remove(c);
	}
	
	public void PayMarket(Check c){
		Do(AlertTag.BrianRest, "Paying the market a total of "+ "$" + c.totalCost);
		c.market.msgHereIsTheMoney(new Money((int)c.totalCost, (int)((c.totalCost - (int)c.totalCost)*100)));
		money -= c.totalCost;
		checks.remove(c);
	}
	
	public void leaveRestaurant(){
		Do(AlertTag.BrianRest, "Brian Cashier is leaving restaurant.");
		B_BrianRestaurant br = (B_BrianRestaurant)myPerson.getBuilding();
		br.cashierFilled = false;
		myPerson.msgGoHome();
		exitBuilding(myPerson);
		wantToGoHome = false;
	}
	
//################    Utility     ##################
	public String toString(){
		return "RCsr";
	}

//######################## End of Class Cook#############################
	
	//#### Inner Class ####	
	public class Check {
		  String choice;
		  public double totalCost;
		  double customerPayment;
		  BrianCustomer customer;
		  BrianWaiter waiter;
		  public CheckStatus state = CheckStatus.pending;
		  
		  //For market
		  public CheckType type = CheckType.restaurant;
		  MarketManagerRole market;
		  
		  //Restaurant Check
		  public Check(String choice, BrianCustomer c, BrianWaiter w){
			  this.choice = choice;
			  customer = c;
			  waiter = w;
		  }
		  
		  //Market Check
		  public Check (double totalCost2, MarketManagerRole m){
			  totalCost = totalCost2;
			  type = CheckType.market;
			  market = m;
		  }
	}
	@Override
	protected void enterBuilding() {
	}

	@Override
	public void workOver() {
	}

	@Override
	public void msgPayMarket(int amount, Money pricePerUnit,
			MarketManagerRole manager) {
		Money totalCost = new Money(0,0);
		for (int i=0; i<amount; i++){
			totalCost = totalCost.add(pricePerUnit);
		}
		Check ch = new Check(totalCost.getDollar() + totalCost.cents / 100.0, manager);
		checks.add(ch);	
		stateChanged();
	}

	public void setMoney(Money money2) {
		money = money2.getDollar();
	}

}



