package Bank;

import java.util.Collections;
import java.util.*;
import agent.Agent;

/*
 * Bank Teller Role
 */

public class tellerRole extends Agent {
	//-----------------------------------------------Data-------------------------------------------------
	Map<Integer, Integer> bankAccs = new HashMap<Integer, Integer>();
	public List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());
	class Client {
		int accountNum;
		int money;
		int editmoney;
		state s;
		bankCustomerRole cust;
	}
	public enum state{ none, added, called, withdraw, deposit, leaving };
	private state s = state.none;
	
	//-----------------------------------------------Messages-------------------------------------------------
	public void tellerAssigned(bankCustomerRole c) {
		Client cl = new Client();
		cl.cust = c;
		cl.s = state.added;
		clients.add(cl);
		stateChanged();
	}
	
	public void foundTeller(int accNum, int money, bankCustomerRole cust) {
		Client c = new Client();
		c.accountNum = accNum;
		c.money = money;
		c.cust = cust;
		clients.add(c);
	}
	
	public void requestWithdraw(int acc, int money) {
		for (Client c : clients) {
			if (c.accountNum == acc) {
				c.s = state.withdraw;
				c.editmoney = money;
				stateChanged();
			}
		}
	}
	
	public void requestDeposit(int acc, int money) {
		for (Client c : clients) {
			if (c.accountNum == acc) {
				c.s = state.deposit;
				c.editmoney = money;
				stateChanged();
			}
		}
	}

	public void workOver() {
		s = state.leaving;
	}
	
	//-----------------------------------------------Scheduler-------------------------------------------------
	public boolean pickAndExecuteAnAction() {
		if(s.equals("leaving")) {
			leaveBank();
		}
		synchronized(clients) {
			for (Client c : clients) {
				if(c.s.equals("added")) {
					callClient(c);
				}
			}
		}
		synchronized(clients) {
			for (Client c : clients) {
				if(c.s.equals("withdraw")) {
					withdrawDone(c);
				}
				else if (c.s.equals("deposit")) {
					depositDone(c);
				}
				return true;
			}
		}
		synchronized(clients) {
			for (Client c : clients) {
				if(bankAccs.get(c.accountNum) != null) {
					askService(c);
				} 
				else {
					accSetUp(c);
				}
				return true;
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
	}

	public void accSetUp(Client c) {
		bankAccs.put(c.accountNum, c.money);
	}

	public void withdrawDone(Client c) {
		if( c.editmoney >= bankAccs.get(c.accountNum)) {
			c.money = c.money + c.editmoney;
			bankAccs.put(c.accountNum, bankAccs.get(c.accountNum) - c.editmoney);
			c.cust.transactionComplete(c.money);
			clients.remove(c);
		}
	}

	public void depositDone(Client c) {
		c.money = c.money - c.editmoney;
		bankAccs.put(c.accountNum, bankAccs.get(c.accountNum) + c.editmoney);
		c.cust.transactionComplete(c.money);
		clients.remove(c);
	}
	
	public void leaveBank() {
		//Make GUI call to leave the bank
	}
}
