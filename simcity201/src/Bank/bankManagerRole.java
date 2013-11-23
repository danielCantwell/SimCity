package Bank;

import java.util.*;

import SimCity.Base.*;

public class bankManagerRole extends Role{
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
	}
	
	public void newClient(bankCustomerRole c) {
		clients.add(c);
		stateChanged();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void workOver() {
		// TODO Auto-generated method stub
		
	}

}
