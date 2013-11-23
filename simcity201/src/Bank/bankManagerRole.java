package Bank;

import java.util.*;

import SimCity.Base.*;

public class bankManagerRole extends Role{
	
	bankGuardRole guard;
	public bankGuardRole getGuard(){return guard;}
	public void setGuard(bankGuardRole bg){guard = bg;}
	
	//----------------------------------------------Data-------------------------------------------------
	List<Teller> tellers = Collections.synchronizedList(new ArrayList<Teller>());
	
	public static final int NCounters = 2;
	public class Teller {
		int tellerNum;
		tellerRole teller;
		state busy;
	}
	private enum state { yes, no };
	List<bankCustomerRole>clients = Collections.synchronizedList(new ArrayList<bankCustomerRole>());
		
	//----------------------------------------------Messages-------------------------------------------------
	public void newTeller(tellerRole teller) {
		Teller t = new Teller();
		t.teller = teller;
		t.tellerNum = tellers.size();
		t.busy = state.no;
		tellers.add(t);
		stateChanged();
		System.out.println("Manager: New teller has been added");
	}
	
	public void newClient(bankCustomerRole c) {
		clients.add(c);
		stateChanged();
		System.out.println("Manager: New customer has been added");
	}
	
	//----------------------------------------------Scheduler-------------------------------------------------
	public boolean pickAndExecuteAnAction() {
		synchronized(tellers) {
			for (Teller t : tellers) {
				if (t.busy.equals("no") && !clients.isEmpty()) {
					callTeller(clients.get(0), t);
				}
				return true;
			}
		}
		return false;
	}
	
	
	//----------------------------------------------Actions-------------------------------------------------
	
	public void callTeller(bankCustomerRole c, Teller t) {
		t.teller.tellerAssigned(c);
		tellers.remove(t);
		clients.remove(c);
	}

	@Override
	protected void enterBuilding() {
		System.out.println("I am a bank manager");
		stateChanged();
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}

}
