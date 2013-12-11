package Bank;

import java.util.Collections;

import Bank.gui.*;
import Bank.interfaces.Customer;
import Bank.interfaces.Guard;
import Bank.interfaces.Teller;

import java.util.*;
import java.util.concurrent.Semaphore;

import SimCity.Globals.*;
import SimCity.trace.AlertTag;
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
	Money money = new Money(200,0);
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
		Do(AlertTag.BANK,"This Customer has :$"+wMoney.dollars+"."+wMoney.cents);
	}

	//-----------------------------------------------Messages------------------------------------------------

	@Override
	public void enterBuilding() {
		B_Bank bank = (B_Bank)myPerson.building;
		if(!(bank.getBankManager() == null && bank.getBankGuard() == null && bank.getOneTeller() == null)){
		s = state.enter;
		wMoney = myPerson.getMoney();
		accNum = myPerson.getAccNum();
		Do(AlertTag.BANK,"This Customer has entered the building with: $"+wMoney.dollars+"."+wMoney.cents);
		guard = bank.getBankGuard();
		Do(AlertTag.BANK,"messaging this guard: " + " "+ guard.toString());
		bankGui bankgui = (bankGui)myPerson.building.getPanel();
		bankgui.addGui(gui);
		gui.setText("Customer");
		stateChanged();
		} else leaveBank();
	}

	@Override
	public void requestSearch() {
		s = state.reqSearch;
		Do(AlertTag.BANK,"Customer: Guard requested a search "+s);
		stateChanged();
	}

	@Override
	public void yesEnter() {
		s = state.entered;
		stateChanged();
		Do(AlertTag.BANK,"Customer: Guard gave permission to enter");
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
		Do(AlertTag.BANK,"Customer: "+this+" Teller asked which service");
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
		//Do(AlertTag.BANK,"------------------------------------");
		if (s == state.enter){

			openDoor();
			return true;
		}

		if(s == state.leaving) {
			leaveBank();
			return true;
		}
		if(s == state.reqSearch) {
			Do(AlertTag.BANK,"Customer PAE reqSearch");
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
		Do(AlertTag.BANK,"opened door");

		//bankGuardRole newGuard = (bankGuardRole)guard;
		//Do(AlertTag.BANK,"messaging this guard: " + guard + "active? " + newGuard.getActive() );
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
		Do(AlertTag.BANK,"Customer: is asking guard to search");
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
		if (wMoney.getDollar() < 100) {							//Temporary method for choosing whether to withdraw/deposit
			teller.requestWithdraw(accNum, money); 			//arbitrary amount to withdraw, can be changed later
		}
		else {	
			Money temp = new Money(wMoney.getDollar(),0);
			temp.subtract(300,0);
			teller.requestDeposit(accNum,temp);				//deposits everything over $300
			//wMoney = new Money(30,0);
		}
	}

	@Override
	public void leaveBank() {
		gui.doLeaveBank();
		Do(AlertTag.BANK,"customer leaving bank with : $"+wMoney.getDollar());
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		myPerson.msgGoHome();
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
		return "BCmr";
	}

}
