package Bank;

import java.util.*;
import java.util.concurrent.Semaphore;

import Bank.gui.bankGuardGui;
import Bank.gui.bankGui;
import Bank.interfaces.Customer;
import Bank.interfaces.Guard;
import Bank.interfaces.Manager;
import Bank.interfaces.Robber;
import SimCity.Base.*;
import SimCity.Base.Person.Intent;
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
	private Semaphore moving = new Semaphore(0,true);
	public List<String> badObjs = new ArrayList<String>();
	public List<Entry> custEnter = Collections.synchronizedList(new ArrayList<Entry>());
	public class Entry {
		List<String> inventory;
		state s;
		Customer bc;
		Robber r;
	}
	public enum state { none, ready, onD, offD, entered, requested, complied, searched, leaving, robber };
	private state s = state.none;
	public bankGuardRole() {
		badObjs.add("gun");
		badObjs.add("knife");
		badObjs.add("ski mask");

		//B_Bank curBank = (B_Bank)God.Get().getBuilding(0);
		//manager = curBank.getBankManager();
	}


	public void setBank(B_Bank bank){
		curBank = bank;
		manager = bank.getBankManager();
	}

	//----------------------------------------------Messages-------------------------------------------------

	public void enterBuilding() {
		//System.out.println("---------------------"+this);
		s = state.ready;
		System.out.println("I am a guard");
		B_Bank bank = (B_Bank)myPerson.building;
		bank.setBankGuard(this);
		manager = bank.getBankManager();
		manager.setGuard(this);
		bankGui bankgui = (bankGui)myPerson.building.getPanel();
		bankgui.addGui(gui);
		bankgui.repaint();
		gui.setText("Guard");
		stateChanged();
	}

	public void wantEnter(Customer newC) {
		System.out.println("Guard: Customer wants to enter");
		Entry c = new Entry();
		c.s = state.entered;
		c.bc = newC;
		custEnter.add(c);
		System.out.println("Myperson.mainrole: "+myPerson.mainRole+" Guard address: "+this+" Size: "+custEnter.size());
		stateChanged();
	}
	
	public void RobberEnter(Robber newR) {
		System.out.println("Guard: Robber is forcing entry");
		Entry c = new Entry();
		c.s = state.robber;
		c.r = newR;
		custEnter.add(c);
		stateChanged();
	}
	
	@Override
	public void allowSearch(Customer newC, List<String> inventory) {
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

	public void doneMotion() {
		moving.release();
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
				if (c.s == state.robber) {
					forcedEntry(c);
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

	public void forcedEntry(Entry c) {
		c.r.yesEnter();
		custEnter.remove(c);
	}
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
		gui.doEnterBank();
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		s = state.onD;
	}

	@Override
	public void leaveBank() {
		gui.doLeaveBank();
		try {
			moving.acquire();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		s = state.offD;
		bankGui bg = (bankGui)myPerson.building.getPanel();
		bg.removeGui(gui);
		myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		exitBuilding(myPerson);
	}

	@Override
	public void setGui(bankGuardGui gui) {
		this.gui = gui;
	}

	@Override
	public bankGuardGui getGui() { 
		return gui;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "bankGuardRole";
	}
}
