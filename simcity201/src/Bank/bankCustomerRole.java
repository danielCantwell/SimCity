package Bank;

import java.util.Collections;

import Bank.gui.*;
import Bank.interfaces.Customer;
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

public class bankCustomerRole extends Role implements Customer{

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

	@Override
	public void setGuard(Guard bg){
		guard = bg;
	}

	@Override
	public void setMoney(Money m) {
		wMoney = myPerson.getMoney();
		System.out.println("This Customer has :$"+wMoney.dollars+"."+wMoney.cents);
	}

	@Override
	public void setAccNum(int a) {
		accNum = myPerson.getAccNum();
		System.out.println("This Customer's account number: "+accNum);
	}
	//-----------------------------------------------Messages------------------------------------------------

	@Override
	public void enterBuilding() {
		s = state.enter;
		B_Bank bank = (B_Bank)myPerson.building;
		guard = (Guard)(bank.getBankGuard());
		//System.out.println("messaging this guard: " + " "+ guard.toString());
		
		System.out.println("Customer: has entered the building");
		
		bankGui bankgui = (bankGui)myPerson.building.getPanel();
		bankgui.addGui(gui);
		stateChanged();
	}

	@Override
	public void requestSearch() {
		s = state.reqSearch;
		stateChanged();
	}

	@Override
	public void yesEnter() {
		s = state.entered;
		stateChanged();
		System.out.println("Customer: Guard gave permission to enter");
	}

	@Override
	public void noEnter() {
		s = state.leaving; 		//Leave or possibly throw away bad objects
	}

	@Override
	public void tellerCalled(Teller t) {
		System.out.println("Customer: Teller has called customer to come");
		s = state.called;
		teller = t;
		stateChanged();
	}

	@Override
	public void whatService() {
		s = state.reqService;
		stateChanged();
		System.out.println("Customer: Teller asked which service");
	}

	@Override
	public void transactionComplete(Money m) {
		money = m;
		s = state.leaving;
		stateChanged();
	}
	
	public void workOver() {
		// make GUI call to leave Bank
	}

	//-----------------------------------------------Scheduler-------------------------------------------------

	@Override
	public boolean pickAndExecuteAnAction() {
		
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
	@Override
	public void openDoor() {
		if (!myPerson.building.getOpen()) {leaveBank(); return;}
		System.out.println("opened door");
		
		bankGuardRole newGuard = (bankGuardRole)guard;
		System.out.println("messaging this guard: " + guard + "active? " + newGuard.getActive() );
		newGuard.test(this);
		newGuard.wantEnter(this);
		//B_Bank bank= (B_Bank)myPerson.building;
		//bank.getBankManager()
		s = state.waiting;
	}

	@Override
	public void giveInv() {
		guard.allowSearch(this, inventory);
		s = state.gaveInv;
		System.out.println("Customer: is asking guard to search");
	}

	@Override
	public void findTeller() {
		teller.foundTeller(accNum, wMoney, this);
	}

	@Override
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

	@Override
	public void leaveBank() {
		gui.doLeaveBank();
		System.out.println("customer leaving bank");
		exitBuilding(myPerson);
	}
	@Override
	public void setGui(bankCustomerGui gui) {
		this.gui = gui;
	}

	@Override
	public bankCustomerGui getGui() { 
		return gui;
	}

}
