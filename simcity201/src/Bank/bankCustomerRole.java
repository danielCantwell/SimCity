package Bank;

import java.util.Collections;

import Bank.gui.*;

import java.util.*;

import SimCity.Globals.*;
import agent.Agent;

/*
 * Bank Customer Role
 */

public class bankCustomerRole extends Agent{
	
	//-----------------------------------------------Data-------------------------------------------------
	int accNum;
	private bankCustomerGui gui = new bankCustomerGui(this);
	Money money;
	List<String> inventory = Collections.synchronizedList(new ArrayList<String>());
	private bankGuardRole guard;
	private tellerRole teller;
	state s = state.none;
	Money wMoney = new Money(20,0);
	public enum state { none, called, reqSearch, gaveInv, entered, reqService, leaving};
	
	//-----------------------------------------------Messages------------------------------------------------

	public void requestSearch() {
		s = state.reqSearch;
		stateChanged();
	}
	
	public void yesEnter() {
		s = state.entered;
		stateChanged();
	}
	
	public void noEnter() {
		s = state.leaving; 		//Leave or possibly throw away bad objects
	}

	public void tellerCalled(tellerRole t) {
		s = state.called;
		teller = t;
	}
	public void whatService() {
		s = state.reqService;
		stateChanged();
	}

	public void transactionComplete(Money m) {
		money = m;
		s = state.leaving;
	}
	
	//-----------------------------------------------Scheduler-------------------------------------------------
	public boolean pickAndExecuteAnAction() {
		if(s.equals("leaving")) {
			leaveBank();
			return true;
		}
		if(s.equals("reqSearch")) {
			giveInv();
			return true;
		}
		if(s.equals("called")) {
			findTeller();
		}
		if(s.equals("reqService")) {
			chooseService();
		}
		return false;
	}
	
	//-----------------------------------------------Actions-------------------------------------------------
	public void giveInv() {
		guard.allowSearch(this, inventory);
		s = state.gaveInv;
	}
	
	public void findTeller() {
		teller.foundTeller(accNum, money, this);
	}
	
	public void chooseService() {
		if (money.getDollar() > 30) {							//Temporary method for choosing whether to withdraw/deposit
			teller.requestWithdraw(accNum, wMoney); 			//arbitrary amount to withdraw, can be changed later
		}
		else {
			money.subtract(30, 0);
			teller.requestDeposit(accNum,money);				//deposits everything over $30
			money.add(30,0);
		}
	}
	
	public void leaveBank() {
		// make GUI call to leave bank 
	}
	
	public void setGui(bankCustomerGui gui) {
		this.gui = gui;
	}
	public bankCustomerGui getGui() { 
		return gui;
	}
}
