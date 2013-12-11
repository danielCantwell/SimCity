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
import SimCity.trace.AlertTag;

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
		//Do(AlertTag.BANK,"---------------------"+this);
		s = state.ready;
		B_Bank bank = (B_Bank)myPerson.building;
		bank.setBankGuard(this);
		manager = bank.getBankManager();
		if(!(manager == null) && bank.getGuard() == null){
		manager.setGuard(this);
		bankGui bankgui = (bankGui)myPerson.building.getPanel();
		bankgui.addGui(gui);
		bankgui.repaint();
		Do(AlertTag.BANK,"I am a guard");
		gui.setText("Guard");
		stateChanged();
		} else exitBuilding(myPerson);
	}

	public void wantEnter(Customer newC) {
		Do(AlertTag.BANK,"Guard: Customer wants to enter");
		Entry c = new Entry();
		c.s = state.entered;
		c.bc = newC;
		custEnter.add(c);
		Do(AlertTag.BANK,"Myperson.mainrole: "+myPerson.mainRole+" Guard address: "+this+" Size: "+custEnter.size());
		stateChanged();
	}
	
	public void RobberEnter(Robber newR) {
		Do(AlertTag.BANK,"Guard: Believes there might be an invisible robber who entered but doesn't get paid enough to really care.");
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
		stateChanged();
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
		if(manager == null) { System.out.println("Guard: Leaving because there is no Manager");
		myPerson.msgGoToBuilding(myPerson.getHouse(), Intent.customer);
		bankGui bg = (bankGui)myPerson.building.getPanel();
		bg.removeGui(gui);
		exitBuilding(myPerson);
		}
		else {
		myPerson.getMoney().add(50, 0);
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
		exitBuilding(myPerson);
		}
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
		return "BGrd";
	}
}
