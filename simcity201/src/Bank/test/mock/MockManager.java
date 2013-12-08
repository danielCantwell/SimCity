package Bank.test.mock;
import java.util.Map;

import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import SimCity.Base.Person;
import SimCity.Buildings.B_House;
import SimCity.Globals.Money;
import SimCity.gui.Gui;
import Bank.bankCustomerRole;
import Bank.tellerRole;
import Bank.interfaces.*;
/**
 * 
 * @author Eric
 *
 */

public class MockManager implements Manager {

	@Override
	public Guard getGuard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGuard(Guard bg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newTeller(Teller teller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newClient(Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void callTeller(Customer c, Bank.bankManagerRole.Teller t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tellerReady(tellerRole tellerRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveMap(Map<Integer, Money> bankAccs) {
		// TODO Auto-generated method stub
		
	}

}
