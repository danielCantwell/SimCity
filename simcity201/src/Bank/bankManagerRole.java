package Bank;

import java.util.*;

import Bank.interfaces.Customer;
import Bank.interfaces.Guard;
import Bank.interfaces.Manager;
import Bank.interfaces.Teller;
import EricRestaurant.EricHost;
import SimCity.Base.*;
import SimCity.Buildings.B_Bank;
import SimCity.Globals.Money;
import SimCity.trace.AlertTag;
/**
 * Manager Role
 * @author Eric
 *
 */
public class bankManagerRole extends Role implements  Manager{

	Guard guard;
	public Map<Integer, Money> ManagerAccs = new HashMap<Integer, Money>();

	public void msgHello(){
		Do(AlertTag.BANK,"Manager received a messsage from customer");
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
	public static int accNum = 0;
	Money RMoney = new Money(500,0);
	List<Teller> tellers = Collections.synchronizedList(new ArrayList<Teller>());

	public class Teller {
		int tellerNum;
		Bank.interfaces.Teller teller;
		state busy;
	}

	private enum state { yes, no };
	List<Customer>clients = Collections.synchronizedList(new ArrayList<Customer>());

	public bankManagerRole() {
		if(!ManagerAccs.containsKey(1000)) {
		ManagerAccs.put(1000, RMoney);
		ManagerAccs.put(2000, RMoney);
		ManagerAccs.put(3000, RMoney);
		ManagerAccs.put(4000, RMoney);
		ManagerAccs.put(5000, RMoney);
		}
	}

	//----------------------------------------------Messages-------------------------------------------------

	@Override
	protected void enterBuilding() {
		Do(AlertTag.BANK,"I am a bank manager: "+this);

		B_Bank bank = (B_Bank)myPerson.building;
		bank.setBankManager(this);
		stateChanged();
	}

	@Override
	public void newTeller(Bank.interfaces.Teller teller) {
		Teller t = new Teller();
		t.teller = teller;
		t.tellerNum = tellers.size();
		t.busy = state.no;
		tellers.add(t);
		t.teller.managerMap(ManagerAccs);
		stateChanged();
		Do(AlertTag.BANK,"Manager: New teller has been added: "+t.teller);
		//Brian adding some code to make sure the bank is open
		myPerson.building.setOpen(myPerson.building.areAllNeededRolesFilled());
	}

	@Override
	public void newClient(Customer c) {
		clients.add(c);
		stateChanged();
		Do(AlertTag.BANK,"Manager: New customer has been added: "+c+"    with account: "+accNum);

	}

	public void tellerReady(tellerRole teller) {
		for (Teller t : tellers) {
			if(t.teller == teller) {
				t.busy = state.no;
			}
		}
	}

	public void giveMap(Map<Integer, Money> bankAccs) {
		ManagerAccs.putAll(bankAccs);
	}


	//----------------------------------------------Scheduler-------------------------------------------------
	@Override
	public boolean pickAndExecuteAnAction() {
		
		synchronized(tellers) {
			for (Teller t : tellers) {
				if (t.busy == state.no && !clients.isEmpty()) {
					callTeller(clients.get(0), t);
					return true;
				}
			}
		}


		return false;
	}


	//----------------------------------------------Actions-------------------------------------------------

	@Override
	public void callTeller(Customer c, Teller t) {
		if (c.getAccNum() == -1) {
			t.teller.tellerAssigned(c, accNum);
			accNum++;
		}
		else t.teller.tellerAssigned(c);
		t.busy = state.yes;
		clients.remove(c);
	}


	public void workOver() {
		// GUI call to leave bank
		exitBuilding(myPerson);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "bankManagerRole";
	}

}
