package Bank;

import java.util.*;

import Bank.gui.bankGuardGui;
import Bank.interfaces.Guard;
import Bank.interfaces.Manager;
import SimCity.Base.*;
import SimCity.Buildings.B_Bank;

/**
 * Bank Guard Role
 * @author Eric
 */

public class bankGuardRole extends Role implements Guard {
	//----------------------------------------------Data-------------------------------------------------
	B_Bank curBank;
	Manager manager;
	bankGuardGui gui = new bankGuardGui(this);
	List<String> badObjs = new ArrayList<String>();
	public List<Entry> custEnter = Collections.synchronizedList(new ArrayList<Entry>());
	public class Entry {
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

		//B_Bank curBank = (B_Bank)God.Get().getBuilding(0);
		//manager = curBank.getBankManager();
	}

	@Override
	public void setBank(B_Bank bank){
		curBank = bank;
		manager = bank.getBankManager();
	}

	//----------------------------------------------Messages-------------------------------------------------

	@Override
	public void enterBuilding() {
		s = state.ready;
		stateChanged();
	}

	@Override
	public void wantEnter(bankCustomerRole newC) {
		Do(newC + "wants to enter");
		Entry c = new Entry();
		c.s = state.entered;
		c.bc = newC;
		custEnter.add(c);
		stateChanged();
	}

	@Override
	public void allowSearch(bankCustomerRole newC, List<String> inventory) {
		for (Entry c : custEnter) {
			if(c.bc == newC) {
				c.inventory = inventory;
				c.s = state.complied;
				stateChanged();
			}
		}
	}

	@Override
	public void workOver() {
		s = state.leaving;
		stateChanged();
	}

	//----------------------------------------------Scheduler-------------------------------------------------

	@Override
	public boolean pickAndExecuteAnAction() {
		if( s == state.ready) {
			enterBank();
			return true;
		}
		if ( s== state.leaving) {
			leaveBank();
			return true;
		}
		synchronized(custEnter) {
			for (Entry c : custEnter) {
				if (c.s == state.complied) {
					Search(c);
					return true;
				}
			}
		}
		synchronized(custEnter) {
			for (Entry c : custEnter) {
				if (c.s == state.entered) {
					askSearch(c);
					return true;
				}
			}
		}
		return false;
	}

	//-----------------------------------------------Actions-------------------------------------------------

	@Override
	public void askSearch(Entry c) {
		c.bc.requestSearch();
		c.s = state.requested;
	}

	@Override
	public void Search(Entry c) {
		if(c.inventory.size() == 0) {
			c.bc.yesEnter();
			System.out.println ("Bank Guard: Customer Can Enter");
			manager.newClient(c.bc);
		}
		else {
			for(int i = 0; i < c.inventory.size(); i++) {
				for(int j = 0; j < badObjs.size(); j++) {
					if(badObjs.get(j).equals(c.inventory.get(i))) {
						System.out.println ("Bank Guard: Customer Cannot Enter");
						c.bc.noEnter();
					}
					else {
						c.bc.yesEnter();
						System.out.println ("Bank Guard: Customer Can Enter");
						manager.newClient(c.bc);
					}
				}
			}
		}
		custEnter.remove(c);
	}

	@Override
	public void enterBank() {
		//Make GUI call to enter bank
		s = state.onD;
	}

	@Override
	public void leaveBank() {
		//Make GUI call to leave the bank
		s = state.offD;
	}

	@Override
	public void setGui(bankGuardGui gui) {
		this.gui = gui;
	}

	@Override
	public bankGuardGui getGui() { 
		return gui;
	}
}
