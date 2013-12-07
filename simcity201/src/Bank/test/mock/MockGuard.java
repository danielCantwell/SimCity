package Bank.test.mock;

import java.util.List;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Base.Person;
import SimCity.Buildings.B_Bank;
import SimCity.Globals.Money;
import Bank.bankCustomerRole;
import Bank.bankGuardRole.Entry;
import Bank.gui.bankGuardGui;
import Bank.interfaces.*;


/**
 * 
 * @author Eric
 *
 */
public class MockGuard implements Guard {
	
	public EventLog log = new EventLog();


	@Override
	public void setBank(B_Bank bank) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void wantEnter(bankCustomerRole newC) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void allowSearch(Customer newC, List<String> inventory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void askSearch(Entry c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Search(Entry c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveBank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(bankGuardGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public bankGuardGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void RobberEnter(Robber robberRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wantEnter(Customer newC) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doneMotion() {
		// TODO Auto-generated method stub
		
	}

}
