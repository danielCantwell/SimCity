package Bank;

import java.util.*;

import SimCity.Globals.*;
import SimCity.Base.*;
import Bank.gui.*;
import Bank.interfaces.Customer;
import Bank.interfaces.Teller;

/**
 * Bank Teller Role
 * @author Eric
 */

public class tellerRole extends Role implements Teller {
	//-----------------------------------------------Data-------------------------------------------------
	private tellerGui gui = new tellerGui(this);
	public Map<Integer, Money> bankAccs = new HashMap<Integer, Money>();
	public List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());
	public class Client {
		int accountNum;
		Money money;
		Money editmoney;
		state s;
		Customer cust;
	}
	Money empty = new Money(0,0);
	public enum state{ none, ready, added, noAcc, yesAcc, setUp, askedService, called, withdraw, deposit, leaving };
	private state s = state.none;


	//-----------------------------------------------Messages-------------------------------------------------
	@Override
	public void enterBuilding() {
		s = state.ready;
		System.out.println("Teller: I am a teller");
		stateChanged();
	}

	@Override
	public void tellerAssigned(Customer c) {
		Client cl = new Client();
		cl.cust = c;
		cl.s = state.added;
		clients.add(cl);
		System.out.println("Teller: New customer assigned to me");
		stateChanged();

	}

	@Override
	public void foundTeller(int accNum, Money money, Customer cust) {
		Client c = new Client();
		c.accountNum = accNum;
		c.money = money;
		c.cust = cust;
		clients.add(c);
		if(bankAccs.get(c.accountNum) != null) {
			c.s = state.yesAcc;
		} 
		else {
			c.s = state.noAcc;
		}
		System.out.println("Teller: Customer has come to me accNum: "+accNum+" "+c.s);
		stateChanged();
	}

	@Override
	public void requestWithdraw(int acc, Money money) {
		for (Client c : clients) {
			if (c.accountNum == acc) {
				c.s = state.withdraw;
				c.editmoney = money;
				System.out.println("Teller: Customer requested "+c.s+": $"+c.editmoney.getDollar());
				stateChanged();
			}
		}
	}

	@Override
	public void requestDeposit(int acc, Money money) {
		for (Client c : clients) {
			if (c.accountNum == acc) {
				c.editmoney = money;
				c.s = state.deposit;
				System.out.println("Teller: Customer requested "+c.s+": $"+c.editmoney.getDollar());
			}	
		}
		stateChanged();
	}

	@Override
	public void workOver() {
		s = state.leaving;
	}

	//-----------------------------------------------Scheduler-------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		if(s == state.ready) {
			goToCounter();
			return true;
		}
		if(s == state.leaving) {
			leaveBank();
			return true;
		}
		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.added) {
					callClient(c);
					return true;
				}
			}
		}
		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.yesAcc) {
					askService(c);
					return true;
				}
				else if(c.s == state.noAcc){
					accSetUp(c);
					return true;
				}
			}
		}
		synchronized(clients) {
			for (Client c : clients) {
				if(c.s == state.withdraw) {
					withdrawDone(c);
					return true;
				}
				else if (c.s == state.deposit) {
					depositDone(c);
					return true;
				}
			}
		}
		return false;
	}

	//-----------------------------------------------Actions-------------------------------------------------
	public void callClient(Client c) {
		c.cust.tellerCalled(this);
		c.s = state.called;
	}

	public void askService(Client c){
		c.cust.whatService();
		c.s = state.askedService;
	}

	public void accSetUp(Client c) {
		System.out.println("Teller: no existing account, creating...");
		bankAccs.put(c.accountNum, empty);
		c.s = state.yesAcc;
	}
	
	public void withdrawDone(Client c) {
		if( ! (c.editmoney.isGreaterThan(bankAccs.get(c.accountNum)))) {
			c.money = c.money.add(c.editmoney);
			Money temp = bankAccs.get(c.accountNum);
			bankAccs.put(c.accountNum, temp.subtract(c.editmoney));
			c.cust.transactionComplete(c.money);
			System.out.println("Customer current total: $"+c.money.dollars);
			clients.remove(c);
		}
		else System.out.println("Teller: Insufficient funds");
	}

	public void depositDone(Client c) {
		//System.out.println(c.money.getDollar());
		c.money = c.money.subtract(c.editmoney);
		//System.out.println("###"+c.money.getDollar());
		Money temp = bankAccs.get(c.accountNum);
		bankAccs.put(c.accountNum, temp.add(c.editmoney));
		c.cust.transactionComplete(c.money);
		System.out.println("Customer current total: $"+c.money.dollars);
		//System.out.println(bankAccs.get(1).getDollar());
		clients.remove(c);
	}

	@Override
	public void goToCounter() {
		//Make GUI call to walk to the counter
	}

	@Override
	public void leaveBank() {
		exitBuilding(myPerson);
		//Make GUI call to leave the bank
	}

	@Override
	public void setGui(tellerGui gui) {
		this.gui = gui;
	}

	@Override
	public tellerGui getGui() { 
		return gui;
	}

}
