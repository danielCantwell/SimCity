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
import SimCity.trace.AlertTag;

public class RobberRole extends Role implements Robber{
	//----------------------------------------------Data-------------------------------------------------

	Money rMoney;
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
	
	}

	@Override
	public void setAccNum(int a) {
		accNum = myPerson.getAccNum();
		Do(AlertTag.BANK,"This Customer's account number: "+accNum);
	}
	//----------------------------------------------Messages-------------------------------------------------

	@Override
	public void enterBuilding() {
		s = state.enter;
		rMoney = myPerson.getMoney();
		B_Bank bank = (B_Bank)myPerson.building;
		guard = (Guard)(bank.getBankGuard());
		Do(AlertTag.BANK,"Robber: puts on his cloak of invisibility because duh, easiest way to rob.");
		Do(AlertTag.BANK,"Robber: has entered the building");
		stateChanged();
		//bankGui bankgui = (bankGui)myPerson.building.getPanel();
		//bankgui.addGui(gui);		
	}

	@Override
	public void yesEnter() {
		s = state.entered;
		Do(AlertTag.BANK,"Robber: Has made it into the bank undetected");
		stateChanged();
	}
	

	@Override
	public void tellerCalled(Teller t) {
		Do(AlertTag.BANK,"Customer: Teller has called customer to come");
		s = state.called;
		teller = t;
		stateChanged();
	}

	@Override
	public void doneRobbing(Money m) {
		rMoney = m;
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
	
		if(s == state.entered){
			robBank();
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
//		if (!myPerson.building.getOpen()) {leaveBank(); System.out.println("BANK CLOSED MOFO CANT ROB DIS ISH");return;}
		Do(AlertTag.BANK,"opened door");
		guard.RobberEnter(this);
		s = state.waiting;
	}

	public void robBank() {
		rMoney.add(500, 0);
		s = state.leaving;
	}
	
	@Override
	public void findTeller() {
		teller.RobTeller(accNum, rMoney, this);
	}
	

	@Override
	public void leaveBank() {
		Do(AlertTag.BANK,"Robber has made it out with : $"+rMoney.getDollar());
		exitBuilding(myPerson);
	}

	@Override
	public void workOver() {
		leaveBank();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "BRbr";
	}

}
