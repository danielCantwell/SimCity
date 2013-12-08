package Bank.test.mock;

import java.util.Map;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Base.Person;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import SimCity.gui.Gui;
import Bank.tellerRole;
import Bank.tellerRole.Client;
import Bank.gui.tellerGui;
import Bank.interfaces.*;

/**
 * 
 * @author Eric
 *
 */

public class MockTeller implements Teller {
	public EventLog log = new EventLog();

	@Override
	public void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tellerAssigned(Customer c) {
		// TODO Auto-generated method stub
		
	}

	public void foundTeller(int accNum, Money money, Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestWithdraw(int acc, Money money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestDeposit(int acc, Money money) {
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
	public void callClient(Client c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askService(Client c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accSetUp(Client c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void withdrawDone(Client c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void depositDone(Client c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveBank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(tellerGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public tellerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void RobTeller(int accNum, Money money, Robber robberRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doneMotion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tellerAssigned(Customer c, int accNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void foundTeller(Money money, Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void managerMap(Map<Integer, Money> managerAccs) {
		// TODO Auto-generated method stub
		
	}

}
