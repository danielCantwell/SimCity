package Bank;

import java.util.Collections;
import java.util.*;
import agent.Agent;

/*
 * Bank Customer Role
 */

public class bankCustomerRole extends Agent{
	
	//-----------------------------------------------Data-------------------------------------------------
	int accNum;
	List<String> inventory = Collections.synchronizedList(new ArrayList<String>());
	int money;			//Money is an int right now, will change to Money class
	private bankGuardRole guard;
	private tellerRole teller;
	state s = state.none;
	public enum state { none, reqSearch, gaveInv, entered, reqService, leaving};
	
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

	public void whatService() {
		s = state.reqService;
		stateChanged();
	}

	public void transactionComplete(int m) {
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
		if(s.equals("entered")) {
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
		if (money < 30) {									//Temporary method for choosing whether to withdraw/deposit
			teller.requestWithdraw(accNum, 20); 			//arbitrary amount to withdraw, can be changed later
		}
		else {
			teller.requestDeposit(accNum,(money - 30));		//deposits everything over $30
		}
	}
	
	public void leaveBank() {
		// make GUI call to leave bank 
	}
}
