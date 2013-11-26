package Bank;

import java.util.*;

import Bank.interfaces.Customer;
import Bank.interfaces.Guard;
import Bank.interfaces.Manager;
import Bank.interfaces.Teller;
import SimCity.Base.*;
/**
 * Manager Role
 * @author Eric
 *
 */
public class bankManagerRole extends Role implements  Manager{
	
	Guard guard;
	
	@Override
	public Guard getGuard(){return guard;}
	
	@Override
	public void setGuard(Guard bg){guard = bg;}
	
	//----------------------------------------------Data-------------------------------------------------
	List<Teller> tellers = Collections.synchronizedList(new ArrayList<Teller>());
	
	public class Teller {
		int tellerNum;
		Bank.interfaces.Teller teller;
		state busy;
	}
	private enum state { yes, no };
	List<bankCustomerRole>clients = Collections.synchronizedList(new ArrayList<bankCustomerRole>());
		
	//----------------------------------------------Messages-------------------------------------------------
	@Override
	public void newTeller(Bank.interfaces.Teller teller) {
		Teller t = new Teller();
		t.teller = teller;
		t.tellerNum = tellers.size();
		t.busy = state.no;
		tellers.add(t);
		stateChanged();
		System.out.println("Manager: New teller has been added");
	}

	@Override
	public void newClient(bankCustomerRole c) {
		clients.add(c);
		stateChanged();
		System.out.println("Manager: New customer has been added");
	}
	
	//----------------------------------------------Scheduler-------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		synchronized(tellers) {
			for (Teller t : tellers) {
				if (t.busy == state.no && !clients.isEmpty()) {
					callTeller(clients.get(0), t);
				}
				return true;
			}
		}
		return false;
	}
	
	
	//----------------------------------------------Actions-------------------------------------------------
	
	@Override
	public void callTeller(Customer c, Teller t) {
		t.teller.tellerAssigned(c);
		tellers.remove(t);
		clients.remove(c);
	}

	@Override
	protected void enterBuilding() {
		System.out.println("I am a bank manager");
		stateChanged();
	}

	public void workOver() {
		// TODO Auto-generated method stub
		
	}

}
