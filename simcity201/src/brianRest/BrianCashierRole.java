package brianRest;

import SimCity.Base.Role;
import SimCity.Buildings.B_BrianRestaurant;
import agent.Agent;


import brianRest.interfaces.BrianCashier;
import brianRest.interfaces.BrianCustomer;
import brianRest.interfaces.BrianMarket;
import brianRest.interfaces.BrianWaiter;

import java.util.*;

public class BrianCashierRole extends Role implements BrianCashier {
	
	String name;
	
	private int money = Integer.MAX_VALUE;
	
	public List<Check> checks;
	public BrianMenu menu;
	public enum CheckStatus {pending, calculated, paid};
	public enum CheckType {restaurant, market};

	//Constructor
	public BrianCashierRole(String name){
		checks =  Collections.synchronizedList(new ArrayList<Check>());
		menu = new BrianMenu();
		this.name = name;
	}
		
//########## Messages  ###############
	public void msgHereIsCheck(String choice, BrianCustomer c, BrianWaiter wa){
		Check ch = new Check(choice, c, wa);
		checks.add(ch);
		stateChanged();
	}
	
	@Override
	public void msgHeresIsMyMoney(BrianCustomer c, double totalMoney){
		synchronized(checks){
			for (Check ch: checks){
				//if (ch.customer == c){
					ch.state = CheckStatus.paid;
					ch.customerPayment = totalMoney;
					stateChanged();
					return;
				//}
			}
		}
	}
	
	public void msgHereIsMarketCost(double cost, BrianMarket m){
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
		return false;
	}
		
//########## Actions ###############
	public void CalculateCheck(Check c){
		Do("Calculating Check");
		c.state = CheckStatus.calculated;
		c.totalCost = menu.getPrice(c.choice);
		//c.waiter.msgHereIsCheck(c.totalCost, c.customer);
	}
	
	public void CheckIsPaid(Check c){
		if (c.customerPayment - c.totalCost < 0){
			//c.waiter.msgCleanUpDeadCustomer(c.customer);
			//c.customer.msgDie();
			checks.remove(c);
			return;
		}
		
		Do("Here is your change: $" + (c.customerPayment-c.totalCost));
		//c.customer.msgHeresYourChange(c.customerPayment - c.totalCost);
		checks.remove(c);
	}
	
	public void PayMarket(Check c){
		Do("Paying "+c.market.name+ " market a total of "+ "$" + c.totalCost);
		c.market.msgPayMarket(c.totalCost);
		money -= c.totalCost;
		checks.remove(c);
	}
	
//################    Utility     ##################
	public String toString(){
		return "Cashier " + name;
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
		  BrianMarket market;
		  
		  //Restaurant Check
		  public Check(String choice, BrianCustomer c, BrianWaiter w){
			  this.choice = choice;
			  customer = c;
			  waiter = w;
		  }
		  
		  //Market Check
		  public Check (double cost, BrianMarket m){
			  totalCost = cost;
			  type = CheckType.market;
			  market = m;
		  }
	}
	@Override
	protected void enterBuilding() {
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		B_BrianRestaurant rest = (B_BrianRestaurant)myPerson.getBuilding();
		rest.cashierFilled = false;
	}

}



