package Bank.test.mock;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import Bank.gui.bankCustomerGui;
import Bank.interfaces.Customer;
import Bank.interfaces.Guard;
import Bank.interfaces.Teller;
import SimCity.Globals.Money;
/**
 * 
 * @author Eric
 *
 */

public class MockCustomer implements Customer{
	public EventLog log = new EventLog();

	@Override
	public void setGuard(Guard bg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMoney(Money m) {
		// TODO Auto-generated method stub
		
	}

	public void setAccNum(int a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBuilding() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestSearch() {
		log.add(new LoggedEvent("Guard has requested search"));		
	}

	@Override
	public void yesEnter() {
		log.add(new LoggedEvent ("Gaurd let me in"));		
	}

	@Override
	public void noEnter() {
		// TODO Auto-generated method stub
		
	}

	public void tellerCalled(Teller t, int x) {
		log.add(new LoggedEvent ("Teller called for me"));
	}

	@Override
	public void whatService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transactionComplete(Money m) {
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
	public void openDoor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveInv() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void findTeller() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chooseService() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveBank() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(bankCustomerGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public bankCustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doneMotion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAccNum() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
