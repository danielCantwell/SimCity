package Bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Bank.gui.bankGui;
import Bank.interfaces.Guard;
import Bank.interfaces.Robber;
import Bank.interfaces.Teller;
import SimCity.Base.Role;
import SimCity.Buildings.B_Bank;
import SimCity.Globals.Money;

public class RobberRole extends Role implements Robber{
	//----------------------------------------------Data-------------------------------------------------

	Money wMoney;
	int accNum;
	//private bankCustomerGui gui = new bankCustomerGui(this);
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
	//----------------------------------------------Messages-------------------------------------------------

	@Override
	public void enterBuilding() {
		s = state.enter;
		B_Bank bank = (B_Bank)myPerson.building;
		guard = (Guard)(bank.getBankGuard());
		System.out.println("messaging this guard: " + " "+ guard.toString());
		stateChanged();
		System.out.println("Robber: has entered the building");
		
		bankGui bankgui = (bankGui)myPerson.building.getPanel();
		//bankgui.addGui(gui);		
	}

	@Override
	public void yesEnter() {
		s = state.entered;
		stateChanged();
	}
	

	@Override
	public void tellerCalled(Teller t) {
		System.out.println("Customer: Teller has called customer to come");
		s = state.called;
		teller = t;
		stateChanged();
	}

	@Override
	public void doneRobbing(Money m) {
		wMoney = m;
		s = state.leaving;
		stateChanged();
	}
	//----------------------------------------------Scheduler-------------------------------------------------


	protected boolean pickAndExecuteAnAction() {
		if (s == state.leaving) {
			leaveBank();
		}
		if (s == state.enter){
			openDoor();
			return true;
		}
	
		if(s == state.called) {
			findTeller();
		}
		return false;
	}
	
	//----------------------------------------------Actions-------------------------------------------------

	@Override
	public void openDoor() {
		if (!myPerson.building.getOpen()) {leaveBank(); return;}
		System.out.println("opened door");
		guard.RobberEnter(this);
		s = state.waiting;
	}

	@Override
	public void findTeller() {
		teller.RobTeller(accNum, wMoney, this);
	}
	

	@Override
	public void leaveBank() {
		
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}

}
