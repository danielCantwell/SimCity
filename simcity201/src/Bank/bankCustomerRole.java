package Bank;

import java.util.Collections;

import Bank.gui.*;
import Bank.interfaces.Guard;
import Bank.interfaces.Teller;

import java.util.*;

import SimCity.Globals.*;
import SimCity.Base.*;
import SimCity.Buildings.B_Bank;

/**
 * Bank Customer Role
 * @author Eric
 * 
 */

public class bankCustomerRole extends Role{

	//-----------------------------------------------Data-------------------------------------------------
	
	Money wMoney;
	int accNum;
	private bankCustomerGui gui = new bankCustomerGui(this);
	Money money = new Money(20,0);
	List<String> inventory = Collections.synchronizedList(new ArrayList<String>());
	private Guard guard;
	private Teller teller;
	public state s = state.none;
	public enum state { none, enter, waiting, called, reqSearch, gaveInv, entered, reqService, leaving};

	public void setGuard(Guard bg){
		guard = bg;
	}
	
	public void setMoney(Money m) {
		wMoney = myPerson.getMoney();
		System.out.println("This Customer has :$"+wMoney.dollars+"."+wMoney.cents);
	}
	
	public void setAccNum(int a) {
		accNum = myPerson.getAccNum();
		System.out.println("This Customer's account number: "+accNum);
	}
	//-----------------------------------------------Messages------------------------------------------------
	public void enterBuilding() {
		s = state.enter;
		stateChanged();
		System.out.println("Customer: has entered the building");
	}

	public void requestSearch() {
		s = state.reqSearch;
		stateChanged();
	}

	public void yesEnter() {
		s = state.entered;
		stateChanged();
		System.out.println("Customer: Guard gave permission to enter");
	}

	public void noEnter() {
		s = state.leaving; 		//Leave or possibly throw away bad objects
	}

	public void tellerCalled(Teller t) {
		System.out.println("Customer: Teller has called customer to come");
		s = state.called;
		teller = t;
		stateChanged();
	}
	public void whatService() {
		s = state.reqService;
		stateChanged();
		System.out.println("Customer: Teller asked which service");
	}

	public void transactionComplete(Money m) {
		money = m;
		s = state.leaving;
	}

	@Override
	public void workOver() {
		// make GUI call to leave Bank
	}

	//-----------------------------------------------Scheduler-------------------------------------------------
	public boolean pickAndExecuteAnAction() {
		
		if(s.equals("enter")) {
			
			openDoor();
			return true;
		}
		
		if (s == state.enter){
			
			openDoor();
			return true;
		}
		
		if(s == state.leaving) {
			leaveBank();
			return true;
		}
		if(s == state.reqSearch) {
			giveInv();
			return true;
		}
		if(s == state.called) {
			findTeller();
		}
		if(s == state.reqService) {
			chooseService();
		}
		return false;
	}

	//-----------------------------------------------Actions-------------------------------------------------
	public void openDoor() {
		System.out.println("opened door");
		guard.wantEnter(this);
		s = state.waiting;
	}

	public void giveInv() {
		guard.allowSearch(this, inventory);
		s = state.gaveInv;
		System.out.println("Customer: is asking guard to search");
	}

	public void findTeller() {
		teller.foundTeller(accNum, wMoney, this);
	}

	public void chooseService() {
		if (wMoney.getDollar() < 30) {							//Temporary method for choosing whether to withdraw/deposit
			teller.requestWithdraw(accNum, money); 			//arbitrary amount to withdraw, can be changed later
		}
		else {
			wMoney.subtract(30, 0);
			teller.requestDeposit(accNum,wMoney);				//deposits everything over $30
			wMoney.add(30,0);
			System.out.println(wMoney.getDollar());

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
