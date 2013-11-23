package Bank;

import java.util.*;
import Bank.gui.bankGuardGui;
import SimCity.Base.*;

/*
 * Bank Guard Role
 */

public class bankGuardRole extends Role {
	//----------------------------------------------Data-------------------------------------------------
	bankManagerRole manager;
	bankGuardGui gui = new bankGuardGui(this);
	List<String> badObjs = new ArrayList<String>();
	public List<Entry> custEnter = Collections.synchronizedList(new ArrayList<Entry>());
	class Entry {
		List<String> inventory;
		state s;
		bankCustomerRole bc;
	}
	public enum state { none, ready, onD, offD, entered, requested, complied, searched, leaving };
	private state s = state.none;
	public bankGuardRole() {
		badObjs.add("gun");
		badObjs.add("knife");
		badObjs.add("ski mask");
	}

	//----------------------------------------------Messages-------------------------------------------------
	public void enterBuilding() {
		s = state.ready;
		stateChanged();
	}
		
	public void wantEnter(bankCustomerRole newC) {
		Entry c = new Entry();
		c.s = state.entered;
		c.bc = newC;
		custEnter.add(c);
		stateChanged();
	}

	public void allowSearch(bankCustomerRole newC, List<String> inventory) {
		for (Entry c : custEnter) {
			if(c.bc == newC) {
				c.inventory = inventory;
				c.s = state.complied;
				stateChanged();
			}
		}
	}

	public void workOver() {
		s = state.leaving;
		stateChanged();
	}

	//----------------------------------------------Scheduler-------------------------------------------------
	public boolean pickAndExecuteAnAction() {
		if( s.equals("ready")) {
			enterBank();
			return true;
		}
		if ( s.equals("leaving")) {
			leaveBank();
			return true;
		}
		synchronized(custEnter) {
			for (Entry c : custEnter) {
				if (c.s.equals("complied")) {
					Search(c);
					return true;
				}
			}
		}
		synchronized(custEnter) {
			for (Entry c : custEnter) {
				if (c.s.equals("entered")) {
					askSearch(c);
					return true;
				}
			}
		}
		return false;
	}

	//-----------------------------------------------Actions-------------------------------------------------
	public void askSearch(Entry c) {
		c.bc.requestSearch();
		c.s = state.requested;
	}

	public void Search(Entry c) {
		for(int i = 0; i < badObjs.size(); i++) {
			for(int j = 0; j < c.inventory.size(); j++) {
				if(badObjs.get(1).equals(c.inventory.get(j))) {
					c.bc.noEnter();
				}
				else {
					c.bc.yesEnter();
					manager.newClient(c.bc);
				}
			}
		}
		custEnter.remove(c);
	}

	public void enterBank() {
		//Make GUI call to enter bank
		s = state.onD;
	}
	
	public void leaveBank() {
		//Make GUI call to leave the bank
		s = state.offD;
	}
	public void setGui(bankGuardGui gui) {
		this.gui = gui;
	}
	public bankGuardGui getGui() { 
		return gui;
	}
}
