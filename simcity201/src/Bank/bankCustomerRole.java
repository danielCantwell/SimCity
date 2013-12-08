package Bank;

import java.util.Collections;

import Bank.gui.*;
import Bank.interfaces.Customer;
import Bank.interfaces.Guard;
import Bank.interfaces.Teller;

import java.util.*;
import java.util.concurrent.Semaphore;

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
	public int accNum;
	private bankCustomerGui gui = new bankCustomerGui(this);
	private Semaphore moving = new Semaphore(0,true);
	Money money = new Money(20,0);
	List<String> inventory = Collections.synchronizedList(new ArrayList<String>());
	private Guard guard;
	private Teller teller;
	public state s = state.none;
	public enum state { none, enter, waiting, inLine, called, finding, reqSearch, gaveInv, entered, reqService, leaving};

	@Override
	public void setGuard(Guard bg){
		guard = bg;
	}

	@Override
	public void setMoney(Money m) {
		wMoney = myPerson.getMoney();
		System.out.println("This Customer has :$"+wMoney.dollars+"."+wMoney.cents);
	}

	//-----------------------------------------------Messages------------------------------------------------

	@Override
	public void enterBuilding() {
		s = state.enter;
		wMoney = myPerson.getMoney();
		accNum = myPerson.getAccNum();
		System.out.println("This Customer has entered the building with: $"+wMoney.dollars+"."+wMoney.cents);
		B_Bank bank = (B_Bank)myPerson.building;
		guard = bank.getBankGuard();
		System.out.println("messaging this guard: " + " "+ guard.toString());
		bankGui bankgui = (bankGui)myPerson.building.getPanel();
		bankgui.addGui(gui);
		gui.setText("Customer");
		stateChanged();
	}

	@Override
	public void requestSearch() {
		s = state.reqSearch;
		System.out.println("Customer: Guard requested a search "+s);
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
	public void tellerCalled(Teller t, int num) {
		s = state.called;
		teller = t;
		accNum = num;
		myPerson.accNum = num;
		stateChanged();
	}

	@Override
	public void whatService() {
		s = state.reqService;
		System.out.println("Customer: "+this+" Teller asked which service");
		stateChanged();
	}

	@Override
	public void transactionComplete(Money m) {
		wMoney = m;
		s = state.leaving;
		stateChanged();
	}

	public void workOver() {
		s = state.leaving;
		stateChanged();
	}

	public void doneMotion() {
		moving.release();
	}
	//-----------------------------------------------Scheduler-------------------------------------------------

	@Override
	public boolean pickAndExecuteAnAction() {
		//System.out.println("------------------------------------");
		if (s == state.enter){

			openDoor();
			return true;
		}

		if(s == state.leaving) {
			leaveBank();
			return true;
		}
		if(s == state.reqSearch) {
			System.out.println("Customer PAE reqSearch");
			giveInv();
			return true;
		}
		if(s == state.entered) {
			waiting();
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
		//if (!myPerson.building.getOpen()) {leaveBank(); return;}
		System.out.println("opened door");

		//bankGuardRole newGuard = (bankGuardRole)guard;
		//System.out.println("messaging this guard: " + guard + "active? " + newGuard.getActive() );
		//newGuard.test(this);
		guard.wantEnter(this);
		//B_Bank bank= (B_Bank)myPerson.building;
		//bank.getBankManager()
		gui.doEnterBank();
		s = state.waiting;
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void giveInv() {
		System.out.println("Customer: is asking guard to search");
		guard.allowSearch(this, inventory);
		s = state.gaveInv;
	}

	public void waiting() {
		gui.doWaitLine(1);
		s = state.inLine;
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void findTeller() {
		s = state.finding;
		gui.doGoToTeller();
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		teller.foundTeller(wMoney, this);
	}

	@Override
	public void chooseService() {
		if (wMoney.getDollar() < 30) {							//Temporary method for choosing whether to withdraw/deposit
			teller.requestWithdraw(accNum, money); 			//arbitrary amount to withdraw, can be changed later
		}
		else {	
			wMoney.subtract(30,0);
			teller.requestDeposit(accNum,wMoney);				//deposits everything over $30
			wMoney = new Money(30,0);
			//System.out.println(wMoney.getDollar());
		}
	}

	@Override
	public void leaveBank() {
		gui.doLeaveBank();
		System.out.println("customer leaving bank");
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		exitBuilding(myPerson);
	}
	
	public int getAccNum() {
		return accNum;
	}
	
	@Override
	public void setGui(bankCustomerGui gui) {
		this.gui = gui;
	}

	@Override
	public bankCustomerGui getGui() { 
		return gui;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "bankCustomerRole";
	}

}
