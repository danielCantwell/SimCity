package Bank;

import java.util.Collections;
import java.util.*;
import SimCity.Globals.*;
import agent.Agent;
import Bank.gui.*;

/*
 * Bank Teller Role
 */

public class tellerRole extends Agent {
	//-----------------------------------------------Data-------------------------------------------------
	private tellerGui gui = new tellerGui(this);
	Map<Integer, Money> bankAccs = new HashMap<Integer, Money>();
	public List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());
	class Client {
		int accountNum;
		Money money;
		Money editmoney;
		state s;
		bankCustomerRole cust;
	}
	public enum state{ none, ready, added, called, withdraw, deposit, leaving };
	private state s = state.none;


	//-----------------------------------------------Messages-------------------------------------------------
	public void enterBuilding() {
		s = state.ready;
		stateChanged();
	}
	
	public void tellerAssigned(bankCustomerRole c) {
		Client cl = new Client();
		cl.cust = c;
		cl.s = state.added;
		clients.add(cl);
		stateChanged();
	}

	public void foundTeller(int accNum, Money money, bankCustomerRole cust) {
		Client c = new Client();
		c.accountNum = accNum;
		c.money = money;
		c.cust = cust;
		clients.add(c);
	}

	public void requestWithdraw(int acc, Money money) {
		for (Client c : clients) {
			if (c.accountNum == acc) {
				c.s = state.withdraw;
				c.editmoney = money;
				stateChanged();
			}
		}
	}

	public void requestDeposit(int acc, Money money) {
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
		if(s.equals("ready")) {
			goToCounter();
			return true;
		}
		if(s.equals("leaving")) {
			leaveBank();
			return true;
		}
		synchronized(clients) {
			for (Client c : clients) {
				if(c.s.equals("added")) {
					callClient(c);
				}
				return true;
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
		if( c.editmoney.isGreaterThan(bankAccs.get(c.accountNum))) {
			c.money = c.money.add(c.editmoney);
			Money temp = bankAccs.get(c.accountNum);
			bankAccs.put(c.accountNum, temp.subtract(c.editmoney));
			c.cust.transactionComplete(c.money);
			clients.remove(c);
		}
	}

	public void depositDone(Client c) {
		c.money = c.money.subtract(c.editmoney);
		Money temp = bankAccs.get(c.accountNum);
		bankAccs.put(c.accountNum, temp.add(c.editmoney));
		c.cust.transactionComplete(c.money);
		clients.remove(c);
	}

	public void goToCounter() {
		//Make GUI call to walk to the counter
	}
	
	public void leaveBank() {
		//Make GUI call to leave the bank
	}

	public void setGui(tellerGui gui) {
		this.gui = gui;
	}
	public tellerGui getGui() { 
		return gui;
	}
}
