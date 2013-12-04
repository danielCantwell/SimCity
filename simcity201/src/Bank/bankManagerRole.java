package Bank;

import java.util.*;

import Bank.interfaces.Customer;
import Bank.interfaces.Guard;
import Bank.interfaces.Manager;
import Bank.interfaces.Teller;
import SimCity.Base.*;
import SimCity.Buildings.B_Bank;
/**
 * Manager Role
 * @author Eric
 *
 */
public class bankManagerRole extends Role implements  Manager{
	
	Guard guard;
	
	public void msgHello(){
		System.out.println("Manager received a messsage from customer");
	}
	
	@Override
	public Guard getGuard(){return guard;}
	
	@Override
	public void setGuard(Guard bg){
		guard = bg;
		B_Bank bank = (B_Bank)myPerson.building;
		//bank.setBankGuard();
		myPerson.building.setOpen(myPerson.building.areAllNeededRolesFilled());
	}
	
	//----------------------------------------------Data-------------------------------------------------
	List<Teller> tellers = Collections.synchronizedList(new ArrayList<Teller>());
	
	public class Teller {
		int tellerNum;
		Bank.interfaces.Teller teller;
		state busy;
	}
	private enum state { yes, no };
	List<Customer>clients = Collections.synchronizedList(new ArrayList<Customer>());
		
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
		//Brian adding some code to make sure the bank is open
		myPerson.building.setOpen(myPerson.building.areAllNeededRolesFilled());
	}

	@Override
	public void newClient(Customer c) {
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
		B_Bank bank = (B_Bank)myPerson.building;
		bank.setBankManager(this);
		stateChanged();
	}

	public void workOver() {
		// GUI call to leave bank
		exitBuilding(myPerson);
	}

}
